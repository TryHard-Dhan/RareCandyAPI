package com.rarecandy.rarecandyapi.registry;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.rarecandy.rarecandyapi.bridge.GymBridge;
import com.rarecandy.rarecandyapi.data.PlayerData;
import com.rarecandy.rarecandyapi.data.PlayerDataManager;
import com.PixelmonRaid.RaidSaveData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RareCandyExpansion extends PlaceholderExpansion {

    private static final Map<String, String> ASYNC_CACHE = new ConcurrentHashMap<>();

    @Override
    public @NotNull String getIdentifier() { return "rarecandyapi"; }

    @Override
    public @NotNull String getAuthor() { return "CautionLol"; }

    @Override
    public @NotNull String getVersion() { return "1.0.0"; }

    @Override
    public boolean persist() { return true; }

    @Override
    public boolean canRegister() { return true; }

    /**
     * Helper method to fetch async data safely.
     * Returns "Loading..." instantly, and updates the cache in the background when the DB responds.
     */
    private String getOrFetchAsync(UUID uuid, String param, CompletableFuture<?> future) {
        String cacheKey = uuid.toString() + "_" + param;
        if (ASYNC_CACHE.containsKey(cacheKey)) {
            return ASYNC_CACHE.get(cacheKey);
        }

        ASYNC_CACHE.put(cacheKey, "Loading...");
        future.thenAccept(result -> {
            ASYNC_CACHE.put(cacheKey, String.valueOf(result));
        }).exceptionally(ex -> {
            ASYNC_CACHE.put(cacheKey, "Error");
            return null;
        });

        return "Loading...";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        PlayerData data = PlayerDataManager.get(player.getUniqueId());
        ServerPlayer serverPlayer = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(player.getUniqueId());
        String lowerParam = params.toLowerCase();

        if (lowerParam.equals("gymsystem_gyms_beaten_total")) {
            return getOrFetchAsync(player.getUniqueId(), lowerParam, GymBridge.getTotalWinsByCategory(player.getUniqueId(), "gyms"));
        }

        if (lowerParam.equals("gymsystem_e4_beaten_total")) {
            return getOrFetchAsync(player.getUniqueId(), lowerParam, GymBridge.getTotalWinsByCategory(player.getUniqueId(), "e4"));
        }

        if (lowerParam.startsWith("gymsystem_has_defeated_")) {
            String gymId = lowerParam.replace("gymsystem_has_defeated_", "");
            return getOrFetchAsync(player.getUniqueId(), lowerParam, GymBridge.hasBeatenNode(player.getUniqueId(), gymId));
        }

        if (lowerParam.startsWith("gymsystem_bt_lifetime_wins_")) {
            String gymId = lowerParam.replace("gymsystem_bt_lifetime_wins_", "");
            return getOrFetchAsync(player.getUniqueId(), lowerParam, GymBridge.getLifetimeWins(player.getUniqueId(), gymId));
        }

        if (lowerParam.startsWith("gymsystem_champion_streak_")) {
            String gymId = lowerParam.replace("gymsystem_champion_streak_", "");
            com.gym.gymsystem.data.DatabaseManager.GymLeaderData leader = GymBridge.getChampionData(gymId);
            if (leader != null && leader.playerUUID().equals(player.getUniqueId().toString())) {
                return String.valueOf(leader.defenseStreak());
            }
            return "0";
        }

        if (lowerParam.equals("gymsystem_bt_active_floor")) {
            return GymBridge.getActiveBTFloor(player.getUniqueId());
        }

        if (data == null) {
            if (lowerParam.equalsIgnoreCase("party_lead_name")) return "None";
            return "0";
        }

        switch (lowerParam) {
            case "prestige_level": return String.valueOf(data.getPrestigeLevel());
            case "prestige_tokens": return String.valueOf(data.getPrestigeTokens());
            case "lifetime_catches": return String.valueOf(data.getLifetimeCatches());
            case "lifetime_legendaries": return String.valueOf(data.getLifetimeLegendaries());
            case "total_catches": return String.valueOf(data.getTotalCatches());
            case "shiny_catches": return String.valueOf(data.getShinyCatches());
            case "legendary_catches": return String.valueOf(data.getLegendariesCaught());
            case "mythical_catches": return String.valueOf(data.getMythicalsCaught());
            case "ultrabeast_catches": return String.valueOf(data.getUltraBeastsCaught());
            case "pixelpass_tier": return String.valueOf(data.getLevel());
            case "pixelpass_xp": return String.valueOf(data.getXp());
            case "successful_fishes": return String.valueOf(data.getSuccessfulFishes());
            case "megas_used": return String.valueOf(data.getMegasUsed());
            case "dynamax_used": return String.valueOf(data.getDynamaxUsed());
            case "zmoves_used": return String.valueOf(data.getZMovesUsed());
            case "eggs_obtained": return String.valueOf(data.getEggsObtained());
            case "fossils_restored": return String.valueOf(data.getFossilsRestored());
            case "evolution_stones_used": return String.valueOf(data.getEvolutionStonesUsed());
            case "shrines_activated": return String.valueOf(data.getShrinesActivated());
            case "dex_count": return "0";

            case "raidboss_fought":
                if (serverPlayer != null) return String.valueOf(RaidSaveData.get((ServerLevel) serverPlayer.level()).getPlayerStats(player.getUniqueId()).raidsJoined);
                return "0";
            case "raidboss_defeated":
                if (serverPlayer != null) return String.valueOf(RaidSaveData.get((ServerLevel) serverPlayer.level()).getPlayerStats(player.getUniqueId()).kills);
                return "0";
            case "raidboss_damage_dealt":
                if (serverPlayer != null) return String.valueOf(RaidSaveData.get((ServerLevel) serverPlayer.level()).getPlayerStats(player.getUniqueId()).totalDamage);
                return "0";
            case "raidboss_damage_taken":
                if (serverPlayer != null) return String.valueOf(RaidSaveData.get((ServerLevel) serverPlayer.level()).getPlayerStats(player.getUniqueId()).damageTaken);
                return "0";

            case "party_lead_name":
                if (serverPlayer != null) {
                    PlayerPartyStorage party = StorageProxy.getPartyNow(serverPlayer);
                    Pokemon[] all = party.getAll();
                    if (all.length > 0 && all[0] != null) {
                        return all[0].getSpecies().getName();
                    }
                }
                return "None";
        }

        return null;
    }
}