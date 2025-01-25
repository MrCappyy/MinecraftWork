package com.mrcappy.villageplugin.manager;

import com.mrcappy.villageplugin.villager.VillagerRole;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BuffManager {

    public void applyBuff(Player player, VillagerRole role) {
        if (role == null || role.getBuffEffect() == null) return;

        // Remove the existing buff effect if already applied
        player.removePotionEffect(role.getBuffEffect());

        // Apply the new buff effect
        player.addPotionEffect(new PotionEffect(
                role.getBuffEffect(),
                Integer.MAX_VALUE, // Duration: Permanent
                role.getBuffLevel(), // Level of the buff
                false, // Ambient
                false  // Particles
        ));

        player.sendMessage("§aYou have been granted the " + role.getBuffEffect().getName() + " buff!");
    }

    public void removeAllBuffs(Player player) {
        // Remove all potion effects from the player
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }
}
