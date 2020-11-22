package server;

import java.util.ArrayList;
import java.util.Objects;

public class Room implements ChatGroup {
	public final ArrayList<Client> clientList;
	public       int               id;
	public       String            name;

	public Room(int id, String name) {
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
		return Objects.hash(id);
	}

	@Override
	public void broadcast() {
		// TODO: Broadcast the given data to the clients.
	}
}
