package com.example.coinflip;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CoinFlip extends JavaPlugin {
    private static Economy economy;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault not found! Disabling CoinFlip plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("coinflip").setExecutor(new CoinFlipCommand(this));
        getLogger().info("CoinFlip plugin enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CoinFlip plugin disabled!");
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }
}
