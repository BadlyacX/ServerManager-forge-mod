//package com.badlyac.servermanager.utils.DeprecatedChestGui;
//
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.items.IItemHandler;
//import net.minecraftforge.items.SlotItemHandler;
//import org.jetbrains.annotations.NotNull;
//
//public class PlayerChestMenu extends AbstractContainerMenu {
//    private final IItemHandler handler;
//
//    public PlayerChestMenu(int id, Inventory playerInventory, IItemHandler handler) {
//        super(ModMenus.MOD_CHEST_MENU.get(), id);
//        this.handler = handler;
//
//        // Chest slots (6 rows x 9 columns = 54 slots)
//        int slotIndex = 0;
//        for (int row = 0; row < 6; row++) {
//            for (int col = 0; col < 9; col++) {
//                this.addSlot(new SlotItemHandler(handler, slotIndex++, 8 + col * 18, 18 + row * 18));
//            }
//        }
//
//        // Player inventory
//        for (int row = 0; row < 3; row++) {
//            for (int col = 0; col < 9; col++) {
//                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
//            }
//        }
//
//        // Hotbar
//        for (int col = 0; col < 9; col++) {
//            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 198));
//        }
//    }
//
//    @Override @NotNull
//    public ItemStack quickMoveStack(@NotNull Player player, int index) {
//        // Shift-click handling, optional
//        return ItemStack.EMPTY;
//    }
//
//    @Override
//    public boolean stillValid(@NotNull Player player) {
//        return true;
//    }
//}
