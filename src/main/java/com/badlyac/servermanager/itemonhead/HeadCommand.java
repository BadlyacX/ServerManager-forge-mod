package com.badlyac.servermanager.itemonhead;

import com.badlyac.servermanager.Main;
import com.badlyac.servermanager.utils.Msg;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
                        .then(Commands.argument("itemId", StringArgumentType.string())
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    String itemId = StringArgumentType.getString(context, "itemId");

                                    if ("null".equals(itemId)) {
                                        replaceWithHeldItem(player);
                                    } else {
                                        replaceWithItem(player, itemId);
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

    private static void replaceWithItem(ServerPlayer player, String itemId) {
        ItemStack currentHeadItem = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!currentHeadItem.isEmpty()) {
            if (!isCursedAndNonArmor(currentHeadItem)) {
                player.spawnAtLocation(currentHeadItem);
            }
        }

        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
        if (item != null) {
            ItemStack itemStack = new ItemStack(item);
            itemStack.enchant(net.minecraft.world.item.enchantment.Enchantments.BINDING_CURSE, 1);
            player.setItemSlot(EquipmentSlot.HEAD, itemStack);
            player.getPersistentData().putBoolean("customHeadItem", true);
        } else {
            Msg.sendColoredMsgToPlayer(player, "Item not found: " + itemId, ChatFormatting.RED);
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
                Msg.sendColoredMsgToPlayer(player, "You are not holding any item and you have no item on your head.", ChatFormatting.RED);
            } else {
                Msg.sendColoredMsgToPlayer(player, "You already have an item on your head.", ChatFormatting.RED);
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