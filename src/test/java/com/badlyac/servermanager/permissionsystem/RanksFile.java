package com.badlyac.servermanager.permissionsystem;

import com.badlyac.servermanager.utils.Msg;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RanksFile {
    private static final File RANKS_FILE = new File("ranks.json");
    private static JsonObject ranksData;

    public static void initRanksFile() {
        try {
            if (!RANKS_FILE.exists() || RANKS_FILE.length() == 0) {
                writeDefaultRanks();
            } else {
                FileReader reader = new FileReader(RANKS_FILE);
                ranksData = JsonParser.parseReader(reader).getAsJsonObject();
                reader.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void writeDefaultRanks() {
        ranksData = new JsonObject();

        JsonObject adminPermissions = new JsonObject();
        adminPermissions.addProperty("command.*", true);
        adminPermissions.addProperty("block.commandblock", true);
        ranksData.add("admin", adminPermissions);

        JsonObject defaultPermissions = new JsonObject();
        defaultPermissions.addProperty("command.list", true);
        ranksData.add("default", defaultPermissions);

        saveRanks();
    }

    private static void saveRanks() {
        try (FileWriter writer = new FileWriter(RANKS_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(ranksData, writer);
        } catch (IOException e) {
            Msg.printStackTrace(e);
        }
    }

    public static JsonObject getRankPermissions(String rank) {
        if (ranksData.has(rank)) {
            return ranksData.getAsJsonObject(rank);
        }
        return null;
    }
}
