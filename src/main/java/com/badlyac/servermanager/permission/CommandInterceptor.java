package com.badlyac.servermanager.permission;

import com.badlyac.servermanager.Main;
import com.badlyac.servermanager.utils.Msg;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class CommandInterceptor {

    @SubscribeEvent
    public static void onCommandEvent(CommandEvent event) {
        CommandSourceStack source = event.getParseResults().getContext().getSource();

        if (source.getEntity() instanceof Player) {
            ServerPlayer player = (ServerPlayer) source.getEntity();
            if (player == null) return;

            String command = event.getParseResults().getReader().getString().split(" ")[0];
            String permission = "minecraft.commands." + command;

            if (!PermissionManager.hasPerm(player, "minecraft.commands.super") && !PermissionManager.hasPerm(player, permission)) {
                Msg.sendNoPermissionMessage(player);
                event.setCanceled(true);
            }
        }
    }
}
