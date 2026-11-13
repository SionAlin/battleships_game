package main.network.server;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GameServer{

    private ServerSocket server;
    
    public static void main(String[] args){
        try{
            GameServer server = new GameServer(5000);
            System.out.println("Server started. Waiting for client ...");
            Socket client = server.acceptConnection();
            System.out.println("Client connected!");
            
            BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);

            String line = in.readLine();
            System.out.println("Received: " + line);
            out.println("WELCOME");

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public GameServer(int port) throws IOException{
        this.server = new ServerSocket(port);
    }

    public Socket acceptConnection() throws IOException{
        return server.accept();
    }

    public void close() throws IOException{
        server.close();
    }
}
