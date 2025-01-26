package com.badlyac.servermanager.maxnodamageticks;

import com.badlyac.servermanager.Main;
import com.badlyac.servermanager.utils.Msg;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Main.MODID)
public class FastDamage {

    private static final String CONFIG_FILE = "ticks.json";
    private static boolean useDefaultTicks = false;
    private static final int CUSTOM_TICKS = 0;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        File file = new File(CONFIG_FILE);
        try {
            if (!file.exists()) {
                Gson gson = new Gson();
                JsonObject json = new JsonObject();
                json.addProperty("useDefaultTicks", true);

                try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                    gson.toJson(json, writer);
                }
            } else {
                try (FileReader reader = new FileReader(CONFIG_FILE)) {
                    Gson gson = new Gson();
                    JsonObject json = gson.fromJson(reader, JsonObject.class);
                    useDefaultTicks = json.get("useDefaultTicks").getAsBoolean();
                }
            }
        } catch (IOException e) {
            Msg.printStackTrace(e);
        }
    }

    private static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            Gson gson = new Gson();
            JsonObject json = new JsonObject();
            json.addProperty("useDefaultTicks", useDefaultTicks);
            gson.toJson(json, writer);
        } catch (IOException e) {
            System.out.println("Error saving config: " + e.getMessage());
        }
    }

    @SubscribeEvent
    public static void onEntityAttack(LivingAttackEvent event) {
        if (!useDefaultTicks) {
            event.getEntity().invulnerableTime = CUSTOM_TICKS;
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        registerCommands(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        loadConfig();
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fastdamage")
                .then(Commands.literal("default")
                        .executes(context -> {
                            useDefaultTicks = true;
                            saveConfig();
                            context.getSource().sendSuccess(() -> Component.literal("Using default invulnerable ticks (20 ticks)"), true);
                            System.out.println("Using default invulnerable ticks (20 ticks)");
                            return Command.SINGLE_SUCCESS;
                        }))
                .executes(context -> {
                    useDefaultTicks = false;
                    saveConfig();
                    context.getSource().sendSuccess(() -> Component.literal("Ticks set to fastDamage (0 ticks)"), true);
                    System.out.println("Ticks set to custom (0 ticks)");
                    return Command.SINGLE_SUCCESS;
                }));
    }
}
