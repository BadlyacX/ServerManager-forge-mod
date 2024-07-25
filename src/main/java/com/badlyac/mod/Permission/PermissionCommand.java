package com.badlyac.mod.Permission;

import com.badlyac.mod.Main;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Main.MODID)
public class PermissionCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("permission")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("set")
                                .then(Commands.argument("permission", StringArgumentType.string())
                                        .then(Commands.argument("value", BoolArgumentType.bool())
                                                .executes(PermissionCommand::setPermission))))
                        .then(Commands.literal("remove")
                                .then(Commands.argument("permission", StringArgumentType.string())
                                        .executes(PermissionCommand::removePermission))))
                .then(Commands.literal("reload")
                        .executes(PermissionCommand::reloadPermissions)));
    }

    private static int setPermission(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String permission = StringArgumentType.getString(context, "permission");
        boolean value = BoolArgumentType.getBool(context, "value");

        PermissionManager.setPermission(player, permission, value);
        context.getSource().sendSuccess(() -> Component.literal("Permission " + permission + " set to " + value + " for player " + player.getName().getString()).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)), true);
        return 1;
    }

    private static int removePermission(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String permission = StringArgumentType.getString(context, "permission");

        PermissionManager.removePermission(player, permission);
        context.getSource().sendSuccess(() -> Component.literal("Permission " + permission + " removed for player " + player.getName().getString()).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
        return 1;
    }

    private static int reloadPermissions(CommandContext<CommandSourceStack> context) {
        PermissionManager.loadPermissions();
        context.getSource().sendSuccess(() -> Component.literal("Permissions reloaded").setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)), true);
        return 1;
    }
}
