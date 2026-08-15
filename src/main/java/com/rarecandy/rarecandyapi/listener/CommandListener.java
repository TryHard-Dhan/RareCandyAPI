package com.rarecandy.rarecandyapi.listener;

import com.rarecandy.rarecandyapi.command.RareCandyCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class CommandListener {
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        RareCandyCommand.register(event.getDispatcher());
    }
}