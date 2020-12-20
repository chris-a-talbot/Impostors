package us.christalbot.Impostors.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.christalbot.Impostors.Impostors;
import us.christalbot.Impostors.game.Lobby;
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

            } else if(strings[0].equalsIgnoreCase("leave")) {

            }
        }
        return true;
    }
}
