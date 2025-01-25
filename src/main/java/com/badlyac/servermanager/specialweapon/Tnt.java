package com.badlyac.servermanager.specialweapon;

import com.badlyac.servermanager.Main;
import com.badlyac.servermanager.specialweapon.config.SpecialWeaponConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Main.MODID)
public class Tnt {
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!SpecialWeaponConfig.isEnableSpecialWeapon()) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        Level world = player.level();
        ItemStack itemStack = event.getItemStack();

        if (itemStack.getItem() == Items.TNT && event.getHand() == InteractionHand.OFF_HAND) {
            if (!world.isClientSide) {
                Vec3 playerDirection = player.getLookAngle();
                Vec3 playerVelocity = player.getDeltaMovement();
                PrimedTnt tnt = new PrimedTnt(world, player.getX(), player.getY(), player.getZ(), player);
                tnt.setFuse(100);
                Vec3 tntVelocity = playerDirection.scale(2).add(playerVelocity);
                tnt.setDeltaMovement(tntVelocity);
                world.addFreshEntity(tnt);
            }

            if (event.getHand() == InteractionHand.MAIN_HAND) return;
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }


}
