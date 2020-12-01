package server.logic;

import server.Client;
import server.Game;
import server.Room;
import server.packet.Packet;
import server.packet.PacketBuilder;

public class RoomLogic
        extends Logic {
    public final Room room;

    public RoomLogic(Client client, Room room) {
        super(client);
        this.room = room;
    }

    @Override
    public void handleEvent(String type, Object arg) {
        if (type.equals("game:init")) {
            Game.EventArg.Init init = (Game.EventArg.Init) arg;
            this.client.sendPacket(PacketBuilder.buildGameInit(init.isBlue));
            this.client.setLogic(new GameLogic(this.client, init));
            this.client.provider.roomManager().leaveRoom(this.client);
        }
    }

    @Override
    public void handle(Packet.RequestLeaveRoom packet) {
        Room room = this.client.provider.roomManager().leaveRoom(this.client);

        if (room == null) {
            this.client.sendPacket(PacketBuilder.buildRejectLeaveRoomNotInRoom());
            return;
        }

        if (room.clientList.isEmpty())
            this.client.provider.lobbyManager().broadcast(PacketBuilder.buildNotifyLobbyRoomRemoved(room));
        else
            room.broadcast(PacketBuilder.buildNotifyRoomLeaveRoom(this.client));

        this.client.sendPacket(PacketBuilder.buildNotifyLobby(this.client.provider.lobbyManager().clients(), this.client.provider.roomManager().rooms()));
        this.client.provider.lobbyManager().broadcast(PacketBuilder.buildNotifyLobbyEnterLobby(this.client));
        this.client.provider.lobbyManager().addClient(this.client);
        this.client.setLogic(new LobbyLogic(this.client));

        System.out.printf("The client %s has been left the room %s.\n", this.client, this.room);
    }

    @Override
    public void handle(Packet.RequestRenameRoom packet) {
        if (!this.client.provider.roomManager().renameRoom(packet.roomName, this.client))
            return;

        this.room.broadcast(PacketBuilder.buildNotifyRoomRoomRenamed(this.room));
        this.client.provider.lobbyManager().broadcast(PacketBuilder.buildNotifyLobbyRoomRenamed(this.room));

        System.out.printf("The client %s has been renamed the room %s.\n", this.client, this.room);
    }

    @Override
    public void handle(Packet.RequestStartGame packet) {
        if (this.room.clientList.size() != 2)
            return;

        Game game = new Game(this.room.provider, this.room.clientList.get(0), this.room.clientList.get(1));
        this.client.provider.gameManager().addGame(game);

        System.out.printf("The client %s has been started a new game.\n", this.client);
    }

    @Override
    public void handle(Packet.ChatNormal packet) {
        this.room.broadcast(PacketBuilder.buildBroadcastChatNormal(this.client, packet.message));

        System.out.printf("The client %s has been sent a message in the room %s: %s\n", this.client, this.room, packet.message);
    }

    @Override
    public void handle(Packet.ChatWhisper packet) {
        Client target = this.client.provider.clientManager().getClientById(packet.clientId);

        if (target == null)
            return;

        target.sendPacket(PacketBuilder.buildBroadcastChatWhisper(this.client, packet.message));

        System.out.printf("The client %s has been sent a message to the client %s: %s\n", this.client, target, packet.message);
    }
}
