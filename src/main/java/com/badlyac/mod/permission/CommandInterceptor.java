package com.badlyac.mod.permission;

import com.badlyac.mod.Main;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class CommandInterceptor {

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        if (event.getParseResults().getContext().getSource().getEntity() instanceof ServerPlayer player) {
            String command = event.getParseResults().getReader().getString();

            String[] commandParts = command.split(" ");
            if (commandParts.length < 2) {
                return;
            }

            String commandName = commandParts[0] + "." + commandParts[1];

            if (!PermissionManager.hasPermission(player, "minecraft.commands.super") &&
                    !PermissionManager.hasPermission(player, "minecraft.commands." + commandName)) {
                event.setCanceled(true);
                player.sendSystemMessage(Component.literal("You do not have permission to execute this command."));
            }
        }
    }
}