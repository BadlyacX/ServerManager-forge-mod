package com.badlyac.servermanager.specialweapon.config;

import com.badlyac.servermanager.utils.Msg;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class SpecialWeaponCommand {

    private static final SpecialWeaponConfig config = new SpecialWeaponConfig();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("specialweapon")
                        .then(Commands.literal("enable")
                                .executes(context -> setEnable(context, true)))
                        .then(Commands.literal("disable")
                                .executes(context -> setEnable(context, false)))
                        .then(Commands.literal("status")
                                .executes(SpecialWeaponCommand::getStatus))
        );
    }

    private static int setEnable(CommandContext<CommandSourceStack> context, boolean enable) {
        config.setEnableSpecialWeapon();
        if (enable) {
            SpecialWeaponConfig.setToEnable();
            Msg.sendColoredMsgToPlayer(context.getSource(), "Special weapon enabled.", ChatFormatting.GREEN);
        } else {
            SpecialWeaponConfig.setToDisable();
            Msg.sendColoredMsgToPlayer(context.getSource(), "Special weapon disabled.", ChatFormatting.RED);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int getStatus(CommandContext<CommandSourceStack> context) {
        boolean isEnabled = SpecialWeaponConfig.isEnableSpecialWeapon();
        String status = isEnabled ? "enabled" : "disabled";
        Msg.sendColoredMsgToPlayer(context.getSource(), "Special weapon is currently " + status + ".", ChatFormatting.GREEN);
        return Command.SINGLE_SUCCESS;
    }
}
