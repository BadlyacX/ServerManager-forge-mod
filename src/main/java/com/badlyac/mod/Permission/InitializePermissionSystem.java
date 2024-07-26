package com.badlyac.mod.Permission;

import com.badlyac.mod.Main;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Main.MODID)
public class InitializePermissionSystem {
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        PermissionManager.savePermissions();
    }
}
