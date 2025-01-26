package com.badlyac.servermanager.permissionsystem;

import com.badlyac.servermanager.utils.Msg;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "servermanager")
public class CommandInterceptor {

    @SubscribeEvent
    public static void onCommandEvent(CommandEvent event) {
        CommandSourceStack source = event.getParseResults().getContext().getSource();

        if (source.getEntity() instanceof ServerPlayer player) {
            String playerId = player.getGameProfile().getId().toString();
            String command = event.getParseResults().getReader().getString().split(" ")[0];
            String permissionName = "command." + command;

            if (!PermissionsFile.hasPermission(playerId, permissionName) && !PermissionsFile.hasPermission(playerId, "command.super")) {
                Msg.sendColoredMsgToPlayer(source,"You do not have permission to use this command.", ChatFormatting.RED);
                event.setCanceled(true);
            }
        }
    }
}
