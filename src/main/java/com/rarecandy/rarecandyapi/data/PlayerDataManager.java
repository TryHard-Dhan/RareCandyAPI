package com.rarecandy.rarecandyapi.data;

import com.rarecandy.rarecandyapi.database.DatabaseManager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    private static final Map<UUID, PlayerData> DATA_CACHE = new ConcurrentHashMap<>();

    public static PlayerData get(UUID uuid) {
        if (DATA_CACHE.containsKey(uuid)) {
            return DATA_CACHE.get(uuid);
        }

        PlayerData temporaryData = new PlayerData(uuid);
        DATA_CACHE.put(uuid, temporaryData);

        DatabaseManager.loadPlayerAsync(uuid).thenAccept(loadedData -> {
            if (loadedData != null) {
                DATA_CACHE.put(uuid, loadedData);
            }
        });

        return temporaryData;
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