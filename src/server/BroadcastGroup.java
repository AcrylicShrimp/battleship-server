package server;

import java.nio.ByteBuffer;

public interface BroadcastGroup {
    void broadcast(ByteBuffer buffer);

    void broadcastExcept(ByteBuffer buffer, Client except);
}
