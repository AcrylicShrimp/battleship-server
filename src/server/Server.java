package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class Server implements ServerResourceProvider {
	private Selector      selector;
	private ClientManager clientManager;
	private LobbyManager  lobbyManager;

	public void run(int port) {
		try {
			this.selector = Selector.open();
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(port));
			serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
			this.clientManager = new ClientManager(this);
			this.lobbyManager = new LobbyManager(this);

			for (; ; ) {
				this.selector.select();
				Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();

					if (key.isAcceptable())
						this.handleAccept(key);
					else if (key.isReadable())
						this.handleRead(this.clientManager.getClientByKey(key));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleAccept(SelectionKey key) throws IOException {
		this.clientManager.addClient(((ServerSocketChannel) key.channel()).accept());
	}

	private void handleRead(Client client) {
		try {
			client.handleRead();
		} catch (Exception e) {
			this.clientManager.removeClient(client.id);
			System.out.printf("The client %s has been removed due to %s.", client, e.getMessage());
		}
	}

	@Override
	public Selector selector() {
		return this.selector;
	}

	@Override
	public ClientManager clientManager() {
		return this.clientManager;
	}

	@Override
	public LobbyManager lobbyManager() {
		return this.lobbyManager;
	}
}
