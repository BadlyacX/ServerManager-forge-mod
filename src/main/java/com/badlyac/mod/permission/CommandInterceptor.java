package com.badlyac.mod.Permission;

import com.badlyac.mod.Main;
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
            ServerPlayer player = (ServerPlayer) source.getPlayer();
            String command = event.getParseResults().getReader().getString();
            String permission = "minecraft.command." + command.split(" ")[0];

            if (player == null) return;
            
            
            if (!PermissionManager.hasPermission(player, "minecraft.command.*") &&
                !PermissionManager.hasPermission(player, permission)) {
                
                MutableComponent noPermissionMessage = Component.literal("You do not have permission to execute this command.")
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
                player.sendSystemMessage(noPermissionMessage);
                event.setCanceled(true);
            }
        }
    }
}