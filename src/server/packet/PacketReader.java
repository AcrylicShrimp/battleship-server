package server.packet;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class PacketReader {
    public static Integer integer(ByteBuffer buffer) {
        return buffer.getInt();
    }

    public static String string(ByteBuffer buffer) throws Exception {
        int length = buffer.getInt();

        if (length < 0 || 2048 < length)
            throw new Exception();

        byte[] bytes = new byte[length];
        buffer.get(bytes);

        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(bytes)).toString();
    }
}
