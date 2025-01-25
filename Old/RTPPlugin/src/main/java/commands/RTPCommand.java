package com.mrcappy.safertp.commands;

import com.mrcappy.safertp.gui.RTPGui;
import com.mrcappy.safertp.SafeRTP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTPCommand implements CommandExecutor {

    private final SafeRTP plugin;

    public RTPCommand(SafeRTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        new RTPGui(plugin, player).open();
        return true;
    }
}
