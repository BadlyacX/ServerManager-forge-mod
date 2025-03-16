//package com.badlyac.servermanager.DeprecatedChest;
//
//import com.badlyac.servermanager.Main;
//import com.badlyac.servermanager.utils.DeprecatedChestGui.ChestOpener;
//import com.badlyac.servermanager.utils.Msg;
//import com.mojang.brigadier.CommandDispatcher;
//import net.minecraft.ChatFormatting;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraftforge.event.RegisterCommandsEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = Main.MODID)
//public class Chest {
//
//    @SubscribeEvent
//    public static void onRegisterCommands(RegisterCommandsEvent event) {
//        register(event.getDispatcher());
//    }
//
//    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
//        dispatcher.register(Commands.literal("chest")
//                .executes(context -> {
//                    ServerPlayer player = context.getSource().getPlayerOrException();
//                    openChestGui(player);
//                    return 1;
//                }));
//    }
//
//    private static void openChestGui(ServerPlayer player) {
//        ChestOpener.openChest(player);
//        Msg.sendColoredMsgToPlayer(player, "chest opened", ChatFormatting.GREEN);
//    }
//}
