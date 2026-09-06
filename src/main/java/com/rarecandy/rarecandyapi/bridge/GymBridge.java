package com.rarecandy.rarecandyapi.bridge;

import com.gym.gymsystem.GymSystem;
import com.gym.gymsystem.data.DatabaseManager.GymLeaderData;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GymBridge {

    /**
     * Asynchronously checks if a player has beaten a specific gym/node.
     */
    public static CompletableFuture<Boolean> hasBeatenNode(UUID uuid, String gymId) {
        if (GymSystem.databaseManager == null) return CompletableFuture.completedFuture(false);
        return GymSystem.databaseManager.hasBeatenAsync(uuid, gymId);
    }

    /**
     * Asynchronously gets lifetime wins for a specific Battle Tower floor or gym.
     */
    public static CompletableFuture<Integer> getLifetimeWins(UUID uuid, String gymId) {
        if (GymSystem.databaseManager == null) return CompletableFuture.completedFuture(0);
        return GymSystem.databaseManager.getPlayerWinsAsync(uuid, gymId);
    }

    /**
     * Asynchronously gets the total unique nodes beaten in a specific category (e.g., "gyms" or "e4").
     */
    public static CompletableFuture<Integer> getTotalWinsByCategory(UUID uuid, String category) {
        if (GymSystem.databaseManager == null) return CompletableFuture.completedFuture(0);
        return GymSystem.databaseManager.getTotalWinsByCategoryAsync(uuid, category);
    }

    /**
     * Synchronously fetches the current reigning champion data for a gym.
     */
    public static GymLeaderData getChampionData(String gymId) {
        if (GymSystem.gymManager == null) return null;
        return GymSystem.gymManager.getChampionData(gymId);
    }

    /**
     * Safely checks the player's persistent NeoForge NBT data for an active Battle Tower run.
     */
    public static String getActiveBTFloor(UUID uuid) {
        ServerPlayer serverPlayer = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        if (serverPlayer != null && serverPlayer.getPersistentData().contains("ActiveBT")) {
            return serverPlayer.getPersistentData().getString("ActiveBT");
        }
        return "None";
    }

    /**
     * Safely checks the current attempt count for a specific floor from NBT.
     */
    public static int getFloorAttempts(UUID uuid, String floorId) {
        ServerPlayer serverPlayer = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        String attemptKey = "BT_Attempts_" + floorId;
        if (serverPlayer != null && serverPlayer.getPersistentData().contains(attemptKey)) {
            return serverPlayer.getPersistentData().getInt(attemptKey);
        }
        return 0;
    }
}