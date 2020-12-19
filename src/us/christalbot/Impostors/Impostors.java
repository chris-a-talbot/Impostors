package us.christalbot.Impostors;

import org.bukkit.plugin.java.JavaPlugin;
import us.christalbot.Impostors.commands.CommandImpostors;
import us.christalbot.Impostors.commands.CommandLobby;
import us.christalbot.Impostors.commands.CommandMap;
import us.christalbot.Impostors.commands.CommandMaps;
import us.christalbot.Impostors.game.MapManager;

public class Impostors extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Loading Among Us...");

        saveDefaultConfig();
        this.getCommand("impostors").setExecutor(new CommandImpostors());
        this.getCommand("map").setExecutor(new CommandMap());
        this.getCommand("maps").setExecutor(new CommandMaps());
        this.getCommand("lobby").setExecutor(new CommandLobby());

        MapManager.getManager().reloadAll();

        getLogger().info("Loaded Among Us!");
    }

    @Override
    public void onDisable() {
        MapManager.getManager().saveAll();
    }

}
