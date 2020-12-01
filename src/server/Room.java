package server;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Room
        implements BroadcastGroup {
    public final ServerResourceProvider provider;
    public final ArrayList<Client> clientList;
    public int id;
    public String name;

    public Room(ServerResourceProvider provider, int id, String name) {
        this.provider = provider;
        this.id = id;
        this.name = name;
        this.clientList = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Room room = (Room) o;
        return id == room.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public void broadcast(ByteBuffer buffer) {
        for (Client client : clientList)
            this.provider.sendManager().addPacket(client, buffer);
    }

    @Override
    public void broadcastExcept(ByteBuffer buffer, Client except) {
        for (Client client : clientList)
            if (!client.equals(except))
                this.provider.sendManager().addPacket(client, buffer);
    }
}
