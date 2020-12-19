package us.christalbot.Impostors.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.christalbot.Impostors.Impostors;
import us.christalbot.Impostors.game.GameMap;
import us.christalbot.Impostors.game.MapManager;

public class CommandMap implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] strings) {
        if(string.equalsIgnoreCase("map")) {
            /*
            First, we check to make sure our commandSender is an in-game player.
            If the sender is not an in-game player, map editing is not currently supported.
            TODO: Implement some (or all) of these commands for console use.
             */
            Player player;
            if(commandSender instanceof Player) {
                player = (Player) commandSender;
            } else {
                commandSender.sendMessage("Maps must be managed from in-game.");
                return true;
            }

            MapManager mm = MapManager.getManager();

            if(strings[0].equalsIgnoreCase("create")) {
                if(strings.length != 2) {
                    player.sendMessage(Impostors.prefix + "Usage: /map create <map_name>");
                } else {
                    player.sendMessage(Impostors.prefix + "Creating map '" + strings[1] + "'...");
                    if(mm.createNewMap(strings[1], player.getName(), player.getWorld().getName())) {
                        player.sendMessage(Impostors.prefix + "Created blank map " + strings[1] + "'!");
                    } else {
                        player.sendMessage(Impostors.prefix + "Map '" + strings[1] + "' already exists.");
                        player.sendMessage(Impostors.prefix + "Try /map delete <map_name> to clear the map!");
                    }
                }
                return true;
            } else if(strings[0].equalsIgnoreCase("delete")) {
                if(strings.length != 2) {
                    player.sendMessage(Impostors.prefix + "Usage: /map delete <map_name>");
                } else {
                    player.sendMessage(Impostors.prefix + "Deleting map '" + strings[1] + "'...");
                    if(mm.deleteMap(strings[1])) {
                        player.sendMessage(Impostors.prefix + "Deleted map '" + strings[1] + "'!");
                    } else {
                        player.sendMessage(Impostors.prefix + "Map '" + strings[1] + "' doesn't exist.");
                        player.sendMessage(Impostors.prefix + "Try /map create <map_name> to create the map!");
                    }
                }
                return true;
            } else if(strings[0].equalsIgnoreCase("setspawn")) {
                if(strings.length != 3) {
                    player.sendMessage(Impostors.prefix + "Usage: /map setspawn <map_name> <spawnpoint_number>");
                } else if(MapManager.getManager().getMap(strings[1]) == null) {
                    player.sendMessage(Impostors.prefix + "Could not find map named '" + strings[1] + "'");
                    player.sendMessage(Impostors.prefix + "Usage: /map setspawn <map_name> <spawnpoint_number>");
                } else if(Integer.parseInt(strings[2]) <= 0 || Integer.parseInt(strings[2]) > 10) {
                    player.sendMessage(Impostors.prefix + "Spawnpoint number must be between 1 and 10!");
                    player.sendMessage(Impostors.prefix + "Usage: /map setspawn <map_name> <spawnpoint _number>");
                } else {
                    GameMap m = mm.getMap(strings[1]);
                    m.setSpawnLocation(Integer.parseInt(strings[2]), player.getLocation());
                    player.sendMessage(Impostors.prefix + "Spawn point #" + strings[2] + " set!");
                }
                return true;
            } else if(strings[0].equalsIgnoreCase("setname")) {
                if(strings.length != 3) {
                    player.sendMessage(Impostors.prefix + "Usage: /map setname <old_name> <new_name>");
                } else {
                    if(mm.getMap(strings[1]) != null) {
                        mm.getMap(strings[1]).setName(strings[2]);
                        player.sendMessage(Impostors.prefix + "Renamed map '" + strings[1]
                                + "' to '" + strings[2] + "'!");
                    } else {
                        player.sendMessage(Impostors.prefix + "Could not find map '" + strings[1] + "'!");
                    }
                }
                return true;
            } else if(strings[0].equalsIgnoreCase("setcreator")) {
                if(strings.length != 3) {
                    player.sendMessage(Impostors.prefix + "Usage: /map setname <map_name> <creator_name>");
                } else {
                    if(mm.getMap(strings[1]) != null) {
                        mm.getMap(strings[1]).setCreator(strings[2]);
                        player.sendMessage(Impostors.prefix + "Changed creator of map '"
                                + strings[1] + "' to '" + strings[2] + "'!");
                    } else {
                        player.sendMessage(Impostors.prefix + "Could not find map '" + strings[1] + "'!");
                    }
                }
                return true;
            } else if(strings[0].equalsIgnoreCase("verify")) {
                if(strings.length != 2) {
                    player.sendMessage(Impostors.prefix + "Usage: /map verify <map_name>");
                } else {
                    if(mm.getMap(strings[1]) != null && mm.getMap(strings[1]).verifySpawnLocations()) {
                        player.sendMessage(Impostors.prefix + "Map '" + strings[1] + "' is valid and playable!");
                    } else if(mm.getMap(strings[1]) == null){
                        player.sendMessage(Impostors.prefix + "Could not find map '" + strings[1] + "'!");
                    } else {
                        player.sendMessage(Impostors.prefix + "Map '" + strings[1] + "' is not currently valid!");
                    }
                }
                return true;
            }
        }
        return false;
    }

}
