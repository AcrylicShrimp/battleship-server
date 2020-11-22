package server;

public abstract class ServerResource {
	protected ServerResourceProvider provider;

	public ServerResource(ServerResourceProvider provider) {
		this.provider = provider;
	}
}
