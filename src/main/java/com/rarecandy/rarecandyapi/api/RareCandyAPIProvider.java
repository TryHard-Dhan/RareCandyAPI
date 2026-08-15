package com.rarecandy.rarecandyapi.api;

import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.rarecandy.rarecandyapi.data.PlayerData;
import com.rarecandy.rarecandyapi.data.PlayerDataManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Predicate;

public class RareCandyAPIProvider {

    public static PlayerData getPlayerData(UUID uuid) {
        return PlayerDataManager.get(uuid);
    }

    public static PlayerData getPlayerData(ServerPlayer player) {
        return getPlayerData(player.getUUID());
    }

    public static void addPixelPassXP(ServerPlayer player, int xpAmount) {
        PlayerData data = getPlayerData(player);
        data.addXp(xpAmount);
        PlayerDataManager.save(player.getUUID());
    }

    public static void setQuestProgress(ServerPlayer player, String questId, int progress) {
        PlayerData data = getPlayerData(player);
        data.setQuestProgress(questId, progress);
        PlayerDataManager.save(player.getUUID());
    }


    public static int getBalance(ServerPlayer player) {
        BankAccount account = (BankAccount) StorageProxy.getPartyNow(player);
        return account != null ? account.getBalance().intValue() : 0;
    }

    public static void addBalance(ServerPlayer player, int amount) {
        BankAccount account = (BankAccount) StorageProxy.getPartyNow(player);
        if (account != null) {
            account.add(BigDecimal.valueOf(amount));
        }
    }

    public static void removeBalance(ServerPlayer player, int amount) {
        BankAccount account = (BankAccount) StorageProxy.getPartyNow(player);
        if (account != null) {
            account.take(BigDecimal.valueOf(amount));
        }
    }

    public static void setBalance(ServerPlayer player, int amount) {
        BankAccount account = (BankAccount) StorageProxy.getPartyNow(player);
        if (account != null) {
            account.setBalance(BigDecimal.valueOf(amount));
        }
    }


    public static int countPokemonInParty(ServerPlayer player, String speciesName) {
        PlayerPartyStorage party = StorageProxy.getPartyNow(player);
        int count = 0;
        for (Pokemon pokemon : party.getAll()) {
            if (pokemon != null && pokemon.getSpecies().getName().equalsIgnoreCase(speciesName)) {
                count++;
            }
        }
        return count;
    }

    public static int countPokemonInPC(ServerPlayer player, String speciesName) {
        PCStorage pc = StorageProxy.getPCForPlayerNow(player);
        int count = 0;
        for (Pokemon pokemon : pc.getAll()) {
            if (pokemon != null && pokemon.getSpecies().getName().equalsIgnoreCase(speciesName)) {
                count++;
            }
        }
        return count;
    }

    public static boolean hasPokemonInParty(ServerPlayer player, String speciesName) {
        return countPokemonInParty(player, speciesName) > 0;
    }

    public static boolean hasPokemonInPC(ServerPlayer player, String speciesName) {
        return countPokemonInPC(player, speciesName) > 0;
    }

    public static int countCustomPokemonInParty(ServerPlayer player, Predicate<Pokemon> condition) {
        PlayerPartyStorage party = StorageProxy.getPartyNow(player);
        int count = 0;
        for (Pokemon pokemon : party.getAll()) {
            if (pokemon != null && condition.test(pokemon)) {
                count++;
            }
        }
        return count;
    }

    public static int countCustomPokemonInPC(ServerPlayer player, Predicate<Pokemon> condition) {
        PCStorage pc = StorageProxy.getPCForPlayerNow(player);
        int count = 0;
        for (Pokemon pokemon : pc.getAll()) {
            if (pokemon != null && condition.test(pokemon)) {
                count++;
            }
        }
        return count;
    }

    public static boolean takePokemonFromParty(ServerPlayer player, Predicate<Pokemon> condition, int amountToTake) {
        if (countCustomPokemonInParty(player, condition) < amountToTake) {
            return false;
        }
        PlayerPartyStorage party = StorageProxy.getPartyNow(player);
        int removed = 0;
        for (Pokemon pokemon : party.getAll()) {
            if (pokemon != null && condition.test(pokemon)) {
                party.set(pokemon.getPosition(), null);
                removed++;
                if (removed >= amountToTake) {
                    return true;
                }
            }
        }
        return true;
    }

    public static boolean takePokemonFromPC(ServerPlayer player, Predicate<Pokemon> condition, int amountToTake) {
        if (countCustomPokemonInPC(player, condition) < amountToTake) {
            return false;
        }
        PCStorage pc = StorageProxy.getPCForPlayerNow(player);
        int removed = 0;
        for (Pokemon pokemon : pc.getAll()) {
            if (pokemon != null && condition.test(pokemon)) {
                pc.set(pokemon.getPosition(), null);
                removed++;
                if (removed >= amountToTake) {
                    return true;
                }
            }
        }
        return true;
    }


    public static int countItem(ServerPlayer player, Item item) {
        return player.getInventory().countItem(item);
    }

    public static boolean hasItem(ServerPlayer player, Item item, int amount) {
        return countItem(player, item) >= amount;
    }

    public static boolean takeItem(ServerPlayer player, Item item, int amountToTake) {
        if (!hasItem(player, item, amountToTake)) {
            return false;
        }

        int remaining = amountToTake;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.is(item)) {
                if (stack.getCount() >= remaining) {
                    stack.shrink(remaining);
                    return true;
                } else {
                    remaining -= stack.getCount();
                    stack.setCount(0);
                }
            }
        }
        return true;
    }
}