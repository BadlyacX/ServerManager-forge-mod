package com.badlyac.servermanager.autocopyworldfile;

import com.badlyac.servermanager.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class MinecraftServerWorldBackup {
    public static String sourcePath;
    public static String backupPath;

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        getPathFromJson();
        loadConfig();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        try {
            copyFolder(Paths.get(sourcePath), Paths.get(backupPath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void copyFolder(Path source, Path target) throws IOException {
        Files.walk(source).forEach(sourcePath -> {
            try {
                Path targetPath = target.resolve(source.relativize(sourcePath));

                if (targetPath.toString().isEmpty() || sourcePath.toString().isEmpty()) {
                    System.out.println("sourcePath or targetPath is empty");
                    return;
                }
                System.out.println("sourcePath: " + sourcePath + "\n" + "targetPath: " + targetPath);
                if (!sourcePath.getFileName().toString().equals("session.lock")) {
                    if (Files.isDirectory(sourcePath)) {
                        if (!Files.exists(targetPath)) {
                            Files.createDirectories(targetPath);
                        }
                    } else {
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public static void loadConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File configFile = new File("path.json");

        if (!configFile.exists()) {
            JsonObject defaultConfig = new JsonObject();
            defaultConfig.addProperty("sourcePath", "");
            defaultConfig.addProperty("backupPath", "");

            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(defaultConfig, writer);
                System.out.println("Default path.json created.");

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void getPathFromJson() {
        try ( FileReader reader = new FileReader("path.json")) {
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
           sourcePath = jsonObject.get("sourcePath").getAsString();
           backupPath = jsonObject.get("backupPath").getAsString();
           if (sourcePath.isEmpty() || backupPath.isEmpty()) {
               System.out.println("soucePath or backupPath is empty");
               return;
           }
           System.out.println("sourcePath: " + sourcePath + "\n" + "backupPath: " + backupPath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
