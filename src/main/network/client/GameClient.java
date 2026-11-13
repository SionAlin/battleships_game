package main.network.client;

import main.network.Connection;
import main.network.protocol.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GameClient{

    private Connection connection;

    public static void main(String[] args){
        try{
            GameClient client = new GameClient("localhost", 5000);
            System.out.println("Connected to server!");

            Connection conn = client.getConnection();

            conn.send("HELLO");
            String reply = conn.receive();
            System.out.println("Server saoid: " + reply);

            conn.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public GameClient(String ip, int port) throws IOException{
        this.connection = new Connection(new Socket(ip, port));
    }

    public Connection getConnection(){
        return connection;
    }
}
