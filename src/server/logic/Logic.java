package server.logic;

import server.Client;
import server.packet.PacketHandler;

public abstract class Logic extends PacketHandler {
    protected final Client client;

    public Logic(Client client) {
        this.client = client;
    }

    public abstract void handleEvent(String type);
}
