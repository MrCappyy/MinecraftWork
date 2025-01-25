package com.example.coinflip;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.Random;

public class CoinFlipManager {
    private final CoinFlip plugin;
    private final Random random = new Random();

    public CoinFlipManager(CoinFlip plugin) {
        this.plugin = plugin;
    }

    public void handleCoinFlip(Player player1, Player player2, double bet) {
        Economy economy = CoinFlip.getEconomy();

        // Check balances
        if (!hasEnough(player1, bet) || !hasEnough(player2, bet)) {
            player1.sendMessage(Utils.colorize("&cOne of the players does not have enough money!"));
            player2.sendMessage(Utils.colorize("&cOne of the players does not have enough money!"));
            return;
        }

        // Deduct money
        economy.withdrawPlayer(player1, bet);
        economy.withdrawPlayer(player2, bet);

        // Randomly determine the winner
        Player winner = random.nextBoolean() ? player1 : player2;
        economy.depositPlayer(winner, bet * 2);

        player1.sendMessage(Utils.colorize("&aThe winner is: " + winner.getName() + "! They won " + (bet * 2) + " coins."));
        player2.sendMessage(Utils.colorize("&aThe winner is: " + winner.getName() + "! They won " + (bet * 2) + " coins."));
    }

    private boolean hasEnough(Player player, double amount) {
        return CoinFlip.getEconomy().has(player, amount);
    }
}
