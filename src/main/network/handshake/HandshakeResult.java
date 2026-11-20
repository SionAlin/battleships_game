package main.network.handshake;

import main.network.Connection;
import main.network.protocol.Role;

public class HandshakeResult{

    private final Connection connection;
    private final Role role;
    private final String opponentName;
    private final boolean startFirst;

    public HandshakeResult(Connection connection, Role role, String opponentName){
        this.connection = connection;
        this.role = role;
        this.opponentName = opponentName;
        this.startFirst = (role == Role.HOST);
    }

    public Connection getConnection(){
        return connection;
    }

    public Role getRole(){
        return role;
    }

    public String getOpponentName(){
        return opponentName;
    }

    public boolean isStartFirst(){
        return startFirst;
    }
}
