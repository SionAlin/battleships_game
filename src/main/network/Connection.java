package main.network;

import main.network.protocol.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class Connection{
    
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Object lock = new Object();
    private ConnectionListener listener;
    private final ArrayDeque<Message> pending = new ArrayDeque<>();
    private boolean disconnectPending = false;
    private Thread reader;

    public Connection(Socket socket) throws IOException{
        this.socket = socket;
        this.in = new BufferedReader(
            new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
        );
        this.out = new PrintWriter(
            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true
        );
    }
    
    public void switchListener(ConnectionListener listener){
        attach(listener);
    }

    public void start(ConnectionListener listener){
        if(reader != null)
            throw new IllegalStateException("Connection already started");

        attach(listener);
        reader = new Thread(this::readLoop, "connection-reader");
        reader.setDaemon(true);
        reader.start();
    }
    
    public void attach(ConnectionListener newListener){
        synchronized(lock){
            this.listener = newListener;
            if(newListener == null) return;

            while(listener == newListener && !pending.isEmpty())
                newListener.onMessage(pending.poll());

            if(listener == newListener && disconnectPending){
                disconnectPending = false;
                newListener.onDisconnect();
            }
        }
    }
    
    public void detach(){
        attach(null);
    }

    private void deliver(Message message){
        synchronized(lock){
            if(listener == null) pending.add(message);
            else listener.onMessage(message);
        }
    }

    private void deliverDisconnect(){
        synchronized(lock){
            if(listener == null) disconnectPending = true;
            else listener.onDisconnect();
        }
    }

    private void readLoop(){
        try{
            String line;
            while((line = in.readLine()) != null){
                Message message;
                try{
                    message = MessageSerializer.parse(line);
                }catch(IllegalArgumentException e){
                    System.err.println("Ignoring invalid message: " + e.getMessage());
                    continue;
                }
                deliver(message);
            }
        }catch(IOException e){
            if(!closed.get())
                e.printStackTrace();
        }finally{
            if(closed.compareAndSet(false, true)){
                closeQuietly();
                deliverDisconnect();
            }
        }
    }

    public synchronized boolean send(Message message){
        if(closed.get()) return false;
        out.println(MessageSerializer.serialize(message));
        if(out.checkError()){
            if(closed.compareAndSet(false, true)){
                closeQuietly();
                deliverDisconnect();
            }
            return false;
        }
        return true;
    }

    public void close(){
        if(closed.compareAndSet(false, true))
            closeQuietly();
    }

    private void closeQuietly(){
        try{
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
