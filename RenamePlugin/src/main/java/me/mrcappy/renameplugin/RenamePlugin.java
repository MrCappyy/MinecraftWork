package me.mrcappy.renameplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RenamePlugin extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        this.getCommand("rename").setExecutor(this);
        getLogger().info("RenamePlugin enabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rename.use")) {
            sender.sendMessage(ChatColor.RED + "You don’t have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /rename <player> <newname>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            nameBuilder.append(args[i]).append(" ");
        }
        String newName = ChatColor.translateAlternateColorCodes('&', nameBuilder.toString().trim());

        target.setDisplayName(newName);
        target.setPlayerListName(newName);
        sender.sendMessage(ChatColor.GREEN + "Renamed " + target.getName() + " to " + newName);
        return true;
    }
}
