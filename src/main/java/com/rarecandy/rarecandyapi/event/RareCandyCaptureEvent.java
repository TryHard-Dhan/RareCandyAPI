package com.rarecandy.rarecandyapi.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

public class RareCandyCaptureEvent extends Event {
    private final ServerPlayer player;
    private final String pokemonName;
    private final boolean isShiny;

    public RareCandyCaptureEvent(ServerPlayer player, String pokemonName, boolean isShiny) {
        this.player = player;
        this.pokemonName = pokemonName;
        this.isShiny = isShiny;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public boolean isShiny() {
        return isShiny;
    }
}