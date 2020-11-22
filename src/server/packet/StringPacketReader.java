package server.packet;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringPacketReader implements PacketReader<String> {

	@Override
	public int size() {
		return Integer.BYTES;
	}

	@Override
	public String read(ByteBuffer buffer) throws Exception {
		int length = buffer.getInt();

		if (length < 0 || 2048 < length)
			throw new Exception();

		return StandardCharsets.UTF_8.decode(buffer).toString();
	}
}
