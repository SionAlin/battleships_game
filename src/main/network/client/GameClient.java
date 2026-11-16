package main.network.client;

import main.network.Connection;
import main.network.protocol.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GameClient{

    private Connection connection;

    public GameClient(String ip, int port) throws IOException{
        this.connection = new Connection(new Socket(ip, port));
    }

    public Connection getConnection(){
        return connection;
    }
}
