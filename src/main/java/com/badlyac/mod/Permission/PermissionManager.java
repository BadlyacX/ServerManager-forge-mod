package com.badlyac.mod.Permission;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PermissionManager {
    private static final Map<String, Boolean> permissions = new HashMap<>();
    private static final File FILE = new File("config/permissions.json");
    private static final Gson GSON = new Gson();

    static {
        loadPermissions();
    }

    public static void setPermission(ServerPlayer player, String permission, boolean value) {
        permissions.put(player.getUUID() + "." + permission, value);
        savePermissions();
    }

    public static void removePermission(ServerPlayer player, String permission) {
        permissions.remove(player.getUUID() + "." + permission);
        savePermissions();
    }

    public static boolean hasPermission(ServerPlayer player, String permission) {
        return permissions.getOrDefault(player.getUUID() + "." + permission, false);
    }

    public static void savePermissions() {
        try {
            File parentDir = FILE.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                System.out.println("Failed to create directory: " + parentDir.getAbsolutePath());
                return;
            }

            try (FileWriter writer = new FileWriter(FILE)) {
                GSON.toJson(permissions, writer);
                System.out.println("Permissions saved to " + FILE.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error saving permissions: " + e.getMessage());
        }
    }

    public static void loadPermissions() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                Type type = new TypeToken<Map<String, Boolean>>() {}.getType();
                Map<String, Boolean> loadPermissions = GSON.fromJson(reader, type);
                if (loadPermissions != null) {
                    permissions.putAll(loadPermissions);
                }
                System.out.println("Permissions loaded from " + FILE.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Error loading permissions: " + e.getMessage());
            }
        } else {
            System.out.println("Permissions file does not exist: " + FILE.getAbsolutePath());
        }
    }
}
