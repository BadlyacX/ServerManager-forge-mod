package com.badlyac.servermanager.permission;

import com.badlyac.servermanager.utils.Msg;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class CommandPermission {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("permission")
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.string())
                                .then(Commands.argument("permission", StringArgumentType.string())
                                        .executes(context -> {
                                            String playerName = StringArgumentType.getString(context, "player");
                                            String permission = StringArgumentType.getString(context, "permission");
                                            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayerByName(playerName);
                                            if (player != null) {
                                                PermissionManager.setPermission(player.getUUID(), permission);
                                                Msg.sendColoredMsgToPlayer(context.getSource(), "Permission set for " + playerName, ChatFormatting.GREEN);
                                            } else {
                                                Msg.sendColoredMsgToPlayer(context.getSource(), "Player not found", ChatFormatting.RED);
                                            }
                                            return 1;
                                        }))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", StringArgumentType.string())
                                .then(Commands.argument("permission", StringArgumentType.string())
                                        .executes(context -> {
                                            String playerName = StringArgumentType.getString(context, "player");
                                            String permission = StringArgumentType.getString(context, "permission");
                                            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayerByName(playerName);
                                            if (player != null) {
                                                PermissionManager.removePermission(player.getUUID(), permission);
                                                Msg.sendColoredMsgToPlayer(context.getSource(), "Permission removed for " + playerName, ChatFormatting.GREEN);
                                            } else {
                                                Msg.sendColoredMsgToPlayer(context.getSource(), "Player not found", ChatFormatting.RED);
                                            }
                                            return 1;
                                        })))));
    }
}