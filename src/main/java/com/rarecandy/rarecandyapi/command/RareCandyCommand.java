package com.rarecandy.rarecandyapi.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.rarecandy.rarecandyapi.api.RareCandyAPIProvider;
import com.rarecandy.rarecandyapi.data.PlayerData;
import com.rarecandy.rarecandyapi.util.ColorUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class RareCandyCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("rarecandy")
                .then(Commands.literal("stats")
                        .executes(context -> showStats(context.getSource(), context.getSource().getPlayerOrException()))
                        .then(Commands.argument("target", EntityArgument.player())
                                .requires(source -> source.hasPermission(2))
                                .executes(context -> showStats(context.getSource(), EntityArgument.getPlayer(context, "target")))))
                .then(Commands.literal("xp")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("add")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(context -> addXp(context.getSource(), EntityArgument.getPlayer(context, "target"), IntegerArgumentType.getInteger(context, "amount"))))))));
    }

    private static int showStats(CommandSourceStack source, ServerPlayer target) {
        PlayerData data = RareCandyAPIProvider.getPlayerData(target);

        source.sendSystemMessage(ColorUtils.format("<gradient:#00FFAA:#00AAFF>--- " + target.getName().getString() + "'s Stats ---</gradient>", target));
        source.sendSystemMessage(ColorUtils.format("<#00FFAA>Level: <#FFFFFF>" + data.getLevel() + " <#AAAAAA>(XP: " + data.getXp() + ")", target));
        source.sendSystemMessage(ColorUtils.format("<#00FFAA>Total Catches: <#FFFFFF>" + data.getTotalCatches(), target));
        source.sendSystemMessage(ColorUtils.format("<#00FFAA>Shiny Catches: <#FFFFFF>" + data.getShinyCatches(), target));
        source.sendSystemMessage(ColorUtils.format("<#00FFAA>Boss Defeats: <#FFFFFF>" + data.getBossDefeats(), target));

        return 1;
    }

    private static int addXp(CommandSourceStack source, ServerPlayer target, int amount) {
        RareCandyAPIProvider.addPixelPassXP(target, amount);
        source.sendSystemMessage(ColorUtils.format("<#00FF00>Added " + amount + " XP to " + target.getName().getString() + ".", target));
        target.sendSystemMessage(ColorUtils.format("<#00FF00>You received " + amount + " XP!", target));
        return 1;
    }
}