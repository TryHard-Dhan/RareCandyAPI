package com.rarecandy.rarecandyapi.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.api.daycare.event.DayCareEvent;
import com.pixelmonmod.pixelmon.api.events.ApricornEvent;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.DynamaxEvent;
import com.pixelmonmod.pixelmon.api.events.EggHatchEvent;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.api.events.FishingEvent;
import com.pixelmonmod.pixelmon.api.events.FossilMachineEvent;
import com.pixelmonmod.pixelmon.api.events.LevelUpEvent;
import com.pixelmonmod.pixelmon.api.events.MegaEvolutionEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import com.pixelmonmod.pixelmon.api.events.PokeLootEvent;
import com.pixelmonmod.pixelmon.api.events.PokeStopEvent;
import com.pixelmonmod.pixelmon.api.events.battles.AttackEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.rarecandy.rarecandyapi.data.PlayerData;
import com.rarecandy.rarecandyapi.data.PlayerDataManager;
import com.rarecandy.rarecandyapi.event.RareCandyCaptureEvent;
import com.rarecandy.rarecandyapi.event.RareCandyRaidCaptureEvent;
import com.rarecandy.rarecandyapi.event.RareCandyTradeEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

public class PixelmonEventInterceptor {

    private static boolean isRegistered = false;

    public static void register() {
        if (isRegistered) {
            return;
        }
        Pixelmon.EVENT_BUS.register(new PixelmonEventInterceptor());
        isRegistered = true;
    }

    @SubscribeEvent
    public void onCapture(CaptureEvent.SuccessfulCapture event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            String pokemonName = event.getPokemon().getSpecies().getName();
            boolean isShiny = event.getPokemon().isShiny();
            boolean isLegendary = event.getPokemon().isLegendary();
            boolean isUltraBeast = event.getPokemon().isUltraBeast();
            boolean isMythical = event.getPokemon().isMythical();

            PlayerData data = PlayerDataManager.get(player.getUUID());
            data.incrementTotalCatches();

            if (isShiny) data.incrementShinyCatches();
            if (isLegendary) data.incrementLegendariesCaught();
            if (isUltraBeast) data.incrementUltraBeastsCaught();
            if (isMythical) data.incrementMythicalsCaught();

            NeoForge.EVENT_BUS.post(new RareCandyCaptureEvent(player, pokemonName, isShiny));
        }
    }

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event) {
        boolean isTrainer = false;
        boolean isWild = false;
        boolean isBoss = false;

        for (BattleParticipant opponent : event.getResults().keySet()) {
            String participantName = opponent.getClass().getSimpleName();
            if (participantName.contains("Trainer")) isTrainer = true;
            if (participantName.contains("Wild")) isWild = true;
            if (participantName.contains("Boss") || participantName.contains("Raid")) isBoss = true;
        }

        for (Player p : event.getPlayers()) {
            if (p instanceof ServerPlayer player) {
                BattleResults result = event.getResult(player).orElse(null);
                if (result != null && result.name().equals("VICTORY")) {
                    PlayerData data = PlayerDataManager.get(player.getUUID());
                    if (isBoss) {
                        data.incrementBossDefeats();
                    } else if (isTrainer) {
                        data.incrementNpcWins();
                    } else if (isWild) {
                        data.incrementWildDefeats();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEggHatch(EggHatchEvent.Post event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            PlayerData data = PlayerDataManager.get(player.getUUID());
            data.incrementEggsHatched();
        }
    }

    @SubscribeEvent
    public void onEvolve(EvolveEvent.Post event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            PlayerData data = PlayerDataManager.get(player.getUUID());
            data.incrementEvolutions();

            if (EvolveEvent.Constants.INTERACTION.equals(event.getCause())) {
                data.incrementEvolutionStonesUsed();
            }
        }
    }

    @SubscribeEvent
    public void onRaidCatch(CaptureEvent.SuccessfulRaidCapture event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            String pokemonName = event.getPokemon().getSpecies().getName();
            boolean isShiny = event.getPokemon().isShiny();

            PlayerData data = PlayerDataManager.get(player.getUUID());
            data.incrementRaidCaptures();
            data.incrementRaidWins();

            NeoForge.EVENT_BUS.post(new RareCandyRaidCaptureEvent(player, pokemonName, isShiny));
        }
    }

    @SubscribeEvent
    public void onTrade(PixelmonTradeEvent.Post event) {
        if (event.getPlayer1() instanceof ServerPlayer player1 &&
                event.getPlayer2() instanceof ServerPlayer player2) {

            PlayerDataManager.get(player1.getUUID()).incrementTradesCompleted();
            PlayerDataManager.get(player2.getUUID()).incrementTradesCompleted();

            NeoForge.EVENT_BUS.post(new RareCandyTradeEvent(player1, player2));
        }
    }

    @SubscribeEvent
    public void onPokeLootClaim(PokeLootEvent.Claim event) {
        if (event.player instanceof ServerPlayer player) {
            PlayerData data = PlayerDataManager.get(player.getUUID());
            data.incrementPokeLootsClaimed();
        }
    }

    @SubscribeEvent
    public void onPokestopSpin(PokeStopEvent.Drops.Post event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            PlayerData data = PlayerDataManager.get(player.getUUID());
            data.incrementPokestopsSpun();
        }
    }

    @SubscribeEvent
    public void onApricornPick(ApricornEvent.Pick event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            PlayerData data = PlayerDataManager.get(player.getUUID());
            data.incrementApricornsPicked();
        }
    }

    @SubscribeEvent
    public void onPokemonLevelUp(LevelUpEvent.Post event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            PlayerData data = PlayerDataManager.get(player.getUUID());
            data.incrementPokemonLeveledUp();
        }
    }

    @SubscribeEvent
    public void onFishingReel(FishingEvent.Reel event) {
        if (event.isPokemon() && event.player instanceof ServerPlayer player) {
            PlayerData data = PlayerDataManager.get(player.getUUID());
            data.incrementSuccessfulFishes();
        }
    }

    @SubscribeEvent
    public void onMegaEvolve(MegaEvolutionEvent.Post event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            PlayerDataManager.get(player.getUUID()).incrementMegasUsed();
        }
    }

    @SubscribeEvent
    public void onDynamax(DynamaxEvent.BattleEvolve.Post event) {
        if (event.getPokemon().getPlayerOwner() instanceof ServerPlayer player) {
            PlayerDataManager.get(player.getUUID()).incrementDynamaxUsed();
        }
    }

    @SubscribeEvent
    public void onFossilClaim(FossilMachineEvent.RemoveFossil event) {
        if (event.getPlayer() != null) {
            PlayerData data = PlayerDataManager.get(event.getPlayer().getUUID());
            data.incrementFossilsRestored();
        }
    }

    @SubscribeEvent
    public void onEggCollect(DayCareEvent.PostCollect event) {
        if (event.getPlayer() != null) {
            PlayerData data = PlayerDataManager.get(event.getPlayer().getUUID());
            data.incrementEggsObtained();
        }
    }

    @SubscribeEvent
    public void onAttackUse(AttackEvent.Use event) {
        if (event.user != null && event.user.usingZ) {
            ServerPlayer player = event.user.getOwnerPlayer();
            if (player != null) {
                PlayerData data = PlayerDataManager.get(player.getUUID());
                data.incrementZMovesUsed();
            }
        }
    }
}