package server.logic;

import server.Client;
import server.Game;
import server.packet.Packet;
import server.packet.PacketBuilder;

import java.util.Arrays;

class Helper {
    public static boolean putShip(boolean[][] board, Packet.GameInit.Ship ship, int size) {
        int x = ship.x;
        int y = ship.y;

        try {
            for (int index = 0; index < size; ++index) {
                if (board[x][y])
                    return false;

                board[x][y] = true;

                if (ship.rotated)
                    ++y;
                else
                    ++x;
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }
}

public class GameLogic extends Logic {
    private final Game game;
    private final boolean isBlue;
    private final Client enemy;

    public GameLogic(Client client, Game.EventArg.Init init) {
        super(client);
        this.game = init.game;
        this.isBlue = init.isBlue;
        this.enemy = init.enemy;
    }

    @Override
    public void handleEvent(String type, Object arg) {
        switch (type) {
            case "game:begin" -> this.client.sendPacket(PacketBuilder.buildGameBegin());
            case "game:turn" -> this.client.sendPacket(PacketBuilder.buildGameTurn(((Game.EventArg.Turn) arg).isMyTurn));
            case "game:fire:friendly" -> {
                Game.EventArg.Fire fire = (Game.EventArg.Fire) arg;
                this.client.sendPacket(PacketBuilder.buildGameFireFriendly(fire.x, fire.y, fire.hit));
            }
            case "game:fire:enemy" -> {
                Game.EventArg.Fire fire = (Game.EventArg.Fire) arg;
                this.client.sendPacket(PacketBuilder.buildGameFireEnemy(fire.x, fire.y, fire.hit));
            }
            case "game:set" -> {
                this.client.sendPacket(PacketBuilder.buildGameSet(((Game.EventArg.GameSet) arg).won));
                this.client.sendPacket(PacketBuilder.buildNotifyLobby(this.client.provider.lobbyManager().clients(),
                                                                      this.client.provider.roomManager().rooms()));
                this.client.provider.lobbyManager().broadcast(PacketBuilder.buildNotifyLobbyEnterLobby(this.client));
                this.client.provider.lobbyManager().addClient(this.client);
                this.client.setLogic(new LobbyLogic(this.client));
                System.out.printf("The client %s has been left the game.\n", this.client);
            }
            case "game:crash" -> {
                this.client.sendPacket(PacketBuilder.buildGameSet(true));
                this.client.sendPacket(PacketBuilder.buildNotifyLobby(this.client.provider.lobbyManager().clients(),
                                                                      this.client.provider.roomManager().rooms()));
                this.client.provider.lobbyManager().broadcast(PacketBuilder.buildNotifyLobbyEnterLobby(this.client));
                this.client.provider.lobbyManager().addClient(this.client);
                this.client.setLogic(new LobbyLogic(this.client));
                System.out.printf("The client %s has been left the game.\n", this.client);
            }
        }
    }

    @Override
    public void handle(Packet.GameInit packet) {
        boolean[][] board = new boolean[10][10];

        for (boolean[] row : board)
            Arrays.fill(row, false);

        if (!Helper.putShip(board, packet.ships[0], 3))
            return;
        if (!Helper.putShip(board, packet.ships[1], 4))
            return;
        if (!Helper.putShip(board, packet.ships[2], 5))
            return;
        if (!Helper.putShip(board, packet.ships[3], 7))
            return;

        if (this.isBlue)
            this.game.initializeBlue(board);
        else
            this.game.initializeRed(board);
    }

    @Override
    public void handle(Packet.GameFire packet) {
        Game.FireResult result = this.isBlue ? this.game.fireBlue(packet.x, packet.y) : this.game.fireRed(packet.x, packet.y);

        if (result != Game.FireResult.Ok)
            this.client.sendPacket(PacketBuilder.buildGameFireRejected());
    }

    @Override
    public void handle(Packet.ChatNormal packet) {
        this.game.broadcast(PacketBuilder.buildBroadcastChatNormal(this.client, packet.message));

        System.out.printf("The client %s has been sent a message in the game: %s\n", this.client, packet.message);
    }

    @Override
    public void handle(Packet.ChatWhisper packet) {
        Client target = this.client.provider.clientManager().getClientById(packet.clientId);

        if (target == null)
            return;

        target.sendPacket(PacketBuilder.buildBroadcastChatWhisper(this.client, packet.message));

        System.out.printf("The client %s has been sent a message to the client %s: %s\n", this.client, target, packet.message);
    }
}
