package com.rarecandy.rarecandyapi.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private int level;
    private int xp;
    private int totalCatches;
    private int shinyCatches;
    private int bossDefeats;
    private int wildDefeats;
    private int npcWins;
    private int eggsHatched;
    private int evolutions;
    private int raidWins;
    private int raidCaptures;
    private int tradesCompleted;
    private int legendariesCaught;
    private int ultraBeastsCaught;
    private int pokestopsSpun;
    private int pokeLootsClaimed;
    private int apricornsPicked;
    private int successfulFishes;
    private int pokemonLeveledUp;
    private int megasUsed;
    private int dynamaxUsed;
    private int fossilsRestored;
    private int eggsObtained;
    private int shrinesActivated;
    private int zMovesUsed;
    private int mythicalsCaught;
    private int prestigeLevel;
    private int evolutionStonesUsed;
    private final Map<String, Integer> questProgress;

    private int prestigeTokens;
    private int lifetimeCatches;
    private int lifetimeLegendaries;
    private int lifetimeBossDefeats;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.level = 1;
        this.xp = 0;
        this.totalCatches = 0;
        this.shinyCatches = 0;
        this.bossDefeats = 0;
        this.wildDefeats = 0;
        this.npcWins = 0;
        this.eggsHatched = 0;
        this.evolutions = 0;
        this.raidWins = 0;
        this.raidCaptures = 0;
        this.tradesCompleted = 0;
        this.legendariesCaught = 0;
        this.ultraBeastsCaught = 0;
        this.pokestopsSpun = 0;
        this.pokeLootsClaimed = 0;
        this.megasUsed = 0;
        this.dynamaxUsed = 0;
        this.apricornsPicked = 0;
        this.pokemonLeveledUp = 0;
        this.successfulFishes = 0;
        this.fossilsRestored = 0;
        this.eggsObtained = 0;
        this.shrinesActivated = 0;
        this.zMovesUsed = 0;
        this.evolutionStonesUsed = 0;
        this.mythicalsCaught = 0;
        this.prestigeLevel = 0;
        this.prestigeTokens = 0;
        this.lifetimeCatches = 0;
        this.lifetimeLegendaries = 0;
        this.lifetimeBossDefeats = 0;
        this.questProgress = new HashMap<>();
    }

    public void resetPrestigeStats() {
        this.totalCatches = 0;
        this.shinyCatches = 0;
        this.legendariesCaught = 0;
        this.mythicalsCaught = 0;
        this.ultraBeastsCaught = 0;
        this.bossDefeats = 0;
        this.npcWins = 0;
        this.wildDefeats = 0;
        this.raidCaptures = 0;
        this.raidWins = 0;
        this.level = 1;
        this.xp = 0;
        this.eggsHatched = 0;
        this.evolutions = 0;
        this.evolutionStonesUsed = 0;
        this.successfulFishes = 0;
        this.pokeLootsClaimed = 0;
        this.pokestopsSpun = 0;
        this.apricornsPicked = 0;
        this.fossilsRestored = 0;
        this.eggsObtained = 0;
        this.shrinesActivated = 0;
        this.megasUsed = 0;
        this.dynamaxUsed = 0;
        this.zMovesUsed = 0;
        this.tradesCompleted = 0;
        this.pokemonLeveledUp = 0;
    }

    public UUID getUuid() { return uuid; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
    public void addXp(int amount) { this.xp += amount; }

    public int getPrestigeLevel() { return prestigeLevel; }
    public void setPrestigeLevel(int level) { this.prestigeLevel = level; }
    public void incrementPrestigeLevel() { this.prestigeLevel++; }

    public int getPrestigeTokens() { return prestigeTokens; }
    public void setPrestigeTokens(int tokens) { this.prestigeTokens = tokens; }
    public void addPrestigeTokens(int amount) { this.prestigeTokens += amount; }

    public int getLifetimeCatches() { return lifetimeCatches; }
    public void setLifetimeCatches(int lifetimeCatches) { this.lifetimeCatches = lifetimeCatches; }

    public int getLifetimeLegendaries() { return lifetimeLegendaries; }
    public void setLifetimeLegendaries(int lifetimeLegendaries) { this.lifetimeLegendaries = lifetimeLegendaries; }

    public int getLifetimeBossDefeats() { return lifetimeBossDefeats; }
    public void setLifetimeBossDefeats(int lifetimeBossDefeats) { this.lifetimeBossDefeats = lifetimeBossDefeats; }

    public int getTotalCatches() { return totalCatches; }
    public void setTotalCatches(int totalCatches) { this.totalCatches = totalCatches; }
    public void incrementTotalCatches() { this.totalCatches++; this.lifetimeCatches++; }

    public int getShinyCatches() { return shinyCatches; }
    public void setShinyCatches(int shinyCatches) { this.shinyCatches = shinyCatches; }
    public void incrementShinyCatches() { this.shinyCatches++; }

    public int getBossDefeats() { return bossDefeats; }
    public void setBossDefeats(int bossDefeats) { this.bossDefeats = bossDefeats; }
    public void incrementBossDefeats() { this.bossDefeats++; this.lifetimeBossDefeats++; }

    public int getWildDefeats() { return wildDefeats; }
    public void setWildDefeats(int wildDefeats) { this.wildDefeats = wildDefeats; }
    public void incrementWildDefeats() { this.wildDefeats++; }

    public int getNpcWins() { return npcWins; }
    public void setNpcWins(int npcWins) { this.npcWins = npcWins; }
    public void incrementNpcWins() { this.npcWins++; }

    public int getEggsHatched() { return eggsHatched; }
    public void setEggsHatched(int eggsHatched) { this.eggsHatched = eggsHatched; }
    public void incrementEggsHatched() { this.eggsHatched++; }

    public int getEvolutions() { return evolutions; }
    public void setEvolutions(int evolutions) { this.evolutions = evolutions; }
    public void incrementEvolutions() { this.evolutions++; }

    public int getRaidWins() { return raidWins; }
    public void setRaidWins(int raidWins) { this.raidWins = raidWins; }
    public void incrementRaidWins() { this.raidWins++; }

    public int getRaidCaptures() { return raidCaptures; }
    public void setRaidCaptures(int raidCaptures) { this.raidCaptures = raidCaptures; }
    public void incrementRaidCaptures() { this.raidCaptures++; }

    public int getTradesCompleted() { return tradesCompleted; }
    public void setTradesCompleted(int tradesCompleted) { this.tradesCompleted = tradesCompleted; }
    public void incrementTradesCompleted() { this.tradesCompleted++; }

    public int getSuccessfulFishes() { return successfulFishes; }
    public void setSuccessfulFishes(int successfulFishes) { this.successfulFishes = successfulFishes; }
    public void incrementSuccessfulFishes() { this.successfulFishes++; }

    public int getLegendariesCaught() { return legendariesCaught; }
    public void setLegendariesCaught(int legendariesCaught) { this.legendariesCaught = legendariesCaught; }
    public void incrementLegendariesCaught() { this.legendariesCaught++; this.lifetimeLegendaries++; }

    public int getUltraBeastsCaught() { return ultraBeastsCaught; }
    public void setUltraBeastsCaught(int ultraBeastsCaught) { this.ultraBeastsCaught = ultraBeastsCaught; }
    public void incrementUltraBeastsCaught() { this.ultraBeastsCaught++; }

    public int getPokestopsSpun() { return pokestopsSpun; }
    public void setPokestopsSpun(int pokestopsSpun) { this.pokestopsSpun = pokestopsSpun; }
    public void incrementPokestopsSpun() { this.pokestopsSpun++; }

    public int getPokeLootsClaimed() { return pokeLootsClaimed; }
    public void setPokeLootsClaimed(int pokeLootsClaimed) { this.pokeLootsClaimed = pokeLootsClaimed; }
    public void incrementPokeLootsClaimed() { this.pokeLootsClaimed++; }

    public int getApricornsPicked() { return apricornsPicked; }
    public void setApricornsPicked(int apricornsPicked) { this.apricornsPicked = apricornsPicked; }
    public void incrementApricornsPicked() { this.apricornsPicked++; }

    public int getPokemonLeveledUp() { return pokemonLeveledUp; }
    public void setPokemonLeveledUp(int pokemonLeveledUp) { this.pokemonLeveledUp = pokemonLeveledUp; }
    public void incrementPokemonLeveledUp() { this.pokemonLeveledUp++; }

    public int getMegasUsed() { return megasUsed; }
    public void setMegasUsed(int megasUsed) { this.megasUsed = megasUsed; }
    public void incrementMegasUsed() { this.megasUsed++; }

    public int getDynamaxUsed() { return dynamaxUsed; }
    public void setDynamaxUsed(int dynamaxUsed) { this.dynamaxUsed = dynamaxUsed; }
    public void incrementDynamaxUsed() { this.dynamaxUsed++; }

    public int getFossilsRestored() { return fossilsRestored; }
    public void setFossilsRestored(int fossilsRestored) { this.fossilsRestored = fossilsRestored; }
    public void incrementFossilsRestored() { this.fossilsRestored++; }

    public int getEggsObtained() { return eggsObtained; }
    public void setEggsObtained(int eggsObtained) { this.eggsObtained = eggsObtained; }
    public void incrementEggsObtained() { this.eggsObtained++; }

    public int getShrinesActivated() { return shrinesActivated; }
    public void setShrinesActivated(int shrinesActivated) { this.shrinesActivated = shrinesActivated; }
    public void incrementShrinesActivated() { this.shrinesActivated++; }

    public int getZMovesUsed() { return zMovesUsed; }
    public void setZMovesUsed(int zMovesUsed) { this.zMovesUsed = zMovesUsed; }
    public void incrementZMovesUsed() { this.zMovesUsed++; }

    public int getEvolutionStonesUsed() { return evolutionStonesUsed; }
    public void setEvolutionStonesUsed(int evolutionStonesUsed) { this.evolutionStonesUsed = evolutionStonesUsed; }
    public void incrementEvolutionStonesUsed() { this.evolutionStonesUsed++; }

    public int getMythicalsCaught() { return mythicalsCaught; }
    public void setMythicalsCaught(int mythicalsCaught) { this.mythicalsCaught = mythicalsCaught; }
    public void incrementMythicalsCaught() { this.mythicalsCaught++; }

    public Map<String, Integer> getQuestProgress() { return questProgress; }
    public int getQuestProgress(String questId) { return questProgress.getOrDefault(questId, 0); }
    public void setQuestProgress(String questId, int progress) { questProgress.put(questId, progress); }
    public void incrementQuestProgress(String questId, int amount) { questProgress.put(questId, getQuestProgress(questId) + amount); }
}