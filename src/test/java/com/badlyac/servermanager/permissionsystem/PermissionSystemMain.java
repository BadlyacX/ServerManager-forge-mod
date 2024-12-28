package com.badlyac.servermanager.permissionsystem;

import com.badlyac.servermanager.Main;
import com.badlyac.servermanager.permissionsystem.commands.Permission;
import com.badlyac.servermanager.permissionsystem.commands.Rank;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class PermissionSystemMain {

    public static void initializePermissionSystem() {
        PermissionsFile.initPermissionsFile();
        RanksFile.initRanksFile();
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getServer().getCommands().getDispatcher();
        Permission.register(commandDispatcher);
        Rank.register(commandDispatcher);
    }

    @SubscribeEvent
    public static void onFMLCommonSetup(FMLCommonSetupEvent event) {
        initializePermissionSystem();
    }
}
