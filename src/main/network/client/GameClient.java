package main.network.client;

import main.network.Connection;
import main.network.handshake.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GameClient{

    private final String ip;
    private final int port;
    private final String guestName;
    private final int connectTimeoutMs;
    private final long handshakeTimeoutMs;

    private volatile boolean cancelled = false;
    private volatile Socket socket;
    private volatile HandshakeSession session;


    public GameClient(String ip, int port, String guestName, int connectTimeoutMs, long handshakeTimeoutMs){
        this.ip = ip;
        this.port = port;
        this.guestName = guestName;
        this.connectTimeoutMs = connectTimeoutMs;
        this.handshakeTimeoutMs = handshakeTimeoutMs;
    }
    
    public void start(HandshakeListener listener){
        Thread thread = new Thread(() -> connect(listener), "client-connect");
        thread.setDaemon(true);
        thread.start();
    }
    
    public void cancel(){
        cancelled = true;
        HandshakeSession s = session;
        if(s != null) s.cancel();
        else closeQuietly(socket);
    }

    private void connect(HandshakeListener listener){
        Socket s = new Socket();
        socket = s;

        try{
            s.connect(new InetSocketAddress(ip, port), connectTimeoutMs);
        }catch(SocketTimeoutException e){
            closeQuietly(s);
            listener.onFailure(cancelled ? HandshakeFailure.CANCELLED : HandshakeFailure.TIMEOUT);
            return;
        }catch(IOException e){
            closeQuietly(s);
            listener.onFailure(cancelled ? HandshakeFailure.CANCELLED : HandshakeFailure.CONNECTION_REFUSED);
            return;
        }

        if(cancelled){
            closeQuietly(s);
            listener.onFailure(HandshakeFailure.CANCELLED);
            return;
        }

        Connection connection;
        try{
            connection = new Connection(s);
        }catch(IOException e){
            closeQuietly(s);
            listener.onFailure(HandshakeFailure.CONNECTION_LOST);
            return;
        }

        session = HandshakeSession.asGuest(connection, guestName, handshakeTimeoutMs, listener);
        session.begin();
        if(cancelled) session.cancel();
    }

    public static void closeQuietly(Socket s){
        if(s == null) return;
        try{
            s.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
