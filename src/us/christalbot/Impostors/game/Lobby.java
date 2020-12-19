package us.christalbot.Impostors.game;

import org.bukkit.entity.Player;

import java.util.*;

public class Lobby {

    private GameMap game;
    private GameSettings settings;

    Player host;
    int impostorNumber;

    private final Map<UUID, Integer> players = new HashMap<>();
    private final List<Integer> available_indices = new ArrayList<>();

    public Lobby(String mapname, String host, int impostor_number) {

    }

    public Player getHost() { return host; }

    public void setHost(Player host) { this.host = host; }

    public int getImpostorNumber() {
        return impostorNumber;
    }

    public void setImpostorNumber(int impostorNumber) {
        this.impostorNumber = impostorNumber;
    }

    public Map<UUID, Integer> getPlayers() {
        return players;
    }

    public int nextAvailablePlayerID() {
        int index = available_indices.get(0);
        available_indices.remove(0);
        return index;
    }

    public int getPlayerID(Player p) {
        return players.get(p.getUniqueId());
    }

    public void addAvailableID(int id) {
        available_indices.add(id);
    }
}
