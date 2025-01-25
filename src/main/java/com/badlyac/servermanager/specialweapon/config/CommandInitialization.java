package com.badlyac.servermanager.specialweapon.config;

import com.badlyac.servermanager.Main;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class CommandInitialization {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        SpecialWeaponCommand.register(event.getDispatcher());
    }
}
