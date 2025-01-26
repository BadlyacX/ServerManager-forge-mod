package com.badlyac.servermanager.getfirework;

import com.badlyac.servermanager.utils.Msg;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GetFireworkCommand {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("getfirework")
                .then(Commands.argument("count", IntegerArgumentType.integer(1, 64))
                        .then(Commands.argument("explode", BoolArgumentType.bool())
                                .then(Commands.argument("power", IntegerArgumentType.integer(1, 3))
                                        .then(Commands.argument("duration", IntegerArgumentType.integer(1, 3))
                                                .then(Commands.argument("color", StringArgumentType.string())
                                                        .executes(context -> {
                                                            int count = IntegerArgumentType.getInteger(context, "count");
                                                            boolean explode = BoolArgumentType.getBool(context, "explode");
                                                            int power = IntegerArgumentType.getInteger(context, "power");
                                                            int duration = IntegerArgumentType.getInteger(context, "duration");
                                                            String color = StringArgumentType.getString(context, "color");
                                                            return giveFireworks(context.getSource(), count, explode, power, duration, color);
                                                        })))))));
    }

    private static int giveFireworks(CommandSourceStack source, int count, boolean explode, int power, int duration, String color) {
        if (source.getEntity() instanceof ServerPlayer player) {
            ItemStack fireworkStack = createFireworkStack(count, explode, power, duration, color);
            boolean success = player.getInventory().add(fireworkStack);

            if (success) {
                Msg.sendColoredMsgToPlayer(source, "Added " + count + " fireworks to your inventory.", ChatFormatting.GREEN);
            } else {
                Msg.sendColoredMsgToPlayer(source, "Could not add fireworks to inventory! Make sure you have enough space.", ChatFormatting.RED);
            }

            return success ? 1 : 0;
        } else {
            Msg.sendColoredMsgToPlayer(source, "This command can only be run by a player.", ChatFormatting.RED);
            return 0;
        }
    }

    private static ItemStack createFireworkStack(int count, boolean explode, int power, int duration, String color) {
        ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET, count);
        CompoundTag tag = new CompoundTag();
        CompoundTag fireworkTag = new CompoundTag();

        if (explode) {
            ListTag explosions = new ListTag();

            for (int i = 0; i < power; i++) {
                CompoundTag explosion = new CompoundTag();
                explosion.putByte("Type", (byte) FireworkRocketItem.Shape.LARGE_BALL.ordinal());

                int fireworkColor = getColorFromName(color);
                explosion.putIntArray("Colors", new int[]{fireworkColor});

                explosion.putBoolean("Flicker", true);
                explosion.putBoolean("Trail", true);

                explosions.add(explosion);
            }

            fireworkTag.put("Explosions", explosions);
        }

        fireworkTag.putByte("Flight", (byte) duration);
        tag.put("Fireworks", fireworkTag);

        stack.setTag(tag);
        return stack;
    }

    private static int getColorFromName(String colorName) {
        return switch (colorName.toLowerCase()) {
            case "green" -> 0x00FF00;
            case "purple" -> 0x800080;
            case "blue" -> 0x0000FF;
            case "yellow" -> 0xFFFF00;
            case "white" -> 0xFFFFFF;
            case "black" -> 0x000000;
            default -> 0xFF0000; // Default to red
        };
    }
}
