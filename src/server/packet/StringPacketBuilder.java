package server.packet;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringPacketBuilder implements PacketBuilder {
	private final ByteBuffer buffer;

	public StringPacketBuilder(String value) {
		this.buffer = StandardCharsets.UTF_8.encode(value);
	}

	@Override
	public int size() {
		return Integer.BYTES + this.buffer.position();
	}

	@Override
	public void put(ByteBuffer buffer) {
		buffer.putInt(this.buffer.position()).put(this.buffer);
	}
}
