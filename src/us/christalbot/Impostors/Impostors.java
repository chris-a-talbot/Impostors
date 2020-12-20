package us.christalbot.Impostors;

import org.bukkit.plugin.java.JavaPlugin;
import us.christalbot.Impostors.commands.CommandImpostors;
import us.christalbot.Impostors.commands.CommandLobby;
import us.christalbot.Impostors.commands.CommandMap;
import us.christalbot.Impostors.commands.CommandMaps;
import us.christalbot.Impostors.game.MapManager;

import java.util.logging.Logger;

public class Impostors extends JavaPlugin {

    public static String prefix = "[Impostors] ";

    @Override
    public void onEnable() {
        MapManager mm = MapManager.getManager();
        Logger log = getLogger();

        log.info("Loading Among Us...");

        saveDefaultConfig();
        this.getCommand("impostors").setExecutor(new CommandImpostors());
        this.getCommand("map").setExecutor(new CommandMap());
        this.getCommand("maps").setExecutor(new CommandMaps());
        this.getCommand("lobby").setExecutor(new CommandLobby());

        mm.reloadAll();

        log.info("Loaded Among Us!");
    }

    @Override
    public void onDisable() {
        MapManager mm = MapManager.getManager();
        mm.saveAll();
    }

}
