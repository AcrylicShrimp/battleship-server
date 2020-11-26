package server.logic;

import server.Client;
import server.Room;
import server.packet.Packet;
import server.packet.PacketBuilder;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LobbyLogic
        extends Logic {
    public LobbyLogic(Client client) {
        super(client);
    }

    @Override
    public void handleEvent(String type) {
        // Do nothing.
    }

    @Override
    public void handle(Packet.RequestCreateRoom packet) {
        Room room = this.client.provider.roomManager().createRoom(packet.roomName, this.client);

        if (room == null)
            return;

        this.client.provider.lobbyManager().removeClient(this.client);
        this.client.provider.lobbyManager().broadcast(PacketBuilder.buildNotifyLobbyLeaveLobby(client));
        this.client.provider.lobbyManager().broadcast(PacketBuilder.buildNotifyLobbyRoomCreated(room));
        this.client.sendPacket(PacketBuilder.buildNotifyRoom(room, new ArrayList<>()));
        this.client.setLogic(new RoomLogic(this.client, room));
    }

    @Override
    public void handle(Packet.RequestEnterRoom packet) {
        Room room = this.client.provider.roomManager().enterRoom(packet.roomId, this.client);

        if (room == null) {
            this.client.sendPacket(PacketBuilder.buildRejectEnterRoomNotFound());
            return;
        }

        room.broadcastExcept(PacketBuilder.buildNotifyRoomEnterRoom(this.client), this.client);
        this.client.provider.lobbyManager().removeClient(this.client);
        this.client.provider.lobbyManager().broadcast(PacketBuilder.buildNotifyLobbyLeaveLobby(client));
        this.client.sendPacket(PacketBuilder.buildNotifyRoom(room,
                                                             room.clientList.stream().filter(other -> !other.equals(client)).collect(Collectors.toList())));
        this.client.setLogic(new RoomLogic(this.client, room));
    }

    @Override
    public void handle(Packet.RequestLeaveRoom packet) {
        this.client.sendPacket(PacketBuilder.buildRejectLeaveRoomNotInRoom());
    }

    @Override
    public void handle(Packet.ChatNormal packet) {
        this.client.provider.lobbyManager().broadcast(PacketBuilder.buildBroadcastChatNormal(this.client, packet.message));
    }

    @Override
    public void handle(Packet.ChatWhisper packet) {
        Client target = this.client.provider.clientManager().getClientById(packet.clientId);

        if (target == null)
            return;

        target.sendPacket(PacketBuilder.buildBroadcastChatWhisper(this.client, packet.message));
    }
}
