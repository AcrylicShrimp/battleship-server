package server.logic;

import server.Client;
import server.packet.Packet;
import server.packet.PacketBuilder;

public class ClientHelloLogic
        extends Logic {
    public ClientHelloLogic(Client client) {
        super(client);
    }

    @Override
    public void handleEvent(String type, Object arg) {
        // Do nothing.
    }

    @Override
    public void handle(Packet.ClientHello packet) {
        this.client.name = packet.name;
        this.client.sendPacket(PacketBuilder.buildServerHello(this.client));
        this.client.sendPacket(PacketBuilder.buildNotifyLobby(this.client.provider.lobbyManager().clients(), this.client.provider.roomManager().rooms()));
        this.client.provider.lobbyManager().broadcast(PacketBuilder.buildNotifyLobbyEnterLobby(this.client));
        this.client.provider.lobbyManager().addClient(this.client);
        this.client.setLogic(new LobbyLogic(this.client));

        System.out.printf("The client %s has been entered lobby.\n", this.client);
    }
}
