package server.packet;

import java.nio.ByteBuffer;

public class IntegerPacketReader implements PacketReader<Integer> {

	@Override
	public int size() {
		return Integer.BYTES;
	}

	@Override
	public Integer read(ByteBuffer buffer) throws Exception {
		return buffer.getInt();
	}
}
