package com.badlyac.servermanager.invisibleitemframe;


import com.badlyac.servermanager.Main;
import com.badlyac.servermanager.utils.Msg;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class InvisibleItemFrame {

    @SubscribeEvent
    public static void onRegisterCommand(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("invisibleitemframe")
                .executes(InvisibleItemFrame::giveInvisibleItemFrame));
        dispatcher.register(Commands.literal("iif")
                .executes(InvisibleItemFrame::giveInvisibleItemFrame));
    }

    private static int giveInvisibleItemFrame(CommandContext<CommandSourceStack> context) {
        try {
            CommandSourceStack source = context.getSource();
            ServerPlayer player = source.getPlayerOrException();

            ItemStack itemFrame = new ItemStack(Items.ITEM_FRAME);
            CompoundTag entityTag = new CompoundTag();
            entityTag.putByte("Invisible", (byte) 1);
            CompoundTag displayTag = new CompoundTag();
            displayTag.put("EntityTag", entityTag);
            itemFrame.setTag(displayTag);

            if (player.addItem(itemFrame)) {
                player.displayClientMessage(Component.literal("You have given an invisible item frame!").withStyle(ChatFormatting.GREEN), false);
            } else {
                player.displayClientMessage(Component.literal("You don't have enough inventory space!").withStyle(ChatFormatting.RED), false);
            }
        } catch (CommandSyntaxException e) {
            Msg.printStackTrace(e);
        }

        return 1;
    }
}
