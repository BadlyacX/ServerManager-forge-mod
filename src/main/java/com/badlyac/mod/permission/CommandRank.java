package com.badlyac.mod.permission;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

public class CommandRank {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("rank")
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.string())
                                .then(Commands.argument("rank", StringArgumentType.string())
                                        .executes(context -> {
                                            String playerName = StringArgumentType.getString(context, "player");
                                            String rank = StringArgumentType.getString(context, "rank");
                                            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayerByName(playerName);
                                            if (player != null) {
                                                PermissionSystemManager.setRank(player.getUUID(), rank);
                                                context.getSource().sendSuccess(() -> Component.literal("Rank " + rank + " set for " + playerName), false);
                                            } else {
                                                context.getSource().sendFailure(Component.literal("Player not found"));
                                            }
                                            return 1;
                                        }))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", StringArgumentType.string())
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayerByName(playerName);
                                    if (player != null) {
                                        PermissionSystemManager.setRank(player.getUUID(), "default");
                                        context.getSource().sendSuccess(() -> Component.literal("Rank removed for " + playerName), false);
                                    } else {
                                        context.getSource().sendFailure(Component.literal("Player not found"));
                                    }
                                    return 1;
                                }))));
    }
}
