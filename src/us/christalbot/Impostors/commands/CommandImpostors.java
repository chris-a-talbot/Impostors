package us.christalbot.Impostors.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.christalbot.Impostors.Impostors;

public class CommandImpostors implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(s.equalsIgnoreCase("impostors")) {
            Player player;
            if(commandSender instanceof Player) {
                player = (Player) commandSender;
            } else {
                commandSender.sendMessage(Impostors.prefix + "Impostors plugin must be managed from in-game.");
                return true;
            }

        }

        return true;
    }
}
