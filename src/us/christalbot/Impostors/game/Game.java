package us.christalbot.Impostors.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Game {
    private final MapManager mm = MapManager.getManager();
    private final LobbyManager lm = LobbyManager.getManager();

    private final Lobby lobby;
    private Map<Player, Location> previousLocations = new LinkedHashMap<>();
    private Map<Player, Boolean> alive = new LinkedHashMap<>();
    private Map<Player, Boolean> dead = new LinkedHashMap<>();

    public Game(Lobby lobby) {
        this.lobby = lobby;
        int impostorNumber = lobby.getImpostorNumber();
        int playerNumber = lobby.getPlayers().size();
        while(playerNumber - impostorNumber <= impostorNumber) {
            --impostorNumber;
        }

        List<Integer> impostorIndices = new ArrayList<>();
        for(int i = 0; i < impostorNumber; ++i) {
            Random random = new Random();
            impostorIndices.add(random.ints(0, playerNumber-1).findFirst().getAsInt());
        }

        for(Player p : lobby.getPlayers().keySet()) {
            if(impostorIndices.contains(lobby.getPlayers().get(p))) {
                alive.put(p, true);
            } else { alive.put(p, false); }
        }

    }

    public Lobby getLobby() { return lobby; }

    public Map<Player, Boolean> getAlivePlayers() { return alive; }

    public Map<Player, Boolean> getDeadPlayers() { return dead; }

    // TODO return a list of crewmates
    public List<Player> getCrewmates() { return null; }

    // TODO return a list of impostors
    public List<Player> getImpostors() { return null; }

    // TODO inform players of their impostor/crewmate status
    public void begin() {
        for(Map.Entry<Player, Integer> entry : lobby.getPlayers().entrySet()) {
            entry.getKey().teleport(lobby.getMap().getSpawnLocation(entry.getValue()));
        }
    }

    // TODO end the game
    public void end() {
        for(Map.Entry<Player, Location> entry : previousLocations.entrySet()) {
            entry.getKey().teleport(entry.getValue());
        }
    }

    // TODO move the player p from alive to dead & update their gamemode and such
    public void kill(Player p) { }

}
