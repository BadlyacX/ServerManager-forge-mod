package com.badlyac.servermanager.permission;

import com.badlyac.servermanager.utils.Msg;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandReloadRank {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("reloadRank")
                .executes(context -> {
                    PermissionManager.loadRankPermissions();
                    Msg.sendColoredMsgToPlayer(context.getSource(), "Ranks reloaded successfully", ChatFormatting.YELLOW);
                    return 1;
                }));
    }
}
