package server.logic;

import server.packet.PacketHeader;

import java.nio.ByteBuffer;

public abstract class ClientState<T extends Context> implements ClientLogic {
	protected final T             context;
	protected       ByteBuffer    buffer;

	public ClientState(T context) {
		this.context = context;
		this.buffer = null;
	}

	@Override
	public ClientLogic step() {
		return null;
	}

	public abstract void onPacketHeader(PacketHeader header);
}
