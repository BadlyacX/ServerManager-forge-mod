package com.badlyac.servermanager.permission;

import com.badlyac.servermanager.Main;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class PermissionSystemInitialize {

    @SubscribeEvent
    public static void onRegisterCommand(RegisterCommandsEvent event) {
        CommandPermission.register(event.getDispatcher());
        CommandRank.register(event.getDispatcher());
        CommandReloadRank.register(event.getDispatcher());
        CommandModifyRankPermission.register(event.getDispatcher());

        PermissionManager.loadPermissions();
    }
}
