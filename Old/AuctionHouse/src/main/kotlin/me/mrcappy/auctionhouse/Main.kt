package me.mrcappy.auctionhouse

import me.mrcappy.auctionhouse.commands.AuctionCommands
import me.mrcappy.auctionhouse.listeners.AuctionListener
import me.mrcappy.auctionhouse.managers.AuctionManager
import me.mrcappy.auctionhouse.notifications.NotificationManager
import me.mrcappy.auctionhouse.utils.DatabaseUtils
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
    }

    override fun onEnable() {
        instance = this

        // Save default configuration
        saveDefaultConfig()

        // Initialize components
        DatabaseUtils.connect()
        AuctionManager.initialize()
        NotificationManager.initialize()

        // Register commands
        getCommand("auctionhouse")?.setExecutor(AuctionCommands())

        // Register listeners
        server.pluginManager.registerEvents(AuctionListener(), this)

        logger.info("AuctionHouse plugin has been enabled successfully!")
    }

    override fun onDisable() {
        AuctionManager.shutdown() // Save any unsaved data
        DatabaseUtils.disconnect()
        logger.info("AuctionHouse plugin has been disabled!")
    }
}
