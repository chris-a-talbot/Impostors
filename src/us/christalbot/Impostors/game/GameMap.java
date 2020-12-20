package us.christalbot.Impostors.game;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import us.christalbot.Impostors.Impostors;

import java.util.*;


public class GameMap {
    private final MapManager mm = MapManager.getManager();
    private final Plugin plugin = Impostors.getPlugin(Impostors.class);
    private final FileConfiguration config = plugin.getConfig();

    private String name;
    private String creatorName;
    private World world;
    private List<Location> spawnLocations;

    public GameMap(String name, String creatorName, World world, List<Location> spawnLocations) {
        this.name = name;
        this.creatorName = creatorName;
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
        mm.deleteMap(this);
        m.name = name;
        mm.pushMap(m);
    }

    // Returns the map creator's name
    public String getCreator() {
        return creatorName;
    }

    // Change the creator's name and update the config
    public void setCreator(String creatorName) {
        this.creatorName = creatorName;
        config.set("maps." + name + ".creator", creatorName);
        mm.saveMap(this);
    }

    // Returns the world of the map
    public World getWorld() { return world; }

    // Change the map world and update the config
    public void setWorld(World world) {
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