package com.badlyac.mod.permission;

import com.badlyac.mod.Main;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class PermissionSystemInitialize {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        CommandPermission.register(event.getServer().getCommands().getDispatcher());
        CommandRank.register(event.getServer().getCommands().getDispatcher());
        CommandReloadRank.register(event.getServer().getCommands().getDispatcher());
        CommandModifyRankPermission.register(event.getServer().getCommands().getDispatcher());

        PermissionSystemManager.loadPermissions();
    }
}
