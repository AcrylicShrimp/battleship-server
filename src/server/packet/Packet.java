package server.packet;

import java.nio.ByteBuffer;

public class Packet {
    public static class ClientHello {
        public final String name;

        public ClientHello(ByteBuffer buffer) throws Exception {
            this.name = PacketReader.string(buffer);
        }
    }

    public static class RequestCreateRoom {
        public final String roomName;

        public RequestCreateRoom(ByteBuffer buffer) throws Exception {
            this.roomName = PacketReader.string(buffer);
        }
    }

    public static class RequestEnterRoom {
        public final int roomId;

        public RequestEnterRoom(ByteBuffer buffer) {
            this.roomId = PacketReader.integer(buffer);
        }
    }

    public static class RequestLeaveRoom {
    }

    public static class RequestRenameRoom {
        public final String roomName;

        public RequestRenameRoom(ByteBuffer buffer) throws Exception {
            this.roomName = PacketReader.string(buffer);
        }
    }

    public static class RequestStartGame {
    }

    public static class ChatNormal {
        public final String message;

        public ChatNormal(ByteBuffer buffer) throws Exception {
            this.message = PacketReader.string(buffer);
        }
    }

    public static class ChatWhisper {
        public final int clientId;
        public final String message;

        public ChatWhisper(ByteBuffer buffer) throws Exception {
            this.clientId = PacketReader.integer(buffer);
            this.message = PacketReader.string(buffer);
        }
    }

    public static class GameInit {
        public final Ship[] ships;

        public GameInit(ByteBuffer buffer) {
            this.ships = new Ship[4];
            this.ships[0] = new Ship(buffer.getInt(), buffer.getInt(), buffer.getInt() != 0);
            this.ships[1] = new Ship(buffer.getInt(), buffer.getInt(), buffer.getInt() != 0);
            this.ships[2] = new Ship(buffer.getInt(), buffer.getInt(), buffer.getInt() != 0);
            this.ships[3] = new Ship(buffer.getInt(), buffer.getInt(), buffer.getInt() != 0);
        }

        public static class Ship {
            public final int x;
            public final int y;
            public final boolean rotated;

            public Ship(int x, int y, boolean rotated) {
                this.x = x;
                this.y = y;
                this.rotated = rotated;
            }
        }
    }

    public static class GameFire {
        public final int x;
        public final int y;

        public GameFire(ByteBuffer buffer) {
            this.x = buffer.getInt();
            this.y = buffer.getInt();
        }
    }
}
