package com.badlyac.mod.autoshutdownserver;


import com.badlyac.mod.Main;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Main.MODID)
public class ShutdownCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("setShutdownTimer")
                .then(Commands.argument("minutes", IntegerArgumentType.integer(1))
                        .executes(context -> {
                            int minutes = IntegerArgumentType.getInteger(context, "minutes");
                            MinecraftServer server = context.getSource().getServer();
                            ShutdownMod.setShutdownTimer(minutes, server);
                            CommandSourceStack source = context.getSource();
                            if (source.getEntity() instanceof ServerPlayer player) {
                                player.sendSystemMessage(Component.literal("Shutdown timer set for " + minutes + " minutes."));
                            } else {
                                source.sendSuccess(() -> Component.literal("Shutdown timer set for " + minutes + " minutes."), false);
                            }
                            return 1;
                        })));

        dispatcher.register(Commands.literal("cancelShutdownTask")
                .executes(context -> {
                    ShutdownMod.cancelShutdownTask();
                    CommandSourceStack source = context.getSource();
                    if (source.getEntity() instanceof ServerPlayer player) {
                        player.sendSystemMessage(Component.literal("Shutdown task canceled."));
                    } else {
                        source.sendSuccess(() ->Component.literal("Shutdown task canceled."), false);
                    }
                    return 1;
                }));
    }
}
