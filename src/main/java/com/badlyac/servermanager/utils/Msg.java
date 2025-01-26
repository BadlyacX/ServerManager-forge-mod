package com.badlyac.servermanager.utils;

import com.badlyac.servermanager.Main;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import java.util.logging.Logger;

public class Msg {
    private static final Logger LOGGER = Logger.getLogger(Main.MODID);

    public static void sendColoredMsgToPlayer(CommandSourceStack source, String msg, ChatFormatting colors) {
        source.sendSystemMessage(net.minecraft.network.chat.Component.literal(msg).setStyle(Style.EMPTY.withColor(colors)));
    }
    public static void sendColoredMsgToPlayer(ServerPlayer player, String msg, ChatFormatting colors) {
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal(msg).setStyle(Style.EMPTY.withColor(colors)));
    }
    public static void sendNoPermissionMessage(ServerPlayer player) {
        MutableComponent noPermissionMessage = Component.literal("You do not have permission to execute this command.").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
        player.sendSystemMessage(noPermissionMessage);
    }
    public static void printStackTrace(Exception e) {
        System.out.println(e.getMessage());
    }
    public static void println(String s) {
        System.out.println(s);
    }
    public static void info(String s) {
        LOGGER.info(s);
    }
}
