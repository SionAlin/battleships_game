package main.network.handshake;

import main.network.*;
import main.network.protocol.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class HandshakeSession implements ConnectionListener{
    
    private final Connection connection;
    private final Role myRole;
    private final String myName;
    private final long timeoutMs;
    private final HandshakeListener listener;
    private final AtomicBoolean done = new AtomicBoolean(false);
    private final Timer timer = new Timer("handshake-timeout", true);

    public HandshakeSession(Connection connection, Role myRole, String myName, long timeoutMs, HandshakeListener listener){
        this.connection = connection;
        this.myRole = myRole;
        this.myName = myName;
        this.timeoutMs = timeoutMs;
        this.listener = listener;
    }    
   
    public static HandshakeSession asGuest(Connection c, String guestName, long timeoutMs, HandshakeListener l){
        return new HandshakeSession(c, Role.GUEST, guestName, timeoutMs, l);
    }

    public static HandshakeSession asHost(Connection c, String hostName, long timeoutMs, HandshakeListener l){
        return new HandshakeSession(c, Role.HOST, hostName, timeoutMs, l);
    }

    public void begin(){
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                fail(HandshakeFailure.TIMEOUT);
            }
        }, timeoutMs);

        connection.start(this);

        if(myRole == Role.GUEST && !connection.send(Message.joinRequest(myName)))
            fail(HandshakeFailure.CONNECTION_LOST);
    }

    public void cancel(){
        fail(HandshakeFailure.CANCELLED);
    }

    @Override
    public void onMessage(Message message){
        if(done.get()) return;
        if(myRole == Role.GUEST) onGuestMessage(message);
        else onHostMessage(message);
    }

    private void onGuestMessage(Message m){
        switch(m.getType()){
            case JOIN_ACCEPTED:
                succeed(new HandshakeResult(connection, Role.GUEST, m.getName()));
                break;
            case JOIN_REJECTED:
                fail(HandshakeFailure.REJECTED);
                break;
            default:
                fail(HandshakeFailure.INVALID_RESPONSE);
        }
    }

    private void onHostMessage(Message m){
        if(m.getType() != MessageType.JOIN_REQUEST){
            fail(HandshakeFailure.INVALID_RESPONSE);
            return;
        }

        String guest = m.getName().trim();
        if(guest.isEmpty() || guest.length() > 32 || guest.equalsIgnoreCase(myName)){
            connection.send(Message.joinRejected());
            fail(HandshakeFailure.REJECTED);
            return;
        }

        if(!connection.send(Message.joinAccepted(myName, Role.HOST))){
            fail(HandshakeFailure.CONNECTION_LOST);
            return;
        }

        succeed(new HandshakeResult(connection, Role.HOST, guest));
    }

    @Override
    public void onDisconnect(){
        fail(HandshakeFailure.CONNECTION_LOST);
    }

    private void succeed(HandshakeResult result){
        if(!done.compareAndSet(false, true)) return;
        timer.cancel();
        connection.detach();
        listener.onSuccess(result);
    }

    private void fail(HandshakeFailure reason){
        if(!done.compareAndSet(false, true)) return;
        timer.cancel();
        connection.close();
        listener.onFailure(reason);
    }
}
