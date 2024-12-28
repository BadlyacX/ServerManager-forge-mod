package com.badlyac.servermanager.permission;

import com.badlyac.servermanager.utils.Msg;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandModifyRankPermission {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("rankPermission")
                .then(Commands.literal("add")
                        .then(Commands.argument("rank", StringArgumentType.string())
                                .then(Commands.argument("permission", StringArgumentType.string())
                                        .executes(context -> {
                                            String rank = StringArgumentType.getString(context, "rank");
                                            String permission = StringArgumentType.getString(context, "permission");
                                            PermissionManager.addRankPermission(rank, permission);
                                            Msg.sendColoredMsgToPlayer(context.getSource(),"Permission " + permission + " added to rank " + rank, ChatFormatting.GREEN);
                                            return 1;
                                        }))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("rank", StringArgumentType.string())
                                .then(Commands.argument("permission", StringArgumentType.string())
                                        .executes(context -> {
                                            String rank = StringArgumentType.getString(context, "rank");
                                            String permission = StringArgumentType.getString(context, "permission");
                                            Msg.sendColoredMsgToPlayer(context.getSource(), "Permission " + permission + " removed from rank " + rank, ChatFormatting.GREEN);
                                            PermissionManager.removeRankPermission(rank, permission);
                                            return 1;
                                        })))));
    }
}
