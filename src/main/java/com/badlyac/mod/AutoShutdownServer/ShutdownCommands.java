package com.badlyac.mod.AutoShutdownServer;


import com.badlyac.mod.Main;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class ShutdownCommands {

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        ShutdownMod.getInstance().setup(event);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("setShutdownTimer")
                .then(Commands.argument("minutes", IntegerArgumentType.integer(1))
                        .executes(context -> {
                            int minutes = IntegerArgumentType.getInteger(context, "minutes");
                            MinecraftServer server = context.getSource().getServer();
                            ShutdownMod.setShutdownTimer(minutes, server);
                            return 1;
                        })));

        dispatcher.register(Commands.literal("cancelShutdownTask")
                .executes(context -> {
                    ShutdownMod.cancelShutdownTask();
                    return 1;
                }));
    }
}
