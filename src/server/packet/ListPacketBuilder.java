package server.packet;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class ListPacketBuilder<T extends PacketBuilder> implements PacketBuilder {
	private final List<T> builders;
	private final int     size;

	public ListPacketBuilder(T[] builders) {
		this.builders = Arrays.asList(builders);
		this.size = this.builders.stream().map(builder -> builder.size()).reduce(0, Integer::sum);
	}

	public ListPacketBuilder(List<T> builders) {
		this.builders = builders;
		this.size = this.builders.stream().map(builder -> builder.size()).reduce(0, Integer::sum);
	}

	@Override
	public int size() {
		return Integer.BYTES + this.size;
	}

	@Override
	public void put(ByteBuffer buffer) {
		buffer.putInt(this.builders.size());

		for (T builder : this.builders)
			builder.put(buffer);
	}
}
