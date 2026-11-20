package main.network.handshake;

public interface HandshakeListener{
    void onSuccess(HandshakeResult result);
    void onFailure(HandshakeFailure reason);
}
