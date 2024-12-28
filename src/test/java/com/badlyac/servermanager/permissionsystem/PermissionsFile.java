package com.badlyac.servermanager.permissionsystem;

import com.badlyac.servermanager.utils.Msg;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PermissionsFile {
    private static final File PERMISSIONS_FILE = new File("permissions.json");
    public static JsonObject permissionsData;
    public static Map<String, JsonObject> playerPermissions = new HashMap<>();

    public static void initPermissionsFile() {
        try {
            if (!PERMISSIONS_FILE.exists()) {
                PERMISSIONS_FILE.createNewFile();
            }

            FileReader reader = new FileReader(PERMISSIONS_FILE);
            permissionsData = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();
        } catch (IOException e) {
            Msg.printStackTrace(e);
        }
    }

    public static void setPlayerPermission(String playerId, String permission, boolean value) {
        JsonObject playerData = permissionsData.has(playerId) ? permissionsData.getAsJsonObject(playerId) : new JsonObject();
        playerData.addProperty(permission, value);

        permissionsData.add(playerId, playerData);
        savePermissions();
    }

    public static void loadPlayerPermissions(String playerId) {
        if (permissionsData != null && permissionsData.has(playerId)) {
            JsonObject playerData = permissionsData.getAsJsonObject(playerId);
            playerPermissions.put(playerId, playerData);
        } else {
            playerPermissions.put(playerId, new JsonObject());
        }
    }

    public static boolean hasPermission(String playerId, String permission) {
        JsonObject playerData = playerPermissions.get(playerId);
        if (playerData == null) {
            return false;
        }

        return playerData.has(permission) && playerData.get(permission).getAsBoolean();
    }

    public static void savePermissions() {
        try (FileWriter writer = new FileWriter(PERMISSIONS_FILE)) {
            writer.write(permissionsData.toString());
        } catch (IOException e) {
            Msg.printStackTrace(e);
        }
    }
}
