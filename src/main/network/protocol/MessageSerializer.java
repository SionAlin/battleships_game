package main.network.protocol;

import main.model.ShotResult;

public class MessageSerializer{
    
    private static final int SIZE = 10;

    private MessageSerializer(){}
        
    public static String serialize(Message m){
        MessageType t = m.getType();
        switch(t){
            case JOIN_REQUEST:
                return t + " " + clean(m.getName());
            case JOIN_ACCEPTED:
                return t + " " + m.getFirstPlayer() + " " + clean(m.getName());
            case ATTACK:
                return t + " " + m.getRow() + " " + m.getCol();
            case ATTACK_RESULT:
                return t + " " + m.getRow() + " " + m.getCol() + " " + m.getResult() + " " + (m.isGameOver() ? 1 : 0);
            case BOARD_REVEAL:
                return t + " " + m.getLayout();
            default:
                return t.toString();
        }
    }

    public static Message parse(String line){
        if(line == null || line.isBlank()){
            throw new IllegalArgumentException("Empty message");
        }

        String[] head = line.trim().split(" ", 2);
        String rest = head.length > 1 ? head[1].trim() : "";

        MessageType type;
        try{
            type = MessageType.valueOf(head[0]);
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Unknown message type: " + head[0]);
        }

        switch(type){
            case JOIN_REQUEST:
                return Message.joinRequest(nonEmpty(rest));
            case JOIN_ACCEPTED: {
                String[] p = rest.split(" ", 2);
                if(p.length < 2)
                    throw new IllegalArgumentException("JOIN_ACCEPTED needs role and name");
                return Message.joinAccepted(nonEmpty(p[1]), Role.valueOf(p[0]));
            }
            case ATTACK:{
                String[] p = tokens(rest, 2);
                return Message.attack(cell(p[0]), cell(p[1]));
            }
            case ATTACK_RESULT:{
                String[] p = tokens(rest, 4);
                return Message.attackResult(cell(p[0]), cell(p[1]), ShotResult.valueOf(p[2]), flag(p[3]));
            }
            case BOARD_REVEAL:
                return Message.boardReveal(layout(rest));
            case JOIN_REJECTED:
                return Message.joinRejected();
            case SHIPS_READY:
                return Message.shipsReady();
            default:
                throw new IllegalArgumentException("Unhandled types: " + type);
        }
    }

    private static String clean(String s){
        return s.replace('\r', ' '). replace('\n', ' '). trim();
    }

    private static String nonEmpty(String s){
        if(s.isEmpty()) throw new IllegalArgumentException("Missing value");
        return s;
    }
    
    private static String[] tokens(String s, int expected){
        String[] p = s.isEmpty() ? new String[0] : s.split(" ");
        if(p.length != expected)
            throw new IllegalArgumentException("Expected " + expected + " fields, got " + p.length);
        return p;
    }

    private static int cell(String s){
        int v = Integer.parseInt(s);
        if(v < 0 || v >= SIZE)
            throw new IllegalArgumentException("Coordinate out of range: " + v);
        return v;
    }

    private static boolean flag(String s){
        if(s.equals("1")) return true;
        if(s.equals("0")) return false;
        throw new IllegalArgumentException("Invalid flag: " + s);
    }

    private static String layout(String s){
        if(s.length() != SIZE * SIZE || !s.matches("[01]+"))
            throw new IllegalArgumentException("Invalid board layout");
        return s;
    }
}
