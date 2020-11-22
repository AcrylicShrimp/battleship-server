package server.logic.states;

import server.Client;
import server.ServerResourceProvider;
import server.logic.ClientState;
import server.logic.Context;
import server.packet.PacketHeader;

public class InitState extends ClientState<Context> {
	public InitState(Client client, ServerResourceProvider provider) {
		super(new Context(client, provider));
	}

	@Override
	public void onInit() {

	}

	@Override
	public void onFin() {

	}

	@Override
	public void onPacketHeader(PacketHeader header) {

	}
}
