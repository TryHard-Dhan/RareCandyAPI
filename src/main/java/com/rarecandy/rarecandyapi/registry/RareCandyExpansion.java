package com.rarecandy.rarecandyapi.registry;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.rarecandy.rarecandyapi.data.PlayerData;
import com.rarecandy.rarecandyapi.data.PlayerDataManager;
import com.PixelmonRaid.RaidSaveData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class RareCandyExpansion extends PlaceholderExpansion {

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

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        PlayerData data = PlayerDataManager.get(player.getUniqueId());
        ServerPlayer serverPlayer = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(player.getUniqueId());

        if (data == null) {
            if (params.equalsIgnoreCase("party_lead_name")) return "None";
            return "0";
        }

        switch (params.toLowerCase()) {
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