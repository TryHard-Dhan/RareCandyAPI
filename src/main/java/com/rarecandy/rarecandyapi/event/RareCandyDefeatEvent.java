package com.rarecandy.rarecandyapi.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

public class RareCandyDefeatEvent extends Event {
    private final ServerPlayer player;
    private final String pokemonName;
    private final boolean isBoss;

    public RareCandyDefeatEvent(ServerPlayer player, String pokemonName, boolean isBoss) {
        this.player = player;
        this.pokemonName = pokemonName;
        this.isBoss = isBoss;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public boolean isBoss() {
        return isBoss;
    }
}