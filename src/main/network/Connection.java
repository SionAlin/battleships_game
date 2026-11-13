package main.network;

import main.network.protocol.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Connection{
    
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    public Connection(Socket socket) throws IOException{
        this.socket = socket;
        this.in = new BufferedReader(
            new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
        );
        this.out = new PrintWriter(
            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true
        );
    }

    public void send(String message){
        out.println(message);
    }

    public String receive() throws IOException{
        return in.readLine();
    }

    public void close() throws IOException{
        socket.close();
    }
}
