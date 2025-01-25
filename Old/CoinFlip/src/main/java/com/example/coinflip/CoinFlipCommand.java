package com.example.coinflip;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinFlipCommand implements CommandExecutor {
    private final CoinFlip plugin;

    public CoinFlipCommand(CoinFlip plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Usage: /coinflip <player> <amount>");
            return true;
        }

        Player player1 = (Player) sender;
        Player player2 = Bukkit.getPlayer(args[0]);

        if (player2 == null || !player2.isOnline()) {
            player1.sendMessage(Utils.colorize("&cPlayer not found."));
            return true;
        }

        try {
            double betAmount = Double.parseDouble(args[1]);

            if (betAmount <= 0) {
                player1.sendMessage(Utils.colorize("&cThe amount must be greater than 0."));
                return true;
            }

            CoinFlipGUI.openCoinFlipGUI(player1, player2, betAmount);
        } catch (NumberFormatException e) {
            player1.sendMessage(Utils.colorize("&cInvalid amount. Please enter a number."));
        }
        return true;
    }
}
