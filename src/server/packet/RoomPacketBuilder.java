package server.packet;

import server.Room;

import java.nio.ByteBuffer;

public class RoomPacketBuilder implements PacketBuilder {
	private final IntegerPacketBuilder idBuilder;
	private final StringPacketBuilder  nameBuilder;

	public RoomPacketBuilder(Room room) {
		this.idBuilder = new IntegerPacketBuilder(room.id);
		this.nameBuilder = new StringPacketBuilder(room.name);
	}

	@Override
	public int size() {
		return this.idBuilder.size() + this.nameBuilder.size();
	}

	@Override
	public void put(ByteBuffer buffer) {
		this.idBuilder.put(buffer);
		this.nameBuilder.put(buffer);
	}
}
