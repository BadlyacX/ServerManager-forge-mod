package com.badlyac.mod.permission;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CommandReloadRank {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("reloadRank")
                .executes(context -> {
                    PermissionManager.loadRankPermissions();
                    context.getSource().sendSuccess(() -> Component.literal("Ranks reloaded successfully"), false);
                    return 1;
                }));
    }
}
