package me.mrcappy.auctionhouse.utils

import org.bukkit.Material
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.inventory.ItemStack

object ItemSerializer {
    fun serialize(item: ItemStack): ByteArray {
        val config = item.serializeAsBytes()
        return config
    }

    fun deserialize(bytes: ByteArray): ItemStack {
        return ItemStack.deserializeBytes(bytes)
    }
}
