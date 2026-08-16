package com.rarecandy.rarecandyapi.data;

import com.rarecandy.rarecandyapi.database.DatabaseManager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    private static final Map<UUID, PlayerData> DATA_CACHE = new ConcurrentHashMap<>();

    public static PlayerData get(UUID uuid) {
        return DATA_CACHE.computeIfAbsent(uuid, k -> DatabaseManager.loadPlayerAsync(k).join());
    }

    public static void save(UUID uuid) {
        PlayerData data = DATA_CACHE.get(uuid);
        if (data != null) {
            save(data);
        }
    }

    public static void save(PlayerData data) {
        DatabaseManager.savePlayerAsync(data);
    }

    public static void saveAndUnload(UUID uuid) {
        PlayerData data = DATA_CACHE.remove(uuid);
        if (data != null) {
            DatabaseManager.savePlayerAsync(data);
        }
    }

    public static void saveAll() {
        for (PlayerData data : DATA_CACHE.values()) {
            DatabaseManager.savePlayerAsync(data);
        }
    }
}