package com.badlyac.servermanager.getsupercrossbow;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
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

@Mod.EventBusSubscriber
public class GetSuperCrossbowCommand {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("getsupercrossbow")
                        .executes(context -> giveSuperCrossbow(context.getSource().getPlayerOrException()))
        );

        dispatcher.register(
                Commands.literal("getscb")
                        .executes(context -> giveSuperCrossbow(context.getSource().getPlayerOrException()))
        );
    }

    private static int giveSuperCrossbow(ServerPlayer player) {
        ItemStack crossbow = new ItemStack(Items.CROSSBOW);

        CompoundTag tag = crossbow.getOrCreateTag();
        CompoundTag displayTag = new CompoundTag();

        displayTag.putString("Name", Component.Serializer.toJson(Component.literal("Super Crossbow")));
        tag.put("display", displayTag);

        CompoundTag enchantments = new CompoundTag();
        enchantments.putString("id", "quick_charge");
        enchantments.putInt("lvl", 5);
        crossbow.addTagElement("Enchantments", enchantments);

        CompoundTag multishot = new CompoundTag();
        multishot.putString("id", "multishot");
        multishot.putInt("lvl", 1);
        crossbow.addTagElement("Enchantments", multishot);

        crossbow.getOrCreateTag().putBoolean("Unbreakable", true);

        if (player.getInventory().add(crossbow)) {
            player.sendSystemMessage(Component.literal("You have received a Super Crossbow!"));
        } else {
            player.sendSystemMessage(Component.literal("Your inventory is full!"));
        }

        return Command.SINGLE_SUCCESS;
    }
}