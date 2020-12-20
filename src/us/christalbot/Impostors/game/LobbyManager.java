package us.christalbot.Impostors.game;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import us.christalbot.Impostors.Impostors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LobbyManager {
    private final MapManager mm = MapManager.getManager();
    private final Plugin plugin = Impostors.getPlugin(Impostors.class);
    private final FileConfiguration config = plugin.getConfig();

    // Singleton instance
    private static LobbyManager lm;

    // Prevent instantiation
    private LobbyManager() {}

    // Player data
    private final Map<UUID, Location> locs = new HashMap<UUID, Location>();
    private final Map<UUID, ItemStack[]> inv = new HashMap<UUID, ItemStack[]>();
    private final Map<UUID, ItemStack[]> armor = new HashMap<UUID, ItemStack[]>();

    // Lobby data
    private final List<Lobby> lobbies = new ArrayList<>();
    private int lobbyNum = 0;

    // Retrieve an instance of LobbyManager
    public static LobbyManager getManager() {
        if (lm == null) {
            lm = new LobbyManager();
        }
        return lm;
    }

    // Get the full list of active lobbies
    public List<Lobby> getLobbies() { return lobbies; }

    public int getLobbyNum() { return lobbyNum; }

    public int getNextAvailableLobbyID() {
        int lobbyID = 0;
        for(Lobby lobby : this.lobbies) {
            if(lobby.getID() >= lobbyID) {
                lobbyID = lobby.getID() + 1;
            }
        }
        return lobbyID;
    }

    /*
        If a lobby with the given host is active, returns that lobby as an object.
        If no such lobby exists, returns null.
     */
    public Lobby getLobbyByHost(Player host){
        for(Lobby lobby : this.lobbies) {
            if (lobby.getHost() == host) {
                return lobby;
            }
        }
        return null; // Not found
    }

    /*
        If a lobby with the given ID is active, returns that lobby as an object.
        If no such lobby exists, returns null.
     */
    public Lobby getLobbyByID(int id) {
        for(Lobby lobby : this.lobbies) {
            if(lobby.getID() == id) {
                return lobby;
            }
        }
        return null; // Not found
    }

    // Returns true if a given lobby is actively playing
    public boolean isInGame(Lobby lobby) { return lobby.isInGame(); }

    // Creates a new lobby
    public boolean createLobby(Player host, String mapName, int impostorNumber) {
        for(Lobby lobby : this.lobbies) {
            if(lobby.getMapName().equalsIgnoreCase(mapName)) {
                return false;
            }
        }

        for(GameMap map : mm.getMaps()) {
            if(map.getName().equals(mapName)) {
                if(!mm.isInUse(map)) {
                    Lobby lobby = new Lobby(map, host, impostorNumber);
                    lobbies.add(lobby);
                    addPlayer(host, lobby.getID());
                    return true;
                } else { return false; }
            }
        }

        return false;
    }

    // Deletes the given lobby from the lobbies list
    public void deleteLobby(Lobby lobby) { lobbies.remove(lobby); }

    // Ends and deletes a lobby by host
    public boolean deleteLobbyByHost(Player host) {
        if(getLobbyByHost(host) != null) {
            lobbies.remove(getLobbyByHost(host));
            return true;
         } else { return false; }
    }

    // Ends and deletes a lobby by ID
    public boolean deleteLobbyByID(int lobbyID) {
        if(getLobbyByID(lobbyID) != null) {
            lobbies.remove(getLobbyByID(lobbyID));
            return true;
        } else { return false; }
    }

    // Sets the host of the game to the first entry in the players list
    public void updateHost(Lobby lobby) {
        Map.Entry<UUID, Integer> entry = lobby.getPlayers().entrySet().iterator().next();
        Player newHost = Bukkit.getPlayer(entry.getKey());
        lobby.setHost(newHost);
    }

    /**
     * Checks if the player is currently in an arena
     *
     * @param p the player to check
     * @return {@code true} if the player is in game
     */
    public boolean playerIsInGame(Player p) {
        for (Lobby lobby : this.lobbies) {
            if (lobby.getPlayers().containsKey(p.getUniqueId()))
                return true;
        }
        return false;
    }

    /**
     * Adds the player to an arena
     *
     * <p>Gets the arena by ID, checks that it exists,
     * and check the player isn't already in a game.</p>
     *
     * @param p the player to add
     * @param lobbyID the arena ID. A check will be done to ensure its validity.
     */
    public void addPlayer(Player p, int lobbyID) {
        Lobby lobby = this.getLobbyByID(lobbyID);
        if (lobby == null) {
            p.sendMessage("Invalid arena!");
            return;
        }

        if (this.playerIsInGame(p)) {
            p.sendMessage("Cannot join more than 1 game!");
            return;
        }

        p.setGameMode(GameMode.ADVENTURE);

        // Adds the player to the arena player list
        lobby.getPlayers().put(p.getUniqueId(), lobby.nextAvailablePlayerID());

        // Save the inventory and armor
        inv.put(p.getUniqueId(), p.getInventory().getContents());
        armor.put(p.getUniqueId(), p.getInventory().getArmorContents());

        // Clear inventory and armor
        p.getInventory().setArmorContents(null);
        p.getInventory().clear();

        // Save the players's last location before joining,
        // then teleporting them to the arena spawn
        locs.put(p.getUniqueId(), p.getLocation());
        p.sendMessage("You were added to a lobby! You will be teleported when the game begins.");
        lobby.getHost().sendMessage("Player " + p.getName() + " has joined your lobby!");
        lobby.getHost().sendMessage("You now have " + lobby.getPlayers().size() + " players in your lobby.");
    }

    /**
     * Removes the player from their current arena.
     *
     * <p>The player is allowed to not be in game, a check
     * will be performed to ensure the validity of the arena</p>
     *
     * @param player the player to remove from the arena
     */
    public void removePlayer(Player player) {
        Lobby lobby = null;

        // Searches each arena for the player
        for (Lobby l : this.lobbies) {
            if (l.getPlayers().containsKey(player.getUniqueId()))
                lobby = l;
        }

        // Check arena validity
        if (lobby == null) {
            player.sendMessage("Invalid operation!");
            return;
        }

        lobby.addAvailableID(lobby.getPlayerID(player));

        // Remove from arena player lost
        lobby.getPlayers().remove(player.getUniqueId());

        // If the removed player was the last player, delete the lobby
        // If the removed player was the host but not the last player, reset the host
        if(lobby.getPlayers().size() == 0) { deleteLobby(lobby); }
        else if(lobby.getHost() == player) { updateHost(lobby); }

        // Remove inventory acquired during the game
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        // Restore inventory and armor
        player.getInventory().setContents(inv.get(player.getUniqueId()));
        player.getInventory().setArmorContents(armor.get(player.getUniqueId()));

        // Remove player data entries
        inv.remove(player.getUniqueId());
        armor.remove(player.getUniqueId());

        // Teleport to original location, remove it too
        player.teleport(locs.get(player.getUniqueId()));
        locs.remove(player.getUniqueId());

        player.setGameMode(GameMode.SURVIVAL);

        // Heh, you're safe now :)
        player.setFireTicks(0);
    }

}