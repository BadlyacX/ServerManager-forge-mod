package com.badlyac.servermanager.permission;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

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
                                                context.getSource().sendSuccess(() ->Component.literal("Permission set for " + playerName), false);
                                            } else {
                                                context.getSource().sendFailure(Component.literal("Player not found"));
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
                                                context.getSource().sendSuccess(() -> Component.literal("Permission removed for " + playerName), false);
                                            } else {
                                                context.getSource().sendFailure(Component.literal("Player not found"));
                                            }
                                            return 1;
                                        })))));
    }
}