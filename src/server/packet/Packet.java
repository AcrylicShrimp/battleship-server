package server.packet;

import java.nio.ByteBuffer;

public class Packet {
    public static class ClientHello {
        public String name;

        public ClientHello(ByteBuffer buffer) throws Exception {
            this.name = PacketReader.string(buffer);
        }
    }

    public static class RequestCreateRoom {
        public String roomName;

        public RequestCreateRoom(ByteBuffer buffer) throws Exception {
            this.roomName = PacketReader.string(buffer);
        }
    }

    public static class RequestEnterRoom {
        public int roomId;

        public RequestEnterRoom(ByteBuffer buffer) {
            this.roomId = PacketReader.integer(buffer);
        }
    }

    public static class RequestLeaveRoom {
    }

    public static class RequestRenameRoom {
        public String roomName;

        public RequestRenameRoom(ByteBuffer buffer) throws Exception {
            this.roomName = PacketReader.string(buffer);
        }
    }

    public static class ChatNormal {
        public String message;

        public ChatNormal(ByteBuffer buffer) throws Exception {
            this.message = PacketReader.string(buffer);
        }
    }

    public static class ChatWhisper {
        public int    clientId;
        public String message;

        public ChatWhisper(ByteBuffer buffer) throws Exception {
            this.clientId = PacketReader.integer(buffer);
            this.message  = PacketReader.string(buffer);
        }
    }
}
