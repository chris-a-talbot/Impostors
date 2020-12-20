package us.christalbot.Impostors.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import us.christalbot.Impostors.Impostors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MapManager {
    private final Plugin plugin = Impostors.getPlugin(Impostors.class);
    private final FileConfiguration config = plugin.getConfig();

    // Singleton instance
    private static MapManager mm;

    // Prevent instantiation
    private MapManager() {}

    // Map data
    private final List<GameMap> maps = new ArrayList<>();
    private int mapNum = 0;

    // Retrieve an instance of MapManager
    public static MapManager getManager() {
        if (mm == null) { mm = new MapManager(); }
        return mm;
    }

    // Get the full list of loaded maps
    public List<GameMap> getMaps() { return maps; }

    // Get the number of loaded maps
    public int getMapNum() { return mapNum; }

    /*
        If a map with the given name is loaded, returns that game as an object.
        If no such map exists, returns null.
    */
    public GameMap getMapByName(String name) {
        for (GameMap a : this.maps) {
            if (a.getName().equals(name)) return a;
        }

        return null;
    }

    // Returns true if the given map is currently being used by a lobby
    public boolean isInUse(GameMap map) {
        for(Lobby lobby : LobbyManager.getManager().getLobbies()) {
            if (lobby.getMap() == map) return true;
        }

        return false;
    }

    /*
        Load a map by name. Requires that a map with the given name exists in the config.
        Adds the given map to the maps list and increments the map number by one.
     */
    public void loadMap(String name) {
        String creatorName = config.getString("maps." + name + ".creator");
        String worldName = config.getString("maps." + name + ".world");
        World world;
        List<Location> spawnLocations = new ArrayList<>();

        if (worldName == null) { world = null; }
        else { world = Bukkit.getWorld(worldName); }

        for(int i = 0; i < 10; ++i) {
            String spawnPath = "maps." + name + ".spawn." + i;
            double x = config.getDouble(spawnPath + ".x");
            double y = config.getDouble(spawnPath + ".y");
            double z = config.getDouble(spawnPath + ".z");
            float pitch = (float) config.getDouble(spawnPath + ".pitch");
            float yaw = (float) config.getDouble(spawnPath + ".yaw");
            Location loc = new Location(world, x, y, z, yaw, pitch);
            spawnLocations.add(loc);
        }

        maps.add(new GameMap(name, creatorName, world, spawnLocations));
        ++mapNum;
    }

    // Loops through every map in the config and loads it using loadMap().
    public void reloadAll() {
        mapNum = 0;
        maps.clear();

        if(config.getConfigurationSection("maps") != null) {
            Set<String> mapList =
                    Objects.requireNonNull(config.getConfigurationSection("maps")).getKeys(false);

            for(String name : mapList) {
                loadMap(name);
            }
        }
    }

    // Saves a given map to config. Assumes the given map already exists in config.
    public void saveMap(GameMap map) {
        String name = map.getName();
        config.set("maps." + name + ".name", name);
        config.set("maps." + name + ".creator", map.getCreator());
        config.set("maps." + name + ".world", map.getWorld());

        for(int i = 0; i < 10; ++i) {
            config.set("maps." + name + ".spawn." + i, map.getSpawnLocation(i));
        }

        plugin.saveConfig();
    }

    // Saves every map in the config using saveMap().
    public void saveAll() { for(GameMap m : maps) saveMap(m); }

    // Creates a brand new map with mostly null data
    public boolean createNewMap(String name, String creator, String world) {
        if(!config.contains("mapnum")) {
            config.createSection("mapnum");
        }

        if(!config.contains("maps." + name)) {
            config.createSection("maps." + name);
            config.createSection("maps." + name + ".name");
            config.createSection("maps." + name + ".creator");
            config.createSection("maps." + name + ".world");

            for(int i = 0; i < 10; ++i) {
                config.createSection("maps." + name + ".spawn." + i);
            }

            config.set("maps." + name + ".name", name);
            config.set("maps." + name + ".creator", creator);
            config.set("maps." + name + ".world", world);
            World w = Bukkit.getWorld(world);

            for(int i = 0; i < 10; ++i) {
                config.set("maps." + name + ".spawn." + i, new Location(w, 0, 0, 0, 0, 0));
            }

            config.set("mapnum", config.getInt("mapnum") + 1);
            plugin.saveConfig();
            loadMap(name);
            return true;
        } else return false;
    }

    // Pushes a map to config from a given GameMap
    public void pushMap(GameMap map) {
        if(!config.contains("mapnum")) {
            config.createSection("mapnum");
        }

        if(!config.contains("maps." + map.getName())) {
            config.createSection("maps." + map.getName());
            config.createSection("maps." + map.getName() + ".name");
            config.createSection("maps." + map.getName() + ".creator");
            config.createSection("maps." + map.getName() + ".world");

            for(int i = 0; i < 10; ++i) {
                config.createSection("maps." + map.getName() + ".spawn." + i);
            }

            config.set("maps." + map.getName() + ".name", map.getName());
            config.set("maps." + map.getName() + ".creator", map.getCreator());
            config.set("maps." + map.getName() + ".world", map.getWorld());

            for(int i = 0; i < 10; ++i) {
                config.set("maps." + map.getName() + ".spawn." + i,
                        map.getSpawnLocation(i));
            }

            config.set("mapnum", config.getInt("mapnum") + 1);
            plugin.saveConfig();
            loadMap(map.getName());
        }
    }

    // Deletes a map from config & reloads the maps list
    public boolean deleteMap(GameMap map) {
        String name = map.getName();

        if(config.contains("maps." + name)) {
            config.set("maps." + name, null);
            config.set("mapnum", config.getInt("mapnum") - 1);

            plugin.saveConfig();
            --mapNum;
            reloadAll();
            return true;
        } else return false;
    }

}
