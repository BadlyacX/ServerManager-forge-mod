package com.badlyac.mod.permission;

import com.badlyac.mod.Main;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class PlayerJoinHandler {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            UUID playerUUID = player.getUUID();

            if (!PermissionSystemManager.hasRank(playerUUID)) {
                PermissionSystemManager.setRank(playerUUID, "default");
            }
        }
    }
}
