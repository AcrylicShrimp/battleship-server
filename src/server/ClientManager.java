package server;

import server.packet.PacketBuilder;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ClientManager
        extends ServerResource {
    private final Random random;
    private final ArrayList<Client> clientList;
    private final HashMap<SelectionKey, Client> clientMap;

    public ClientManager(ServerResourceProvider provider) {
        super(provider);
        this.random = new Random(System.currentTimeMillis() + (long) (Math.random() * 1000000));
        this.clientList = new ArrayList<>();
        this.clientMap = new HashMap<>();
    }

    public Client addClient(SocketChannel channel) {
        SelectionKey key;

        try {
            key = channel.register(this.provider.selector(), SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            return null;
        }

        Client client = new Client(this.random.nextInt(), channel, key, this.provider, this.clientList.size());
        this.clientList.add(client);
        this.clientMap.put(key, client);

        return client;
    }

    public Client removeClient(int id) {
        if (this.clientList.isEmpty())
            return null;

        Integer index = this.findClient(id);

        if (index == null)
            return null;

        Client client = this.clientList.get(index);
        Client lastClient = this.clientList.get(this.clientList.size() - 1);
        lastClient.index = index;
        this.clientList.set(index, lastClient);
        this.clientList.remove(this.clientList.size() - 1);
        this.clientMap.remove(client.key);

        this.provider.sendManager().removeClient(client);

        if (this.provider.lobbyManager().removeClient(client))
            this.provider.lobbyManager().broadcast(PacketBuilder.buildNotifyLobbyLeaveLobby(client));

        Room room = this.provider.roomManager().leaveRoom(client);

        if (room != null)
            room.broadcast(PacketBuilder.buildNotifyRoomLeaveRoom(client));

        try {
            client.key.cancel();
            client.channel.close();
        } catch (IOException e) {
        }

        System.out.printf("The client %s has been disconnected - current clients: %d\n", client, this.clientList.size());

        return client;
    }

    private Integer findClient(int id) {
        int index = 0;
        int size = this.clientList.size();
        for (; index < size; ++index)
            if (this.clientList.get(index).id == id)
                break;

        if (index == size)
            return null;

        return index;
    }

    public Client getClientById(int id) {
        Integer index = this.findClient(id);

        if (index == null)
            return null;

        return this.clientList.get(index);
    }

    public Client getClientByKey(SelectionKey key) {
        return this.clientMap.get(key);
    }
}
