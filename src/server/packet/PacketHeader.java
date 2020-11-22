package server.packet;

import java.nio.ByteBuffer;

public class PacketHeader {
	public final int type;
	public final int size;

	public PacketHeader(int type, int size) {
		this.type = type;
		this.size = size;
	}

	public static PacketHeader fromBuffer(ByteBuffer buffer) {
		if (buffer.position() < Integer.BYTES * 2)
			return null;

		return new PacketHeader(buffer.getInt(), buffer.getInt());
	}

	public void toBuffer(ByteBuffer buffer) {
		buffer.putInt(this.type);
		buffer.putInt(this.size);
	}
}
