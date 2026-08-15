package com.rarecandy.rarecandyapi.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ColorUtils {

    public static Component format(String text, ServerPlayer player) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        String parsedText = text;

        if (player != null) {
            Player bukkitPlayer = Bukkit.getPlayer(player.getUUID());
            if (bukkitPlayer != null) {
                parsedText = PlaceholderAPI.setPlaceholders(bukkitPlayer, text);
            }
        }

        return Component.literal(parsedText.replace("&", "§"));
    }
}