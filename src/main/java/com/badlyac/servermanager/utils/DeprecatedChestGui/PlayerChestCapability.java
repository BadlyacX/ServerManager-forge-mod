//package com.badlyac.servermanager.utils.DeprecatedChestGui;
//
//import net.minecraft.nbt.CompoundTag;
//import net.minecraftforge.items.ItemStackHandler;
//
//public class PlayerChestCapability {
//    private final ItemStackHandler inventory = new ItemStackHandler(54);
//
//    public ItemStackHandler getInventory() {
//        return inventory;
//    }
//
//    // save data
//    public void saveNBT(CompoundTag nbt) {
//        nbt.put("Inventory", inventory.serializeNBT());
//    }
//
//    // load data
//    public void loadNBT(CompoundTag nbt) {
//        inventory.deserializeNBT(nbt.getCompound("Inventory"));
//    }
//}
