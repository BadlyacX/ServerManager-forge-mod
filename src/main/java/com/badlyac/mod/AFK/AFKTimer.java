package com.badlyac.mod.AFK;

import com.badlyac.mod.Main;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class AFKTimer {
    private static final long AFK_TIMEOUT = 5 * 60 * 1000;
    private static final Map<UUID, Long> lastActivityMap = new HashMap<>();
    private static final Map<UUID, Boolean> afkStatusMap = new HashMap<>();
    private static final Map<UUID, GameType> originalGameModeMap = new HashMap<>();

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal("afk")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    UUID playerId = player.getUUID();
                    if (!afkStatusMap.getOrDefault(playerId, false)) {
                        Component afkMessage = Component.literal(player.getName().getString() + " 離開了...")
                                .withStyle(ChatFormatting.GRAY);
                        for (ServerPlayer p : player.server.getPlayerList().getPlayers()) {
                            p.sendSystemMessage(afkMessage);
                        }
                        originalGameModeMap.put(playerId, player.gameMode.getGameModeForPlayer());
                        player.setGameMode(GameType.SPECTATOR);
                        afkStatusMap.put(playerId, true);
                        lastActivityMap.put(playerId, System.currentTimeMillis());
                    }
                    return 1;
                }));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        lastActivityMap.clear();
        afkStatusMap.clear();
        originalGameModeMap.clear();
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        UUID playerId = event.getEntity().getUUID();
        lastActivityMap.put(playerId, System.currentTimeMillis());
        afkStatusMap.put(playerId, false);
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID playerId = event.getEntity().getUUID();
        lastActivityMap.remove(playerId);
        afkStatusMap.remove(playerId);
        originalGameModeMap.remove(playerId);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            ServerPlayer player = (ServerPlayer) event.player;
            UUID playerId = player.getUUID();
            long currentTime = System.currentTimeMillis();

            if (player.xOld != player.getX() || player.yOld != player.getY() || player.zOld != player.getZ()) {
                boolean wasAfk = afkStatusMap.getOrDefault(playerId, false);
                if (wasAfk) {
                    Component backMessage = Component.literal(player.getName().getString() + " 回來了")
                            .withStyle(ChatFormatting.GRAY);
                    for (ServerPlayer p : player.server.getPlayerList().getPlayers()) {
                        p.sendSystemMessage(backMessage);
                    }
                    GameType originalGameMode = originalGameModeMap.get(playerId);
                    player.setGameMode(originalGameMode);
                    afkStatusMap.put(playerId, false);
                }
                lastActivityMap.put(playerId, currentTime);
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            long currentTime = System.currentTimeMillis();
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                UUID playerId = player.getUUID();
                long lastActivityTime = lastActivityMap.getOrDefault(playerId, currentTime);
                boolean isAfk = afkStatusMap.getOrDefault(playerId, false);

                if (currentTime - lastActivityTime >= AFK_TIMEOUT && !isAfk) {
                    Component afkMessage = Component.literal(player.getName().getString() + " 離開了...")
                            .withStyle(ChatFormatting.GRAY);
                    for (ServerPlayer p : event.getServer().getPlayerList().getPlayers()) {
                        p.sendSystemMessage(afkMessage);
                    }
                    originalGameModeMap.put(playerId, player.gameMode.getGameModeForPlayer());
                    player.setGameMode(GameType.SPECTATOR);
                    afkStatusMap.put(playerId, true);
                }
            }
        }
    }
}
