package server.packet;

import java.nio.ByteBuffer;

public class IntegerPacketBuilder implements PacketBuilder {
	private final int value;

	public IntegerPacketBuilder(int value) {
		this.value = value;
	}

	@Override
	public int size() {
		return Integer.BYTES;
	}

	@Override
	public void put(ByteBuffer buffer) {
		buffer.putInt(this.value);
	}
}
