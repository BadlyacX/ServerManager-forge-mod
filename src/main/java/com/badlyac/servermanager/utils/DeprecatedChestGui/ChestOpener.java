//package com.badlyac.servermanager.utils.DeprecatedChestGui;
//
//import net.minecraft.world.MenuProvider;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.network.NetworkHooks;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.network.chat.Component;
//import org.checkerframework.checker.nullness.qual.NonNull;
//import org.jetbrains.annotations.NotNull;
//
//public class ChestOpener {
//    public static void openChest(ServerPlayer player) {
//        player.getCapability(ModCapabilities.PLAYER_CHEST).ifPresent(cap -> {
//            NetworkHooks.openScreen(player, new MenuProvider() {
//                @Override @NonNull
//                public Component getDisplayName() {
//                    return Component.literal("Large Chest");
//                }
//
//                @Override
//                public PlayerChestMenu createMenu(int id,@NotNull  Inventory inv,@NotNull  Player playerEntity) {
//                    return new PlayerChestMenu(id, inv, cap.getInventory());
//                }
//            });
//        });
//    }
//}
