package com.badlyac.mod.permission;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

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
                                            context.getSource().sendSuccess(() -> Component.literal("Permission " + permission + " added to rank " + rank), false);
                                            return 1;
                                        }))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("rank", StringArgumentType.string())
                                .then(Commands.argument("permission", StringArgumentType.string())
                                        .executes(context -> {
                                            String rank = StringArgumentType.getString(context, "rank");
                                            String permission = StringArgumentType.getString(context, "permission");
                                            PermissionManager.removeRankPermission(rank, permission);
                                            context.getSource().sendSuccess(() -> Component.literal("Permission " + permission + " removed from rank " + rank), false);
                                            return 1;
                                        })))));
    }
}
