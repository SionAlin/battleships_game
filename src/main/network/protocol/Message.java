package main.network.protocol;

import main.model.ShotResult;

public class Message{
    
    private final MessageType type;
    private String name = "";
    private Role firstPlayer = null;
    private int row = -1;
    private int col = -1;
    private ShotResult result = null;
    private boolean gameOver = false;
    private String layout = "";

    private Message(MessageType type){
        this.type = type; 
    }

    public static Message joinRequest(String guestName){
        Message m = new Message(MessageType.JOIN_REQUEST);
        m.name = guestName;
        return m;
    } 

    public static Message joinAccepted(String hostName, Role firstPlayer){
        Message m = new Message(MessageType.JOIN_ACCEPTED);
        m.name = hostName;
        m.firstPlayer = firstPlayer;
        return m;
    }

    public static Message joinRejected(){
        return new Message(MessageType.JOIN_REJECTED);
    }

    public static Message shipsReady(){
        return new Message(MessageType.SHIPS_READY);
    }

    public static Message attack(int row, int col){
        Message m = new Message(MessageType.ATTACK);
        m.row = row;
        m.col = col;
        return m;
    }

    public static Message attackResult(int row, int col, ShotResult result, boolean gameOver){
        Message m = new Message(MessageType.ATTACK_RESULT);
        m.row = row;
        m.col = col;
        m.result = result;
        m.gameOver = gameOver;
        return m;
    }

    public static Message boardReveal(String layout){
        Message m = new Message(MessageType.BOARD_REVEAL);
        m.layout = layout;
        return m;
    }

    public MessageType getType(){ return type; }
    public String getName(){ return name; }
    public Role getFirstPlayer(){ return firstPlayer; }
    public int getRow(){ return row; }
    public int getCol(){ return col; }
    public ShotResult getResult(){ return result; }
    public boolean inGameOver(){ return gameOver; }
    public String getLayout(){ return layout; }
}
