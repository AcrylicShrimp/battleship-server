package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server
        implements ServerResourceProvider {
    private Selector selector;
    private SendManager sendManager;
    private ClientManager clientManager;
    private LobbyManager lobbyManager;
    private RoomManager roomManager;

    public void run(int port) {
        try {
            this.selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            this.sendManager = new SendManager(this);
            this.clientManager = new ClientManager(this);
            this.lobbyManager = new LobbyManager(this);
            this.roomManager = new RoomManager(this);

            for (; ; ) {
                if (this.selector.selectNow() != 0) {
                    Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        if (key.isAcceptable())
                            this.handleAccept(serverSocketChannel);
                        else if (key.isReadable())
                            this.handleRead(this.clientManager.getClientByKey(key));

                        iterator.remove();
                    }
                }

                this.sendManager.push();
                Thread.yield();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAccept(ServerSocketChannel channel) throws IOException {
        SocketChannel clientChannel = channel.accept();
        clientChannel.configureBlocking(false);
        Client client = this.clientManager.addClient(clientChannel);

        System.out.printf("A new client %s has been connected.\n", client);
    }

    private void handleRead(Client client) {
        try {
            client.handleRead();
        } catch (Exception e) {
            this.clientManager.removeClient(client.id);
            System.out.printf("The client %s has been removed due to %s.\n", client, e.getMessage());
        }
    }

    @Override
    public Selector selector() {
        return this.selector;
    }

    @Override
    public SendManager sendManager() {
        return this.sendManager;
    }

    @Override
    public ClientManager clientManager() {
        return this.clientManager;
    }

    @Override
    public LobbyManager lobbyManager() {
        return this.lobbyManager;
    }

    @Override
    public RoomManager roomManager() {
        return this.roomManager;
    }
}
