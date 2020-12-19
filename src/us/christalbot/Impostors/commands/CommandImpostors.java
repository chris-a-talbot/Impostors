package us.christalbot.Impostors.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandImpostors implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player;
        if(commandSender instanceof Player) {
            player = (Player) commandSender;
        } else {
            commandSender.sendMessage("Maps must be managed from in-game.");
            return true;
        }

        return true;
    }
}
