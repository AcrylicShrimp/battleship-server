package server.logic;

import server.Client;
import server.ServerResourceProvider;

public class Context {
	public final Client                 client;
	public final ServerResourceProvider provider;

	public Context(Client client, ServerResourceProvider provider) {
		this.client = client;
		this.provider = provider;
	}
}
