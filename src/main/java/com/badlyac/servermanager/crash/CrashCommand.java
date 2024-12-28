package com.badlyac.servermanager.crash;

import com.badlyac.servermanager.utils.Msg;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class CrashCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("crash")
                .then(Commands.argument("playerName", StringArgumentType.string())
                        .executes(context -> {
                            String playerName = StringArgumentType.getString(context, "playerName");
                            CommandSourceStack source = context.getSource();
                            ServerPlayer player = source.getServer().getPlayerList().getPlayerByName(playerName);

                            if (player != null) {
                                sendParticles(player);
                                if (source.isPlayer()) {
                                    ServerPlayer sender = source.getPlayerOrException();
                                    Msg.sendColoredMsgToPlayer(sender, "Sent particles to " + playerName, ChatFormatting.GREEN);
                                } else {
                                    Msg.sendColoredMsgToPlayer(source, "Sent particles to " + playerName, ChatFormatting.GREEN);
                                }
                            } else {
                                if (source.isPlayer()) {
                                    ServerPlayer sender = source.getPlayerOrException();
                                    Msg.sendColoredMsgToPlayer(sender, "Player not found", ChatFormatting.RED);
                                } else {
                                    Msg.sendColoredMsgToPlayer(source,"Player not found", ChatFormatting.RED);
                                }
                            }

                            return 1;
                        })));
    }

    private static void sendParticles(ServerPlayer player) {
        Vec3 position = player.position();
        ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(ParticleTypes.HAPPY_VILLAGER, true,
                position.x, position.y + 1.0, position.z, 0.5F, 0.5F, 0.5F, 0.0F, Integer.MAX_VALUE);
        player.connection.send(packet);
    }
}
