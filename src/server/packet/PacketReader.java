package server.packet;

import java.nio.ByteBuffer;

public interface PacketReader<T> {
	int size();
	T read(ByteBuffer buffer) throws Exception;
}
