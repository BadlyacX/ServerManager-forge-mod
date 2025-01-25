package com.badlyac.servermanager.swap;

import com.badlyac.servermanager.utils.container.PlayerData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;

@Mod.EventBusSubscriber
public class Swap {

    private static boolean swapEnabled = false;

    @SubscribeEvent
    public static void onRegisterCommand(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("swap")
                        .requires(s -> s.hasPermission(2))
                        .executes(Swap::toggleSwap)
        );
    }

    private static int toggleSwap(CommandContext<CommandSourceStack> context) {
        swapEnabled = !swapEnabled;
        Component message = swapEnabled ? Component.literal("Swap functionality has been enabled!").setStyle(Style.EMPTY).withStyle(ChatFormatting.GREEN) : Component.literal("Swap functionality has been disabled!").setStyle(Style.EMPTY).withStyle(ChatFormatting.RED);
        context.getSource().sendSuccess(() -> message, true);
        return Command.SINGLE_SUCCESS;
    }

    public static boolean isSwapEnabled() {
        return swapEnabled;
    }

    public void swapPlayers(ServerPlayer triggerPlayer) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }

        List<ServerPlayer> validPlayers = new ArrayList<>();
        Map<Integer, PlayerData> playerLocations = new HashMap<>();
        int id = 0;

        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            GameType gameMode = p.gameMode.getGameModeForPlayer();
            if (gameMode == GameType.SURVIVAL || gameMode == GameType.ADVENTURE) {
                validPlayers.add(p);

                ResourceKey<Level> dimensionKey = p.level().dimension();

                PlayerData data = new PlayerData(
                        p.getName().getString(),
                        dimensionKey,
                        p.getX(),
                        p.getY(),
                        p.getZ(),
                        p.getYRot(), // yaw
                        p.getXRot()  // pitch
                );
                playerLocations.put(id, data);
                id++;
            }
        }

        if (validPlayers.isEmpty()) {
            return;
        }

        List<Integer> locationIds = new ArrayList<>(playerLocations.keySet());
        Collections.shuffle(locationIds);

        for (int i = 0; i < validPlayers.size(); i++) {
            ServerPlayer p = validPlayers.get(i);
            int randomId = locationIds.get(i);
            PlayerData data = playerLocations.get(randomId);

            ServerLevel targetLevel = server.getLevel(data.dimension());
            if (targetLevel == null) {
                continue;
            }

            p.teleportTo(
                    targetLevel,
                    data.x(),
                    data.y(),
                    data.z(),
                    data.yaw(),
                    data.pitch()
            );
        }
    }
}