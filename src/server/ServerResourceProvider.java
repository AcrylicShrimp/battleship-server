package server;

import java.nio.channels.Selector;

public interface ServerResourceProvider {
	Selector selector();

	ClientManager clientManager();

	LobbyManager lobbyManager();
}
