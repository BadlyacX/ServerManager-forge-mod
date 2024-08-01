package com.badlyac.mod.afk;

import com.badlyac.mod.Main;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Main.MODID)
public class AFKTimer {
    private static final long AFK_TIMEOUT = 5 * 60 * 1000;
    private static final Map<UUID, Long> lastActivityMap = new HashMap<>();
    private static final Map<UUID, Boolean> afkStatusMap = new HashMap<>();
    private static final Map<UUID, GameType> originalGameModeMap = new HashMap<>();
    private static final Map<UUID, Coordinates> afkCoordinatesMap = new HashMap<>();
    private static final File gameModeFile = new File("config/RecordGameMode.json");
    private static final Gson gson = new Gson();
    private static final Map<UUID, GameType> savedGameModeMap = new HashMap<>();

    private record Coordinates(double x, double y, double z) {
        boolean isClose(double x, double y, double z) {
            return Math.abs(this.x - x) < 0.1 && Math.abs(this.y - y) < 0.1 && Math.abs(this.z - z) < 0.1;
        }
    }

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
                        afkCoordinatesMap.put(playerId, new Coordinates(player.getX(), player.getY(), player.getZ()));
                    }
                    return 1;
                }));
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        lastActivityMap.clear();
        afkStatusMap.clear();
        originalGameModeMap.clear();
        afkCoordinatesMap.clear();
        loadGameModeData();
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        loadGameModeData();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        saveGameModeData();
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        UUID playerId = event.getEntity().getUUID();
        lastActivityMap.put(playerId, System.currentTimeMillis());
        afkStatusMap.put(playerId, false);
        afkCoordinatesMap.remove(playerId);
        GameType savedGameMode = savedGameModeMap.get(playerId);
        if (savedGameMode != null) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            player.setGameMode(savedGameMode);
            savedGameModeMap.remove(playerId);
            saveGameModeData();
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID playerId = event.getEntity().getUUID();
        if (afkStatusMap.getOrDefault(playerId, false)) {
            savedGameModeMap.put(playerId, originalGameModeMap.get(playerId));
            saveGameModeData();
        }
        lastActivityMap.remove(playerId);
        afkStatusMap.remove(playerId);
        originalGameModeMap.remove(playerId);
        afkCoordinatesMap.remove(playerId);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            ServerPlayer player = (ServerPlayer) event.player;
            UUID playerId = player.getUUID();
            long currentTime = System.currentTimeMillis();

            boolean wasAfk = afkStatusMap.getOrDefault(playerId, false);
            Coordinates storedCoordinates = afkCoordinatesMap.get(playerId);

            if (wasAfk && storedCoordinates != null && !storedCoordinates.isClose(player.getX(), player.getY(), player.getZ())) {
                Component backMessage = Component.literal(player.getName().getString() + " 回來了")
                        .withStyle(ChatFormatting.GRAY);
                for (ServerPlayer p : player.server.getPlayerList().getPlayers()) {
                    p.sendSystemMessage(backMessage);
                }
                GameType originalGameMode = originalGameModeMap.get(playerId);
                if (originalGameMode != null) {
                    player.setGameMode(originalGameMode);
                    originalGameModeMap.remove(playerId);
                }
                afkStatusMap.put(playerId, false);
                afkCoordinatesMap.remove(playerId);
            }

            lastActivityMap.put(playerId, currentTime);
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
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
                    afkCoordinatesMap.put(playerId, new Coordinates(player.getX(), player.getY(), player.getZ()));
                }
            }
        }
    }

    private static void loadGameModeData() {
        if (gameModeFile.exists()) {
            try (FileReader reader = new FileReader(gameModeFile)) {
                Type type = new TypeToken<Map<UUID, GameType>>() {
                }.getType();
                Map<UUID, GameType> data = gson.fromJson(reader, type);
                if (data != null) {
                    savedGameModeMap.putAll(data);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void saveGameModeData() {
        try (FileWriter writer = new FileWriter(gameModeFile)) {
            gson.toJson(savedGameModeMap, writer);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
