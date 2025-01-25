package com.mrcappy.villageplugin;

import com.mrcappy.villageplugin.commands.ReloadCommand;
import com.mrcappy.villageplugin.commands.SetVillagerCommand;
import com.mrcappy.villageplugin.commands.TradeCommand;
import com.mrcappy.villageplugin.config.ConfigManager;
import com.mrcappy.villageplugin.manager.BuffManager;
import com.mrcappy.villageplugin.manager.GUIManager;
import com.mrcappy.villageplugin.manager.VillagerManager;
import com.mrcappy.villageplugin.storage.RoleStorage;
import org.bukkit.plugin.java.JavaPlugin;

public class VillagePlugin extends JavaPlugin {

    private VillagerManager villagerManager;
    private BuffManager buffManager;
    private GUIManager guiManager;
    private RoleStorage roleStorage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ConfigManager.loadConfig(this);

        roleStorage = new RoleStorage(this);
        villagerManager = new VillagerManager(roleStorage);
        buffManager = new BuffManager();
        guiManager = new GUIManager(villagerManager);

        // Register commands
        getCommand("setvillager").setExecutor(new SetVillagerCommand(villagerManager, buffManager));
        getCommand("trade").setExecutor(new TradeCommand(guiManager));
        getCommand("villagerplugin").setExecutor(new ReloadCommand(this, roleStorage));

        getLogger().info("VillagePlugin has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        roleStorage.saveData();
        getLogger().info("VillagePlugin has been disabled!");
    }
}
