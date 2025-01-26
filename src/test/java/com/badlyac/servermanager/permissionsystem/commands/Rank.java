package com.badlyac.servermanager.permissionsystem.commands;

import com.badlyac.servermanager.permissionsystem.PermissionsFile;
import com.badlyac.servermanager.permissionsystem.RanksFile;
import com.badlyac.servermanager.utils.Msg;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.Commands;

import java.util.Collection;

public class Rank {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("rank")
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.argument("rank", StringArgumentType.word())
                                        .executes(context -> setRank(
                                                context,
                                                StringArgumentType.getString(context, "player"),
                                                StringArgumentType.getString(context, "rank")
                                        )))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .executes(context -> removeRank(
                                        context,
                                        StringArgumentType.getString(context, "player")
                                )))));
    }

    private static int setRank(CommandContext<CommandSourceStack> context, String playerSelector, String rank) {
        try {
            Collection<ServerPlayer> players = getPlayersFromSelector(context, playerSelector);

            JsonObject rankPermissions = RanksFile.getRankPermissions(rank);
            if (rankPermissions == null) {
                Msg.sendColoredMsgToPlayer(context.getSource(), "Rank " + rank + " does not exist.", ChatFormatting.RED);
                return 0;
            }

            assert players != null;
            for (ServerPlayer player : players) {
                String playerId = player.getGameProfile().getId().toString();

                for (String permission : rankPermissions.keySet()) {
                    boolean value = rankPermissions.get(permission).getAsBoolean();
                    PermissionsFile.setPlayerPermission(playerId, permission, value);
                }

                Msg.sendColoredMsgToPlayer(context.getSource(), "Set rank " + rank + " for player " + player.getName().getString(), ChatFormatting.GREEN);
            }
            return 1;
        } catch (Exception e) {
            Msg.sendColoredMsgToPlayer(context.getSource(),"An error occurred while setting the rank: " + e.getMessage(), ChatFormatting.RED);
            Msg.printStackTrace(e);
            return 0;
        }
    }


    private static int removeRank(CommandContext<CommandSourceStack> context, String playerSelector) {
        Collection<ServerPlayer> players = getPlayersFromSelector(context, playerSelector);
        if (players == null) {
            Msg.println("var players is null");
            return 0;
        }
        for (ServerPlayer player : players) {
            String playerId = player.getGameProfile().getId().toString();

            JsonObject rankPermissions = RanksFile.getRankPermissions("default");
            assert rankPermissions != null;
            for (String permission : rankPermissions.keySet()) {
                PermissionsFile.setPlayerPermission(playerId, permission, false);
            }
            Msg.sendColoredMsgToPlayer(context.getSource(), "Removed rank from player " + player.getName().getString(), ChatFormatting.GREEN);
        }

        return 1;
    }

    private static Collection<ServerPlayer> getPlayersFromSelector(CommandContext<CommandSourceStack> context, String playerSelector) {
        try {
            return EntityArgument.getPlayers(context, "player");
        } catch (Exception e) {
            Msg.sendColoredMsgToPlayer(context.getSource(), "Failed to parse player selector: " + playerSelector, ChatFormatting.RED);
            Msg.printStackTrace(e);
            return null;
        }
    }
}
