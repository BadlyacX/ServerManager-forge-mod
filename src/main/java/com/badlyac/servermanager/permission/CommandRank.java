package com.badlyac.servermanager.permission;

import com.badlyac.servermanager.utils.Msg;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

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
                                                PermissionManager.setRank(player.getUUID(), rank);
                                                Msg.sendColoredMsgToPlayer(context.getSource(), "Rank " + rank + " set for " + playerName, ChatFormatting.GREEN);
                                            } else {
                                                Msg.sendColoredMsgToPlayer(context.getSource(), "Player not found", ChatFormatting.RED);
                                            }
                                            return 1;
                                        }))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", StringArgumentType.string())
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayerByName(playerName);
                                    if (player != null) {
                                        PermissionManager.setRank(player.getUUID(), "default");
                                        Msg.sendColoredMsgToPlayer(context.getSource(), "Rank removed for " + playerName, ChatFormatting.GREEN);
                                    } else {
                                        Msg.sendColoredMsgToPlayer(context.getSource(), "Player not found", ChatFormatting.RED);
                                    }
                                    return 1;
                                }))));
    }
}
