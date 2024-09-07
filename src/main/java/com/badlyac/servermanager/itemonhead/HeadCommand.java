package com.badlyac.servermanager.itemonhead;

import com.badlyac.servermanager.Main;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Main.MODID)
public class HeadCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("head")
                        .then(Commands.argument("blockId", StringArgumentType.string())
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    String blockId = StringArgumentType.getString(context, "blockId");

                                    if ("null".equals(blockId)) {
                                        replaceWithHeldItem(player);
                                    } else {
                                        replaceWithBlock(player, blockId);
                                    }
                                    return 1;
                                }))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            replaceWithHeldItem(player);
                            return 1;
                        })
        );
    }

    private static void replaceWithBlock(ServerPlayer player, String blockId) {
        ItemStack currentHeadItem = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!currentHeadItem.isEmpty()) {
            if (!isCursedAndNonArmor(currentHeadItem)) {
                player.spawnAtLocation(currentHeadItem);
            }
        }

        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId));
        if (block != null) {
            ItemStack blockItem = new ItemStack(block.asItem());
            blockItem.enchant(net.minecraft.world.item.enchantment.Enchantments.BINDING_CURSE, 1);
            player.setItemSlot(EquipmentSlot.HEAD, blockItem);
            player.getPersistentData().putBoolean("customHeadItem", true);
        } else {
            player.sendSystemMessage(Component.literal("Block not found: " + blockId));
        }
    }

    private static void replaceWithHeldItem(ServerPlayer player) {
        ItemStack currentHeadItem = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack heldItem = player.getMainHandItem();

        if (!heldItem.isEmpty()) {
            if (!currentHeadItem.isEmpty() && !isCursedAndNonArmor(currentHeadItem)) {
                player.spawnAtLocation(currentHeadItem);
            }
            player.setItemSlot(EquipmentSlot.HEAD, heldItem);
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            player.getPersistentData().putBoolean("customHeadItem", true);
        } else {
            if (currentHeadItem.isEmpty()) {
                player.sendSystemMessage(Component.literal("You are not holding any item and you have no item on your head."));
            } else {
                player.sendSystemMessage(Component.literal("You already have an item on your head."));
            }
        }
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack oldItem = event.getFrom();
            if (event.getSlot() == EquipmentSlot.HEAD && !oldItem.isEmpty()) {
                if (player.getPersistentData().getBoolean("customHeadItem")) {
                    if (isCursedAndNonArmor(oldItem)) {
                        oldItem.setCount(0);
                    }
                    player.getPersistentData().remove("customHeadItem");
                }
            }
        }
    }

    private static boolean isCursedAndNonArmor(ItemStack itemStack) {
        return EnchantmentHelper.hasBindingCurse(itemStack) && !(itemStack.getItem() instanceof ArmorItem);
    }
}
