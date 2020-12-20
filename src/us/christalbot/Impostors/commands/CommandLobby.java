package us.christalbot.Impostors.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.christalbot.Impostors.Impostors;
import us.christalbot.Impostors.game.LobbyManager;
import us.christalbot.Impostors.game.MapManager;

public class CommandLobby implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        MapManager mm = MapManager.getManager();
        LobbyManager lm = LobbyManager.getManager();

        if(s.equalsIgnoreCase("lobby")) {
            Player player;
            if(commandSender instanceof Player) {
                player = (Player) commandSender;
            } else {
                commandSender.sendMessage(Impostors.prefix + "Lobbies must be managed from in-game.");
                return true;
            }

            if(strings[0].equalsIgnoreCase("create")) {
                if(strings.length == 3) {
                    lm.createLobby(player, strings[1], Integer.parseInt(strings[2]));
                    player.sendMessage("Created lobby and added you to it!");
                }
            } else if(strings[0].equalsIgnoreCase("leave")) {
                lm.removePlayer(player);
                player.sendMessage("Removed you from lobby!");
            }
        }

        return true;
    }
}
