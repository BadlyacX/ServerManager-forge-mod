//package com.badlyac.servermanager.utils.DeprecatedChestGui;
//
//import com.badlyac.servermanager.Main;
//import net.minecraft.world.inventory.MenuType;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;
//import net.minecraftforge.common.extensions.IForgeMenuType;
//
//public class ModMenus {
//    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Main.MODID);
//
//    public static final RegistryObject<MenuType<PlayerChestMenu>> MOD_CHEST_MENU = MENUS.register("mod_chest_menu",
//            () -> IForgeMenuType.create((windowId, inv, data) -> new PlayerChestMenu(windowId, inv,null)));
//}
