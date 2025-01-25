package com.badlyac.servermanager.specialweapon;

import com.badlyac.servermanager.Main;
import com.badlyac.servermanager.specialweapon.config.SpecialWeaponConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Main.MODID)
public class DragonFireball {
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!SpecialWeaponConfig.isEnableSpecialWeapon()) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        Level world = player.level();
        ItemStack itemStack = event.getItemStack();

        if (itemStack.getItem() == Items.DRAGON_BREATH && event.getHand() == InteractionHand.OFF_HAND) {
            if (!world.isClientSide) {
                net.minecraft.world.entity.projectile.DragonFireball dragonFireball = new net.minecraft.world.entity.projectile.DragonFireball(world, player, player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z);
                dragonFireball.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                world.addFreshEntity(dragonFireball);

            }
            if (event.getHand() == InteractionHand.MAIN_HAND) return;
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
