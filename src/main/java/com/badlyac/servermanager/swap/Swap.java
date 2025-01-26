package com.badlyac.servermanager.swap;

import com.badlyac.servermanager.utils.Msg;
import com.badlyac.servermanager.utils.Timer;
import com.badlyac.servermanager.utils.container.PlayerData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Mod.EventBusSubscriber
public class Swap {

    private static boolean swapEnabled = false;
    private static boolean timerStarted = false;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (!swapEnabled) return;

        if (!timerStarted) {
            int random = (int) (Math.random() * 10) + 1;
            Timer timer = new Timer();

            for (ServerPlayer p : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                Msg.sendColoredMsgToPlayer(p, "下次交換時間是 " + random + " 分鐘後", ChatFormatting.GREEN);
            }

            timerStarted = true;
            timer.countDownMin(random, () -> {
                for (ServerPlayer p : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                    Msg.sendColoredMsgToPlayer(p, "SWAP!!", ChatFormatting.GOLD);
                }
                swapPlayers();
                timerStarted = false;
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCommand(@NotNull RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("swap")
                        .requires(s -> s.hasPermission(2))
                        .executes(Swap::toggleSwap)
        );
    }

    private static int toggleSwap(@NotNull CommandContext<CommandSourceStack> context) {
        swapEnabled = !swapEnabled;
        Component message = swapEnabled ? Component.literal("Swap functionality has been enabled!").setStyle(Style.EMPTY).withStyle(ChatFormatting.GREEN) : Component.literal("Swap functionality has been disabled!").setStyle(Style.EMPTY).withStyle(ChatFormatting.RED);
        context.getSource().sendSuccess(() -> message, true);
        timerStarted = false; // Reset timer state when toggling
        return Command.SINGLE_SUCCESS;
    }

    private static ServerPlayer getPlayer(UUID uuid) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server.getPlayerList().getPlayer(uuid);
    }

    public static void swapPlayers() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        int validPlayer = 0;
        Map<Integer, PlayerData> playerLoc = new HashMap<>();
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            if (p.gameMode.getGameModeForPlayer() == GameType.SURVIVAL || p.gameMode.getGameModeForPlayer() == GameType.ADVENTURE) {
                validPlayer++;
                playerLoc.put(validPlayer, new PlayerData(p.getName().toString(), p.level().dimension(), p.getX(), p.getY(), p.getZ(), p.getYRot(), p.getXRot()));
                System.out.println(playerLoc.get(validPlayer));
            }
        }

        List<UUID> eligiblePlayers = new ArrayList<>();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            GameType gameMode = player.gameMode.getGameModeForPlayer();
            if (gameMode == GameType.SURVIVAL || gameMode == GameType.ADVENTURE) {
                eligiblePlayers.add(player.getUUID());
            }
        }

        Map<Integer, PlayerData> locationMap = new HashMap<>();
        List<Integer> locationKeys = new ArrayList<>(locationMap.keySet());

        Random random = new Random();
        for (UUID uuid : eligiblePlayers) {
            if (locationKeys.isEmpty()) {
                System.out.println("No more locations available for assignment!");
                break;
            }

            int randomIndex = random.nextInt(locationKeys.size());
            int locationId = locationKeys.get(randomIndex);

            PlayerData assignedLocation = locationMap.get(locationId);

            Objects.requireNonNull(getPlayer(uuid)).teleportTo(
                    assignedLocation.getX(),
                    assignedLocation.getY(),
                    assignedLocation.getZ()
            );
            System.out.println("Player " + getPlayer(uuid).getName().getString() + " teleported to location: " + assignedLocation);
            locationKeys.remove(randomIndex);
        }
    }
}
