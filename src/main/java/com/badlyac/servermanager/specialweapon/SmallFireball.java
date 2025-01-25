package com.badlyac.servermanager.specialweapon;

import com.badlyac.servermanager.Main;
import com.badlyac.servermanager.specialweapon.config.SpecialWeaponConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class SmallFireball {
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!SpecialWeaponConfig.isEnableSpecialWeapon()) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();

        if (event.getItemStack().getItem() == Items.BLAZE_ROD && event.getHand() == InteractionHand.OFF_HAND) {
            if (!player.level().isClientSide()) {
                net.minecraft.world.entity.projectile.SmallFireball smallFireball = new net.minecraft.world.entity.projectile.SmallFireball(
                        player.level(), player, player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z);
                smallFireball.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                player.level().addFreshEntity(smallFireball);
            }
            if (event.getHand() == InteractionHand.MAIN_HAND) return;
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
