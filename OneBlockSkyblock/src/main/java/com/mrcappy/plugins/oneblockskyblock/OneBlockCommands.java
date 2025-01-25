package com.mrcappy.plugins.oneblockskyblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class OneBlockCommands implements CommandExecutor {

    private final OneBlockSkyblock plugin;
    private final Set<String> adminResetConfirmations = new HashSet<>();

    public OneBlockCommands(OneBlockSkyblock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("resetoneblock")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }

            Player player = (Player) sender;
            Location resetIsland = plugin.createIsland(player);
            player.teleport(resetIsland.add(0.5, 1, 0.5));
            player.sendMessage("§eYour island has been reset!");
            return true;

        } else if (command.getName().equalsIgnoreCase("adminresetoneblock")) {
            if (!sender.hasPermission("oneblockskyblock.admin")) {
                sender.sendMessage("§cYou do not have permission to use this command.");
                return true;
            }

            if (args.length < 1) {
                sender.sendMessage("§cUsage: /adminresetoneblock <player>");
                return true;
            }

            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);

            if (target == null || !target.isOnline()) {
                sender.sendMessage("§cPlayer not found or not online.");
                return true;
            }

            if (adminResetConfirmations.contains(targetName)) {
                // Confirmed reset
                adminResetConfirmations.remove(targetName);
                Location resetIsland = plugin.createIsland(target);
                target.teleport(resetIsland.add(0.5, 1, 0.5));
                sender.sendMessage("§aSuccessfully reset " + target.getName() + "'s island.");
                target.sendMessage("§cYour One-Block Skyblock island has been reset by an admin!");
            } else {
                // First confirmation step
                adminResetConfirmations.add(targetName);
                sender.sendMessage("§eType the command again to confirm resetting " + target.getName() + "'s island.");
            }
            return true;
        }

        return false;
    }
}
