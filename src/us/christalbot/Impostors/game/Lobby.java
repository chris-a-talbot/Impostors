package us.christalbot.Impostors.game;

import org.bukkit.entity.Player;

import java.util.*;

public class Lobby {
    private final LobbyManager lm = LobbyManager.getManager();

    private final int lobbyID;

    private final GameMap map;
    private GameSettings settings;

    Player host;
    int impostorNumber;

    private Game game;
    private boolean isInGame;

    private final LinkedHashMap<Player, Integer> players = new LinkedHashMap<>();
    private final List<Integer> available_indices = new ArrayList<>();

    public Lobby(GameMap mapName, Player host, int impostor_number) {
        this.lobbyID = lm.getNextAvailableLobbyID();
        this.host = host;
        this.settings = new GameSettings();
        this.map = mapName;
        this.impostorNumber = impostor_number;
        for(int i = 0; i < 10; ++i) { available_indices.add(i); }
        this.isInGame = false;
    }

    public int getID() { return lobbyID; }

    public GameMap getMap() { return map; }

    public String getMapName() { return map.getName(); }

    public GameSettings getSettings() { return settings; }

    public Player getHost() { return host; }

    public void setHost(Player host) { this.host = host; }

    public int getImpostorNumber() { return impostorNumber; }

    public void setImpostorNumber(int impostorNumber) { this.impostorNumber = impostorNumber; }

    public boolean isInGame() { return isInGame; }

    public Map<Player, Integer> getPlayers() { return players; }

    public int nextAvailablePlayerID() {
        int index = available_indices.get(0);
        available_indices.remove(0);
        return index;
    }

    public int getPlayerID(Player p) { return players.get(p); }

    public void addAvailableID(int id) { available_indices.add(id); }

    public boolean startGame() {
        if(players.size() >= 4) {
            game = new Game(this);
            game.begin();
            isInGame = true;
            return true;
        } else return false;
    }

    public void endGame() {
        game.end();
        game = null;
        isInGame = false;
    }

}
