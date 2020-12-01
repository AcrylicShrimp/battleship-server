package server.packet;

import java.nio.ByteBuffer;

public class PacketHandler {
    public static void dispatch(int type, ByteBuffer buffer, PacketHandler handler) throws Exception {
        switch (type) {
            case PacketType.CLIENT_HELLO:
                handler.handle(new Packet.ClientHello(buffer));
                break;
            case PacketType.REQUEST_CREATE_ROOM:
                handler.handle(new Packet.RequestCreateRoom(buffer));
                break;
            case PacketType.REQUEST_ENTER_ROOM:
                handler.handle(new Packet.RequestEnterRoom(buffer));
                break;
            case PacketType.REQUEST_LEAVE_ROOM:
                handler.handle(new Packet.RequestLeaveRoom());
                break;
            case PacketType.REQUEST_RENAME_ROOM:
                handler.handle(new Packet.RequestRenameRoom(buffer));
                break;
            case PacketType.REQUEST_START_GAME:
                handler.handle(new Packet.RequestStartGame());
                break;
            case PacketType.GAME_INIT:
                handler.handle(new Packet.GameInit(buffer));
                break;
            case PacketType.GAME_FIRE:
                handler.handle(new Packet.GameFire(buffer));
                break;
            case PacketType.CHAT_NORMAL:
                handler.handle(new Packet.ChatNormal(buffer));
                break;
            case PacketType.CHAT_WHISPER:
                handler.handle(new Packet.ChatWhisper(buffer));
                break;
            default:
                break;
        }
    }

    public void handle(Packet.ClientHello packet) {
        // Do nothing.
    }

    public void handle(Packet.RequestCreateRoom packet) {
        // Do nothing.
    }

    public void handle(Packet.RequestEnterRoom packet) {
        // Do nothing.
    }

    public void handle(Packet.RequestLeaveRoom packet) {
        // Do nothing.
    }

    public void handle(Packet.RequestRenameRoom packet) {
        // Do nothing.
    }

    public void handle(Packet.RequestStartGame packet) {
        // Do nothing.
    }

    public void handle(Packet.GameInit packet) {
        // Do nothing.
    }

    public void handle(Packet.GameFire packet) {
        // Do nothing.
    }

    public void handle(Packet.ChatNormal packet) {
        // Do nothing.
    }

    public void handle(Packet.ChatWhisper packet) {
        // Do nothing.
    }
}
