package com.badlyac.servermanager.hidedisplaynameforother;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HideDisplayNameForOther {
    private static boolean isEnabled = false;

    @SubscribeEvent
    public static void onRegisterCommand(RegisterCommandsEvent event) {
        // failed to hide display name
        // registerToggleCommand(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onRenderPlayerName(PlayerEvent.NameFormat event) {
        if (isEnabled && shouldHideDisplayName(event.getEntity(), event.getEntity())) {
            event.setDisplayname(Component.literal(""));
        } else {
            event.setDisplayname(Component.literal(event.getEntity().getName().getString()));
        }
    }

    private static boolean shouldHideDisplayName(net.minecraft.world.entity.player.Player player, net.minecraft.world.entity.player.Player targetPlayer) {
        return !player.getUUID().equals(targetPlayer.getUUID());
    }

    private static void registerToggleCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("toggleHideDisplayNameForOther")
                .requires(source -> source.hasPermission(2))
                .executes(context -> {
                    isEnabled = !isEnabled;
                    Component message = isEnabled ? Component.literal("Enabled").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)) : Component.literal("Disabled").setStyle(Style.EMPTY).withStyle(ChatFormatting.RED);
                    context.getSource().sendSuccess(() ->message, true);
                    return 1;
                }));
    }
}
