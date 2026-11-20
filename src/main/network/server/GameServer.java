package main.network.server;

import main.network.Connection;
import main.network.handshake.*;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GameServer{

    private final ServerSocket server;
    private final String hostName;
    private final long handshakeTimeoutMs;

    private volatile boolean cancelled = false;
    private volatile HandshakeSession current;
    
    public GameServer(int port, String hostName, long handshakeTimeoutMs) throws IOException{
        this.server = new ServerSocket(port);
        this.hostName = hostName;
        this.handshakeTimeoutMs = handshakeTimeoutMs;
    }
    
    public int getPort(){
        return server.getLocalPort();
    }

    public void start(HandshakeListener listener){
        Thread thread = new Thread(() -> acceptLoop(listener), "host-accept");
        thread.setDaemon(true);
        thread.start();
    }

    public void cancel(){
        cancelled = true;
        closeServer();
        HandshakeSession s = current;
        if(s != null) s.cancel();
    }

    private void acceptLoop(HandshakeListener listener){
        while(true){
            Socket socket;
            try{
                socket = server.accept();
            }catch(IOException e){
                closeServer();
                listener.onFailure(cancelled ? HandshakeFailure.CANCELLED : HandshakeFailure.CONNECTION_LOST);
                return;
            }

            HandshakeResult result = handshakeWith(socket);

            if(cancelled){
                if(result != null) result.getConnection().close();
                closeServer();
                listener.onFailure(HandshakeFailure.CANCELLED);
                return;
            }
            if(result != null){
                closeServer();
                listener.onSuccess(result);
                return;
            }
        }
    }
   
    private HandshakeResult handshakeWith(Socket socket){
        CompletableFuture<HandshakeResult> outcome = new CompletableFuture<>();
        try{
            Connection connection = new Connection(socket);
            HandshakeSession session = HandshakeSession.asHost(connection, hostName, handshakeTimeoutMs, new HandshakeListener(){
                @Override public void onSuccess(HandshakeResult r){ outcome.complete(r); }
                @Override public void onFailure(HandshakeFailure f){ outcome.complete(null); }
            });
            current = session;
            session.begin();
            return outcome.get();
        }catch(IOException e){
            closeQuietly(socket);
            return null;
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            return null;
        }catch(ExecutionException e){
            return null;
        }finally{
            current = null;
        }
    }

    private void closeServer(){
        try{
            server.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void closeQuietly(Socket s){
        try{
            s.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
