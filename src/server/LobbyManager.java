package server;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashSet;

public class LobbyManager
        extends ServerResource
        implements BroadcastGroup {
    private final HashSet<Client> clientSet;

    public LobbyManager(ServerResourceProvider provider) {
        super(provider);
        this.clientSet = new HashSet<>();
    }

    public Collection<Client> clients() {
        return this.clientSet;
    }

    public void addClient(Client client) {
        this.clientSet.add(client);
    }

    public boolean removeClient(Client client) {
        return this.clientSet.remove(client);
    }

    @Override
    public void broadcast(ByteBuffer buffer) {
        for (Client client : this.clientSet)
            this.provider.sendManager().addPacket(client, buffer);
    }

    @Override
    public void broadcastExcept(ByteBuffer buffer, Client except) {
        for (Client client : this.clientSet)
            if (!client.equals(except))
                this.provider.sendManager().addPacket(client, buffer);
    }
}
