package server;

import server.logic.ClientHelloLogic;
import server.logic.Logic;
import server.packet.PacketBodyReader;
import server.packet.PacketHandler;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Client {
    public final int id;
    public final SocketChannel channel;
    public final SelectionKey key;
    public final ServerResourceProvider provider;
    private final PacketBodyReader reader;
    public int index;
    public String name;
    private Logic logic;

    public Client(int id, SocketChannel channel, SelectionKey key, ServerResourceProvider provider, int index) {
        this.id = id;
        this.channel = channel;
        this.key = key;
        this.provider = provider;
        this.index = index;
        this.name = null;
        this.logic = new ClientHelloLogic(this);
        this.reader = new PacketBodyReader();
    }

    public boolean isReady() {
        return this.name != null;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public void sendPacket(ByteBuffer buffer) {
        this.provider.sendManager().addPacket(this, buffer);
    }

    public void handleRead() {
        try {
            PacketBodyReader.Result result = this.reader.read(this.channel);

            if (result != null)
                PacketHandler.dispatch(result.type, result.buffer, this.logic);
        } catch (Exception e) {
            e.printStackTrace();
            this.provider.clientManager().removeClient(this.id);
        }
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
        return id;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
