package server.packet;

import server.Client;
import server.Room;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface PacketBuilder {
    private static IntegerPacketBuilder pack(int value) {
        return new IntegerPacketBuilder(value);
    }

    private static StringPacketBuilder pack(String value) {
        return new StringPacketBuilder(value);
    }

    private static ClientPacketBuilder pack(Client value) {
        return new ClientPacketBuilder(value);
    }

    private static RoomPacketBuilder pack(Room value) {
        return new RoomPacketBuilder(value);
    }

    private static <T extends PacketBuilder> ListPacketBuilder<T> packBuilders(T[] values) {
        return new ListPacketBuilder<T>(values);
    }

    private static <T extends PacketBuilder> ListPacketBuilder<T> packBuilders(List<T> values) {
        return new ListPacketBuilder<T>(values);
    }

    private static ListPacketBuilder<IntegerPacketBuilder> packIntegers(Collection<Integer> values) {
        return packBuilders(values.stream().map(PacketBuilder::pack).toArray(IntegerPacketBuilder[]::new));
    }

    private static ListPacketBuilder<StringPacketBuilder> packStrings(Collection<String> values) {
        return packBuilders(values.stream().map(PacketBuilder::pack).toArray(StringPacketBuilder[]::new));
    }

    private static ListPacketBuilder<ClientPacketBuilder> packClients(Collection<Client> values) {
        return packBuilders(values.stream().map(PacketBuilder::pack).toArray(ClientPacketBuilder[]::new));
    }

    private static ListPacketBuilder<RoomPacketBuilder> packRooms(Collection<Room> values) {
        return packBuilders(values.stream().map(PacketBuilder::pack).toArray(RoomPacketBuilder[]::new));
    }

    private static ByteBuffer build(int code) {
        return ByteBuffer.allocate(Integer.BYTES * 2).putInt(code).putInt(0).flip();
    }

    private static ByteBuffer build(int code, PacketBuilder builder) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * 2 + builder.size()).putInt(code).putInt(builder.size());
        builder.put(buffer);
        return buffer.flip();
    }

    private static ByteBuffer build(int code, PacketBuilder... builders) {
        int size = Arrays.stream(builders).map(builder -> builder.size()).reduce(0, Integer::sum);
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * 2 + size).putInt(code).putInt(size);

        for (PacketBuilder builder : builders)
            builder.put(buffer);

        return buffer.flip();
    }

    static ByteBuffer buildServerHello(Client client) {
        return build(00001, pack(client));
    }

    static ByteBuffer buildNotifyLobbyEnterLobby(Client client) {
        return build(10001, pack(client));
    }

    static ByteBuffer buildNotifyLobbyLeaveLobby(Client client) {
        return build(10002, pack(client.id));
    }

    static ByteBuffer buildNotifyLobbyRoomCreated(Room room) {
        return build(10003, pack(room));
    }

    static ByteBuffer buildNotifyLobbyRoomRenamed(Room room) {
        return build(10004, pack(room));
    }

    static ByteBuffer buildNotifyLobbyRoomRemoved(Room room) {
        return build(10005, pack(room.id));
    }

    static ByteBuffer buildNotifyRoomEnterRoom(Client client) {
        return build(10101, pack(client));
    }

    static ByteBuffer buildNotifyRoomLeaveRoom(Client client) {
        return build(10102, pack(client.id));
    }

    static ByteBuffer buildNotifyRoomRoomRenamed(Room room) {
        return build(10103, pack(room.name));
    }

    static ByteBuffer buildNotifyLobby(Collection<Client> clients, Collection<Room> rooms) {
        return build(10201, packClients(clients), packRooms(rooms));
    }

    static ByteBuffer buildNotifyRoom(Room room, Collection<Client> clients) {
        return build(10202, pack(room), packClients(clients));
    }

    static ByteBuffer buildBroadcastChatNormal(Client client, String message) {
        return build(30001, pack(client), pack(message));
    }

    static ByteBuffer buildBroadcastChatWhisper(Client client, String message) {
        return build(30002, pack(client), pack(message));
    }

    static ByteBuffer buildRejectEnterRoomNotFound() {
        return build(70001);
    }

    static ByteBuffer buildRejectLeaveRoomNotInRoom() {
        return build(70002);
    }

    int size();

    void put(ByteBuffer buffer);
}
