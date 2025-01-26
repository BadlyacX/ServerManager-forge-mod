package com.badlyac.servermanager.permission.blocks;

import com.badlyac.servermanager.Main;
import com.badlyac.servermanager.permission.PermissionManager;
import com.badlyac.servermanager.utils.Msg;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class CommandBlockHandler {
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {

        ServerPlayer player = (ServerPlayer) event.getEntity();
        Block clickedBlock = event.getLevel().getBlockState(event.getPos()).getBlock();
        if (isCommandBlock(clickedBlock)) {
            if (!PermissionManager.hasPerm(player, "minecraft.blocks.commandblock") && !PermissionManager.hasPerm(player, "minecraft.blocks.super")) {
                Msg.sendColoredMsgToPlayer(player, "You do not have permission to use commandblock!", ChatFormatting.RED);
                player.connection.disconnect(Component.literal("You do not have permission to use commandblock!").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
            }
        }
    }

    private static boolean isCommandBlock(Block block) {
        return block == Blocks.COMMAND_BLOCK ||
                block == Blocks.CHAIN_COMMAND_BLOCK ||
                block == Blocks.REPEATING_COMMAND_BLOCK;
    }
}
