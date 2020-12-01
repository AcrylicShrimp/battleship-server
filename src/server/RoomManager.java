package server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class RoomManager
        extends ServerResource {
    private final Random random;
    private final HashMap<Integer, Room> roomMap;
    private final HashMap<Client, Room> clientRoomMap;

    public RoomManager(ServerResourceProvider provider) {
        super(provider);
        this.random = new Random(System.currentTimeMillis() + (long) (Math.random() * 1000000));
        this.roomMap = new HashMap<>();
        this.clientRoomMap = new HashMap<>();
    }

    public Collection<Room> rooms() {
        return this.roomMap.values();
    }

    public Room createRoom(String name, Client client) {
        if (!client.isReady())
            return null;

        if (this.clientRoomMap.containsKey(client))
            return null;

        int id = this.random.nextInt();
        Room room = new Room(this.provider, id, name);
        room.clientList.add(client);

        this.roomMap.put(id, room);
        this.clientRoomMap.put(client, room);

        return room;
    }

    /**
     * It tries to put the given client into a room that has the given id.
     *
     * @param id     A id of a room to enter to.
     * @param client A client want to enter.
     * @return The entered room if any.
     */
    public Room enterRoom(int id, Client client) {
        if (!client.isReady())
            return null;

        if (this.clientRoomMap.containsKey(client))
            return null;

        Room room = this.roomMap.get(id);

        if (room == null)
            return null;

        room.clientList.add(client);
        this.clientRoomMap.put(client, room);

        return room;
    }

    /**
     * It tries to remove the given client from a room that the client has been entered.
     *
     * @param client A client to be removed.
     * @return The left room if any.
     */
    public Room leaveRoom(Client client) {
        if (!client.isReady())
            return null;

        Room room = this.clientRoomMap.get(client);

        if (room == null)
            return null;

        room.clientList.remove(client);
        this.clientRoomMap.remove(client);

        if (room.clientList.isEmpty())
            this.roomMap.remove(room.id);

        return room;
    }

    public boolean renameRoom(String name, Client client) {
        if (!client.isReady())
            return false;

        Room room = this.clientRoomMap.get(client);

        if (room == null)
            return false;

        room.name = name;

        return true;
    }
}
