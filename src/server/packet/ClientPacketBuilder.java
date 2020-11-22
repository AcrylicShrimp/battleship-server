package server.packet;

import server.Client;

import java.nio.ByteBuffer;

public class ClientPacketBuilder implements PacketBuilder {
	private final IntegerPacketBuilder idBuilder;
	private final StringPacketBuilder  nameBuilder;

	public ClientPacketBuilder(Client client) {
		this.idBuilder = new IntegerPacketBuilder(client.id);
		this.nameBuilder = new StringPacketBuilder(client.name);
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
