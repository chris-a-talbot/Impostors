package us.christalbot.Impostors.game;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import us.christalbot.Impostors.Impostors;

import java.util.*;


public class GameMap {
    private final MapManager mm = MapManager.getManager();
    private final Plugin plugin = Impostors.getPlugin(Impostors.class);
    private final FileConfiguration config = plugin.getConfig();

    private String name;
    private String creator;

    String world;
    private List<Location> spawnLocations;
    private transient boolean inGame;

    public GameMap(String name, String creator, String world, List<Location> spawnLocations) {
        this.name = name;
        this.creator = creator;
        this.world = world;
        this.spawnLocations = spawnLocations;
    }

    // Returns the map name
    public String getName() {
        return name;
    }

    // Change the map name and update the config
    public void setName(String name) {
        GameMap m = this;
        mm.deleteMap(this.name);
        m.name = name;
        mm.pushMap(m);
    }

    // Returns the map creator's name
    public String getCreator() {
        return creator;
    }

    // Change the creator's name and update the config
    public void setCreator(String creator) {
        this.creator = creator;
        config.set("maps." + name + ".creator", creator);
        mm.saveMap(this);
    }

    // Returns the world of the map
    public String getWorld() { return world; }

    // Change the map world and update the config
    public void setWorld(String world) {
        this.world = world;
        config.set("maps." + name + ".world", world);
        mm.saveMap(this);
    }

    // Returns spawnpoint #index
    public Location getSpawnLocation(int index) {
        return spawnLocations.get(index);
    }

    // Sets spawnpoint #index to the given location
    public void setSpawnLocation(int index, Location loc) {
        spawnLocations.set(index, loc);
        config.set("maps." + name + ".spawn." + (index-1), loc);
        mm.saveMap(this);
    }

    // Verifies that all 10 spawn locations are valid; returns true if they are
    public boolean verifySpawnLocations() {
        for(int i = 0; i < 10; ++i) {
            Location invalid = new Location(null, 0, 0, 0);
            if(spawnLocations.get(i).toString().equalsIgnoreCase(invalid.toString())) {
                return false;
            }
        }
        return true;
    }

}