package com.rarecandy.rarecandyapi.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

public class RareCandyTradeEvent extends Event {
    private final ServerPlayer player1;
    private final ServerPlayer player2;

    public RareCandyTradeEvent(ServerPlayer player1, ServerPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public ServerPlayer getPlayer1() { return player1; }
    public ServerPlayer getPlayer2() { return player2; }
}