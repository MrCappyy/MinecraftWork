package com.mrcappy.plugins.autoreplant;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentRarity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AutoReplantEnchantment extends Enchantment {

    public AutoReplantEnchantment(@NotNull NamespacedKey key) {
        super(key);
    }

    // Provide a name for the enchantment
    @Override
    public @NotNull String getName() {
        return "Auto Replant";
    }

    // Define the maximum level of the enchantment
    @Override
    public int getMaxLevel() {
        return 1; // Only one level for this enchantment
    }

    // Define the starting level of the enchantment
    @Override
    public int getStartLevel() {
        return 1;
    }

    // Specify the type of items this enchantment can be applied to
    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL; // Applies only to tools
    }

    // Check if the enchantment can be applied to a specific item
    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return item.getType().name().endsWith("_HOE"); // Only applies to hoes
    }

    // Indicate if the enchantment conflicts with other enchantments
    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false; // No conflicts with other enchantments
    }

    // Define whether the enchantment is a treasure enchantment
    @Override
    public boolean isTreasure() {
        return true; // Makes it rare and obtainable via treasure mechanisms
    }

    // Define whether the enchantment is cursed
    @Override
    public boolean isCursed() {
        return false; // Not a cursed enchantment
    }

    // Define whether the enchantment can be traded with villagers
    @Override
    public boolean isTradeable() {
        return true;
    }

    // Define whether the enchantment can be discovered in loot
    @Override
    public boolean isDiscoverable() {
        return true;
    }

    // Define the display name of the enchantment
    @Override
    public @NotNull net.kyori.adventure.text.Component displayName(int level) {
        return net.kyori.adventure.text.Component.text("Auto Replant");
    }

    // Define the translation key for localization purposes
    @Override
    public @NotNull String translationKey() {
        return "enchantment." + getKey().getNamespace() + "." + getKey().getKey();
    }

    // Define the minimum cost for applying this enchantment
    @Override
    public int getMinCost(int level) {
        return 15;
    }

    // Define the maximum cost for applying this enchantment
    @Override
    public int getMaxCost(int level) {
        return 30;
    }

    // Define the minimum modified cost for anvil combinations
    @Override
    public int getMinModifiedCost(int level) {
        return getMinCost(level);
    }

    // Define the maximum modified cost for anvil combinations
    @Override
    public int getMaxModifiedCost(int level) {
        return getMaxCost(level);
    }

    // Define the anvil cost for this enchantment
    @Override
    public int getAnvilCost() {
        return 5; // Cost for combining or using in an anvil
    }

    // Define the rarity of the enchantment
    @Override
    public @NotNull EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE; // Set the enchantment rarity
    }
}
