package com.badlyac.servermanager.openenderchest;

import com.badlyac.servermanager.Main;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Main.MODID)
public class OpenEnderChest {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("openEnderChest")
                .executes(context -> openEnderChest(context.getSource()))
                        .executes(context -> openEnderChest(context.getSource())));
        dispatcher.register(Commands.literal("oec")
                .executes(context -> openEnderChest(context.getSource()))
                .executes(context -> openEnderChest(context.getSource())));
    }

    private static int openEnderChest(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            player.openMenu(new SimpleMenuProvider(
                    (id, inventory, playerEntity) -> ChestMenu.threeRows(id, inventory, player.getEnderChestInventory()),
                    Component.literal("Ender Chest")
            ));
            return 1;
        } else {
            source.sendFailure(Component.literal("This command can only be used by players."));
            return 0;
        }
    }
}

