package server;

import java.util.HashMap;
import java.util.Random;

public class LobbyManager extends ServerResource {
	private final Random                 random;
	private final HashMap<Integer, Room> roomMap;
	private final HashMap<Client, Room>  clientRoomMap;

	public LobbyManager(ServerResourceProvider provider) {
		super(provider);
		this.random = new Random(System.currentTimeMillis() + (long) (Math.random() * 1000000));
		this.roomMap = new HashMap<>();
		this.clientRoomMap = new HashMap<>();
	}

	public boolean createRoom(String name, Client client) {
		if (!client.isReady())
			return false;

		if (this.clientRoomMap.containsKey(client))
			return false;

		int  id   = this.random.nextInt();
		Room room = new Room(id, name);
		room.clientList.add(client);

		this.roomMap.put(id, room);
		this.clientRoomMap.put(client, room);

		return true;
	}

	/**
	 * It tries to put the given client into a room that has the given id.
	 *
	 * @param id     A id of a room to enter to.
	 * @param client A client want to enter.
	 *
	 * @return true on success. false otherwise.
	 */
	public boolean enterRoom(int id, Client client) {
		if (!client.isReady())
			return false;

		if (this.clientRoomMap.containsKey(client))
			return false;

		Room room = this.roomMap.get(id);

		if (room == null)
			return false;

		room.clientList.add(client);
		this.clientRoomMap.put(client, room);

		return true;
	}

	/**
	 * It tries to remove the given client from a room that the client has been entered.
	 *
	 * @param client A client to be removed.
	 *
	 * @return The empty room if any. null otherwise.
	 */
	public Room leaveRoom(Client client) {
		if (!client.isReady())
			return null;

		Room room = this.clientRoomMap.get(client);

		if (room == null)
			return null;

		room.clientList.remove(client);
		this.clientRoomMap.remove(client);

		// NOTE: Returns null if the room is not empty.
		if (!room.clientList.isEmpty())
			return null;

		this.roomMap.remove(room);
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
