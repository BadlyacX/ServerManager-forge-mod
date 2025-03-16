//package com.badlyac.servermanager.utils.DeprecatedChestGui;
//
//import com.badlyac.servermanager.Main;
//import net.minecraft.core.Direction;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.CapabilityManager;
//import net.minecraftforge.common.capabilities.CapabilityToken;
//import net.minecraftforge.common.capabilities.ICapabilityProvider;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.event.AttachCapabilitiesEvent;
//import net.minecraftforge.event.entity.player.PlayerEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import org.jetbrains.annotations.NotNull;
//
//public class ModCapabilities {
//    public static Capability<PlayerChestCapability> PLAYER_CHEST = CapabilityManager.get(new CapabilityToken<>() {});
//
//    @SubscribeEvent
//    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
//        if (event.getObject() instanceof Player) {
//            event.addCapability(new ResourceLocation(Main.MODID, "player_chest"), new ICapabilityProvider() {
//                private final PlayerChestCapability backend = new PlayerChestCapability();
//                private final LazyOptional<PlayerChestCapability> optional = LazyOptional.of(() -> backend);
//
//                @Override @NotNull
//                public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
//                    return cap == PLAYER_CHEST ? optional.cast() : LazyOptional.empty();
//                }
//            });
//        }
//    }
//
//    @SubscribeEvent
//    public static void onPlayerClone(PlayerEvent.Clone event) {
//        event.getOriginal().getCapability(PLAYER_CHEST).ifPresent(oldCap ->
//                event.getEntity().getCapability(PLAYER_CHEST).ifPresent(newCap ->
//                        newCap.getInventory().deserializeNBT(oldCap.getInventory().serializeNBT())));
//    }
//}
