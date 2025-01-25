package com.mrcappy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class customdyerecipes extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Custom Dye Recipes Plugin Enabled!");
        try {
            registerRecipes();
        } catch (Exception e) {
            getLogger().severe("An error occurred while registering recipes: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Custom Dye Recipes Plugin Disabled!");
    }

    private void registerRecipes() {
        addRecipe("white_dye", Material.REDSTONE, Material.QUARTZ, Material.WHITE_DYE);
        addRecipe("orange_dye", Material.REDSTONE, Material.CARVED_PUMPKIN, Material.ORANGE_DYE);
        addRecipe("magenta_dye", Material.REDSTONE, Material.STRIPPED_CRIMSON_STEM, Material.MAGENTA_DYE);
        addRecipe("light_blue_dye", Material.REDSTONE, Material.STRIPPED_WARPED_STEM, Material.LIGHT_BLUE_DYE);
        addRecipe("yellow_dye", Material.REDSTONE, Material.HAY_BLOCK, Material.YELLOW_DYE);
        addRecipe("lime_dye", Material.REDSTONE, Material.WHEAT_SEEDS, Material.LIME_DYE);
        addRecipe("pink_dye", Material.REDSTONE, Material.CRIMSON_STEM, Material.QUARTZ, Material.PINK_DYE);
        addRecipe("gray_dye", Material.REDSTONE, Material.GRAVEL, Material.GRAY_DYE);
        addRecipe("light_gray_dye", Material.REDSTONE, Material.GRAVEL, Material.QUARTZ, Material.LIGHT_GRAY_DYE);
        addRecipe("cyan_dye", Material.REDSTONE, Material.TWISTING_VINES, Material.CYAN_DYE);
        addRecipe("purple_dye", Material.REDSTONE, Material.LAPIS_LAZULI, Material.PURPLE_DYE);
        addRecipe("blue_dye", Material.REDSTONE, Material.WARPED_FUNGUS, Material.BLUE_DYE);
        addRecipe("brown_dye", Material.REDSTONE, Material.SOUL_SAND, Material.BROWN_DYE);
        addRecipe("green_dye", Material.REDSTONE, Material.KELP, Material.GREEN_DYE);
        addRecipe("red_dye", Material.REDSTONE, Material.NETHER_WART, Material.RED_DYE);
        addRecipe("black_dye", Material.REDSTONE, Material.COAL, Material.BLACK_DYE);
    }

    private void addRecipe(String key, Material material1, Material material2, Material result) {
        try {
            ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(this, key), new ItemStack(result));
            recipe.addIngredient(material1);
            recipe.addIngredient(material2);
            Bukkit.addRecipe(recipe);
        } catch (IllegalArgumentException e) {
            getLogger().severe("Failed to register recipe for key: " + key + ". Error: " + e.getMessage());
        }
    }

    private void addRecipe(String key, Material material1, Material material2, Material material3, Material result) {
        try {
            ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(this, key), new ItemStack(result));
            recipe.addIngredient(material1);
            recipe.addIngredient(material2);
            recipe.addIngredient(material3);
            Bukkit.addRecipe(recipe);
        } catch (IllegalArgumentException e) {
            getLogger().severe("Failed to register recipe for key: " + key + ". Error: " + e.getMessage());
        }
    }
}
