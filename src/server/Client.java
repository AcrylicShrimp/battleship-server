package server;

import server.logic.ClientLogic;
import server.logic.states.InitState;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Client {
	public final int                    id;
	public final SocketChannel          channel;
	public final SelectionKey           key;
	public final ServerResourceProvider provider;
	public       int                    index;
	public       String                 name;
	private      ClientLogic            logic;

	public Client(int id, SocketChannel channel, SelectionKey key, ServerResourceProvider provider, int index) {
		this.id = id;
		this.channel = channel;
		this.key = key;
		this.provider = provider;
		this.index = index;
		this.name = null;
		this.logic = null;

		this.logic.onInit();
	}

	public void initLogic() {
		this.logic = new InitState(this, this.provider);
	}

	public void handleRead() {
		ClientLogic next = this.logic.step();

		if (next == null)
			return;

		this.logic.onFin();
		next.onInit();

		this.logic = next;
	}

	public boolean isReady() {
		return this.name != null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Client client = (Client) o;
		return id == client.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
	}
}
