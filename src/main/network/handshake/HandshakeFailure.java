package main.network.handshake;

public enum HandshakeFailure{
    CONNECTION_REFUSED,
    TIMEOUT,
    REJECTED,
    INVALID_RESPONSE,
    CONNECTION_LOST,
    CANCELLED
}
