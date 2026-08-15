package com.rarecandy.rarecandyapi;

import com.rarecandy.rarecandyapi.database.DatabaseManager;
import com.rarecandy.rarecandyapi.listener.PixelmonEventInterceptor;
import com.rarecandy.rarecandyapi.registry.RareCandyExpansion;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RareCandyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        DatabaseManager.init(this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new RareCandyExpansion().register();
        }

        try {
            PixelmonEventInterceptor.register();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        DatabaseManager.close();
    }

    public static String parsePlaceholder(Player player, String placeholder) {
        if (player == null) return "0";
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, placeholder);
        }
        return "0";
    }

    public static String parseRaidPlaceholders(net.minecraft.server.level.ServerPlayer player, String text) {
        if (player == null || text == null || text.isEmpty()) {
            return text;
        }

        net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) player.level();
        com.PixelmonRaid.RaidSaveData.RaidPlayerStats stats = com.PixelmonRaid.RaidSaveData.get(serverLevel).getPlayerStats(player.getUUID());

        if (text.contains("%raidboss_fought%")) text = text.replace("%raidboss_fought%", String.valueOf(stats.raidsJoined));
        if (text.contains("%raidboss_defeated%")) text = text.replace("%raidboss_defeated%", String.valueOf(stats.kills));
        if (text.contains("%raidboss_damage_dealt%")) text = text.replace("%raidboss_damage_dealt%", String.valueOf(stats.totalDamage));
        if (text.contains("%raidboss_damage_taken%")) text = text.replace("%raidboss_damage_taken%", String.valueOf(stats.damageTaken));

        return text;
    }
}