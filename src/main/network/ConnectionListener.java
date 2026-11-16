package main.network;

import main.network.protocol.Message;

public interface ConnectionListener{
    void onMessage(Message message);
    void onDisconnect();
}
