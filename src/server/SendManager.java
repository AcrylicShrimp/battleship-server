package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendManager
        extends ServerResource {
    private HashMap<Client, ArrayDeque<ByteBuffer>> clientMap;

    public SendManager(ServerResourceProvider provider) {
        super(provider);
        this.clientMap = new HashMap<>();
    }

    public void addPacket(Client client, ByteBuffer buffer) {
        ArrayDeque<ByteBuffer> buffers = this.clientMap.get(client);

        if (buffers != null) {
            buffers.add(buffer.slice());
            return;
        }

        buffers = new ArrayDeque<>();
        buffers.addLast(buffer.slice());
        this.clientMap.put(client, buffers);
    }

    public void removeClient(Client client) {
        this.clientMap.remove(client);
    }

    public void push() {
        ArrayList<Client> clients = new ArrayList<>();

        for (Map.Entry<Client, ArrayDeque<ByteBuffer>> entry : this.clientMap.entrySet())
            try {
                ArrayDeque<ByteBuffer> buffers = entry.getValue();

                for (; !buffers.isEmpty(); ) {
                    ByteBuffer buffer = buffers.getFirst();
                    entry.getKey().channel.write(buffer);

                    if (buffer.hasRemaining())
                        break;

                    buffers.removeFirst();
                }

                if (buffers.isEmpty())
                    clients.add(entry.getKey());
            } catch (IOException e) {
                this.provider.clientManager().removeClient(entry.getKey().id);
            }

        for (Client client : clients)
            this.clientMap.remove(client);
    }
}
