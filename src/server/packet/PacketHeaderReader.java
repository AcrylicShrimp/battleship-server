package server.packet;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class PacketHeaderReader {
    public static final int SIZE = Integer.BYTES * 2;
    private final ByteBuffer buffer;

    public PacketHeaderReader() {
        this.buffer = ByteBuffer.allocate(SIZE);
    }

    public Result read(ReadableByteChannel channel) throws Exception {
        if (channel.read(buffer) == -1)
            throw new Exception();

        if (this.buffer.position() != SIZE)
            return null;

        buffer.flip();

        int type = this.buffer.getInt();
        int size = this.buffer.getInt();

        this.buffer.flip();

        return new Result(type, size);
    }

    public static class Result {
        public final int type;
        public final int size;

        public Result(int type, int size) {
            this.type = type;
            this.size = size;
        }
    }
}
