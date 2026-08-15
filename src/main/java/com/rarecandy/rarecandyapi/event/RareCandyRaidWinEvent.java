package com.rarecandy.rarecandyapi.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

public class RareCandyRaidWinEvent extends Event {
    private final ServerPlayer player;
    private final String pokemonName;
    private final int stars;

    public RareCandyRaidWinEvent(ServerPlayer player, String pokemonName, int stars) {
        this.player = player;
        this.pokemonName = pokemonName;
        this.stars = stars;
    }

    public ServerPlayer getPlayer() { return player; }
    public String getPokemonName() { return pokemonName; }
    public int getStars() { return stars; }
}