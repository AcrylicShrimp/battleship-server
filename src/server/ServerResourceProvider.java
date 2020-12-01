package server;

import java.nio.channels.Selector;

public interface ServerResourceProvider {
	Selector selector();

	SendManager sendManager();

	ClientManager clientManager();

	LobbyManager lobbyManager();

	RoomManager roomManager();

	GameManager gameManager();
}
