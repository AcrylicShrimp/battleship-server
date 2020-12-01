package server.packet;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class PacketBodyReader {
    private PacketHeaderReader.Result headerResult;
    private final PacketHeaderReader headerReader;
    private ByteBuffer buffer;
    public PacketBodyReader() {
        this.headerResult = null;
        this.headerReader = new PacketHeaderReader();
        this.buffer = ByteBuffer.allocate(8);
    }

    public Result read(ReadableByteChannel channel) throws Exception {
        if (this.headerResult == null) {
            this.headerResult = this.headerReader.read(channel);

            if (this.headerResult == null)
                return null;

            if (2048 < this.headerResult.size)
                throw new Exception();

            if (this.buffer.capacity() < this.headerResult.size)
                this.buffer = ByteBuffer.allocate(this.headerResult.size);
            else
                this.buffer.limit(this.headerResult.size);

            this.buffer.position(0);
        }

        if (channel.read(this.buffer) == -1)
            throw new Exception();

        if (this.buffer.position() != this.headerResult.size)
            return null;

        Result result = new Result(this.headerResult.type, this.buffer.flip().slice());
        this.headerResult = null;
        return result;
    }

    public static class Result {
        public final int type;
        public final ByteBuffer buffer;

        public Result(int type, ByteBuffer buffer) {
            this.type = type;
            this.buffer = buffer;
        }
    }
}
