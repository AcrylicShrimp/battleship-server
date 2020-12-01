package server;

import java.util.HashMap;

public class GameManager extends ServerResource {
    private final HashMap<Client, Game> clientGameMap;

    public GameManager(ServerResourceProvider provider) {
        super(provider);
        this.clientGameMap = new HashMap<>();
    }

    public void addGame(Game game) {
        this.clientGameMap.put(game.blue, game);
        this.clientGameMap.put(game.red, game);
    }

    public void removeGame(Game game) {
        this.clientGameMap.remove(game.blue);
        this.clientGameMap.remove(game.red);
    }

    public void removeClient(Client client) {
        Game game = this.clientGameMap.get(client);

        if (game == null)
            return;

        game.end(client);
        this.clientGameMap.remove(game.blue);
        this.clientGameMap.remove(game.red);
    }
}
