package server;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Game implements BroadcastGroup {
    public final ServerResourceProvider provider;
    public final Client blue;
    public final Client red;
    public boolean[][] bluePlacement;
    public boolean[][] blueHit;
    public int blueHP;
    public boolean[][] redPlacement;
    public boolean[][] redHit;
    public int redHP;
    public Phase phase;
    public boolean isBlueTurn;

    public Game(ServerResourceProvider provider, Client client1, Client client2) {
        this.provider = provider;
        if (client1.id < client2.id) {
            this.blue = client1;
            this.red = client2;
        } else {
            this.blue = client2;
            this.red = client1;
        }

        this.phase = Phase.Setting;
        this.blue.sendEvent("game:init", new EventArg.Init(this, true, this.red));
        this.red.sendEvent("game:init", new EventArg.Init(this, false, this.blue));
    }

    public void initializeBlue(boolean[][] placement) {
        if (this.phase != Phase.Setting)
            return;

        if (this.bluePlacement != null)
            return;

        this.bluePlacement = placement;
        this.blueHit = new boolean[10][10];

        for (boolean[] row : this.blueHit)
            Arrays.fill(row, false);

        this.blueHP = 7 + 5 + 4 + 3;

        if (this.redPlacement != null)
            this.begin();
    }

    public void initializeRed(boolean[][] placement) {
        if (this.phase != Phase.Setting)
            return;

        if (this.redPlacement != null)
            return;

        this.redPlacement = placement;
        this.redHit = new boolean[10][10];

        for (boolean[] row : this.redHit)
            Arrays.fill(row, false);

        this.redHP = 7 + 5 + 4 + 3;

        if (this.bluePlacement != null)
            this.begin();
    }

    public FireResult fireBlue(int x, int y) {
        if (this.phase != Phase.Fighting)
            return FireResult.Invalid;

        if (!this.isBlueTurn)
            return FireResult.Invalid;

        if (this.redHit[x][y])
            return FireResult.AlreadyFired;

        this.redHit[x][y] = true;
        this.blue.sendEvent("game:fire:friendly", new EventArg.Fire(x, y, this.redPlacement[x][y]));
        this.red.sendEvent("game:fire:enemy", new EventArg.Fire(x, y, this.redPlacement[x][y]));

        if (this.redPlacement[x][y])
            --this.redHP;

        if (this.redHP == 0) {
            this.phase = Phase.Done;
            this.blue.sendEvent("game:set", new EventArg.GameSet(true));
            this.red.sendEvent("game:set", new EventArg.GameSet(false));
            this.provider.gameManager().removeGame(this);
        } else {
            this.isBlueTurn = false;
            this.blue.sendEvent("game:turn", new EventArg.Turn(false));
            this.red.sendEvent("game:turn", new EventArg.Turn(true));
        }

        return FireResult.Ok;
    }

    public FireResult fireRed(int x, int y) {
        if (this.phase != Phase.Fighting)
            return FireResult.Invalid;

        if (this.isBlueTurn)
            return FireResult.Invalid;

        if (this.blueHit[x][y])
            return FireResult.AlreadyFired;

        this.blueHit[x][y] = true;
        this.blue.sendEvent("game:fire:enemy", new EventArg.Fire(x, y, this.bluePlacement[x][y]));
        this.red.sendEvent("game:fire:friendly", new EventArg.Fire(x, y, this.bluePlacement[x][y]));

        if (this.bluePlacement[x][y])
            --this.blueHP;

        if (this.blueHP == 0) {
            this.phase = Phase.Done;
            this.blue.sendEvent("game:set", new EventArg.GameSet(false));
            this.red.sendEvent("game:set", new EventArg.GameSet(true));
            this.provider.gameManager().removeGame(this);
        } else {
            this.isBlueTurn = true;
            this.blue.sendEvent("game:turn", new EventArg.Turn(true));
            this.red.sendEvent("game:turn", new EventArg.Turn(false));
        }

        return FireResult.Ok;
    }

    private void begin() {
        this.phase = Phase.Fighting;
        this.isBlueTurn = true;
        this.blue.sendEvent("game:begin", null);
        this.blue.sendEvent("game:turn", new EventArg.Turn(true));
        this.red.sendEvent("game:begin", null);
        this.red.sendEvent("game:turn", new EventArg.Turn(false));
    }

    public void end(Client client) {
        this.phase = Phase.Done;

        if (client.equals(this.blue))
            this.red.sendEvent("game:crash", null);
        else
            this.blue.sendEvent("game:crash", null);
    }

    @Override
    public void broadcast(ByteBuffer buffer) {
        this.blue.sendPacket(buffer);
        this.red.sendPacket(buffer);
    }

    @Override
    public void broadcastExcept(ByteBuffer buffer, Client except) {
        if (!this.blue.equals(except))
            this.blue.sendPacket(buffer);

        if (!this.red.equals(except))
            this.red.sendPacket(buffer);
    }

    public enum Phase {
        Setting,
        Fighting,
        Done
    }

    public enum FireResult {
        Invalid,
        AlreadyFired,
        Ok
    }

    public static class EventArg {
        public static class Init {
            public final Game game;
            public final boolean isBlue;
            public final Client enemy;

            public Init(Game game, boolean isBlue, Client enemy) {
                this.game = game;
                this.isBlue = isBlue;
                this.enemy = enemy;
            }
        }

        public static class Turn {
            public final boolean isMyTurn;

            public Turn(boolean isMyTurn) {
                this.isMyTurn = isMyTurn;
            }
        }

        public static class Fire {
            public final int x;
            public final int y;
            public final boolean hit;

            public Fire(int x, int y, boolean hit) {
                this.x = x;
                this.y = y;
                this.hit = hit;
            }
        }

        public static class GameSet {
            public final boolean won;

            public GameSet(boolean won) {
                this.won = won;
            }
        }
    }
}
