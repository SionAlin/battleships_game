package main.network;

import main.network.protocol.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Connection{
    
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private volatile boolean closed = false;
    private ConnectionListener listener;
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

    public void start(ConnectionListener listener){
        if(reader != null)
            throw new IllegalStateException("Connection already started");

        this.listener = listener;
        reader = new Thread(this::readLoop, "connection-reader");
        reader.setDaemon(true);
        reader.start();
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
                listener.onMessage(message);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            boolean wasClosedByUs = closed;
            closeQuietly();
            if(!wasClosedByUs)
                listener.onDisconnect();
        }
    }

    public synchronized boolean send(Message message){
        if(closed) return false;
        out.println(MessageSerializer.serialize(message));
        if(out.checkError()){
            closeQuietly();
            return false;
        }
        return true;
    }

    public void close(){
        closeQuietly();
    }

    private void closeQuietly(){
        closed = true;
        try{
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
