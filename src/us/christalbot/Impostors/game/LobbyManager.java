package us.christalbot.Impostors.game;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LobbyManager {
    // Singleton instance
    private static LobbyManager lm;

    // Player data
    private final Map<UUID, Location> locs = new HashMap<UUID, Location>();
    private final Map<UUID, ItemStack[]> inv = new HashMap<UUID, ItemStack[]>();
    private final Map<UUID, ItemStack[]> armor = new HashMap<UUID, ItemStack[]>();

    private final List<Lobby> lobbies = new ArrayList<>();
    private int lobbyNum = 0;

    private LobbyManager() {} // Prevent instantiation

    // Singleton accessor with lazy initialization
    public static LobbyManager getManager() {
        if (lm == null) {
            lm = new LobbyManager();
        }
        return lm;
    }

    public Lobby getLobby(Player host){
        for (Lobby l : this.lobbies) {
            if (l.getHost() == host) {
                return l;
            }
        }
        return null; // Not found
    }

    /**
     * Adds the player to an arena
     *
     * <p>Gets the arena by ID, checks that it exists,
     * and check the player isn't already in a game.</p>
     *
     * @param p the player to add
     * @param host the arena ID. A check will be done to ensure its validity.
     */
    public void addPlayer(Player p, Player host) {
        Lobby lobby = this.getLobby(host);
        if (lobby == null) {
            p.sendMessage("Invalid arena!");
            return;
        }

        if (this.isInGame(p)) {
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
        host.sendMessage("Player " + p.getName() + " has joined your lobby!");
        host.sendMessage("You now have " + lobby.getPlayers().size() + " players in your lobby.");
    }

    /**
     * Removes the player from their current arena.
     *
     * <p>The player is allowed to not be in game, a check
     * will be performed to ensure the validity of the arena</p>
     *
     * @param p the player to remove from the arena
     */
    public void removePlayer(Player p) {
        Lobby lobby = null;

        // Searches each arena for the player
        for (Lobby l : this.lobbies) {
            if (l.getPlayers().containsKey(p.getUniqueId()))
                lobby = l;
        }

        // Check arena validity
        if (lobby == null) {
            p.sendMessage("Invalid operation!");
            return;
        }

        lobby.addAvailableID(lobby.getPlayerID(p));

        // Remove from arena player lost
        lobby.getPlayers().remove(p.getUniqueId());

        // Remove inventory acquired during the game
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        // Restore inventory and armor
        p.getInventory().setContents(inv.get(p.getName()));
        p.getInventory().setArmorContents(armor.get(p.getName()));

        // Remove player data entries
        inv.remove(p.getUniqueId());
        armor.remove(p.getUniqueId());

        // Teleport to original location, remove it too
        p.teleport(locs.get(p.getUniqueId()));
        locs.remove(p.getUniqueId());

        p.setGameMode(GameMode.SURVIVAL);

        // Heh, you're safe now :)
        p.setFireTicks(0);
    }

    /**
     * Checks if the player is currently in an arena
     *
     * @param p the player to check
     * @return {@code true} if the player is in game
     */
    public boolean isInGame(Player p) {
        for (Lobby lobby : this.lobbies) {
            if (lobby.getPlayers().containsKey(p.getUniqueId()))
                return true;
        }
        return false;
    }

}