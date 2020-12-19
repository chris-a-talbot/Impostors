package us.christalbot.Impostors.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.christalbot.Impostors.Impostors;
import us.christalbot.Impostors.game.GameMap;
import us.christalbot.Impostors.game.MapManager;

import java.util.List;

public class CommandMaps implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(s.equalsIgnoreCase("maps")) {
            Player player;
            if(commandSender instanceof Player) {
                player = (Player) commandSender;
            } else {
                commandSender.sendMessage(Impostors.prefix + "Maps must be managed from in-game.");
                return true;
            }

            MapManager mm = MapManager.getManager();

            if(strings.length == 0 || strings[0].equalsIgnoreCase("list")) {
                List<GameMap> maps = MapManager.getManager().getMaps();
                StringBuilder list = new StringBuilder();
                for(int i = 0; i < maps.size(); ++i) {
                    if(i == maps.size() - 1) {
                        list.append(maps.get(i).getName());
                    } else {
                        list.append(maps.get(i).getName()).append(", ");
                    }
                }
                player.sendMessage(list.toString());
            } else if(strings[0].equalsIgnoreCase("save")) {
                mm.saveAll();
                player.sendMessage(Impostors.prefix + "Saved all loaded maps to config!");
            } else if(strings[0].equalsIgnoreCase("reload")) {
                mm.saveAll();
                mm.reloadAll();
                player.sendMessage(Impostors.prefix + "Reloaded all maps from config!");
            } else {
                player.sendMessage(Impostors.prefix + "Invalid command! Usage: /maps [list / save / reload]");
            }
        }
        return true;
    }
}
