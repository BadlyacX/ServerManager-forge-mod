package com.badlyac.servermanager.specialweapon.config;

import com.badlyac.servermanager.utils.Msg;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SpecialWeaponConfig {

    private static final String CONFIG_FILE_NAME = "SpecialWeaponConfig.json";
    private static boolean enableSpecialWeapon = false;
    private static final Gson GSON = new Gson();

    public SpecialWeaponConfig() {
        loadConfig();
    }

    public static boolean isEnableSpecialWeapon() {
        return enableSpecialWeapon;
    }

    public void setEnableSpecialWeapon() {
        saveConfig();
    }

    public static void setToDisable() {
        enableSpecialWeapon = false;
        saveConfig();
        Msg.println("Special weapon has been disabled.");
    }

    public static void setToEnable() {
        enableSpecialWeapon = true;
        saveConfig();
        Msg.println("Special weapon has been enabled.");
    }

    private void loadConfig() {
        File configFile = new File(CONFIG_FILE_NAME);
        if (!configFile.exists()) {
            saveConfig();
            return;
        }

        try (Reader reader = Files.newBufferedReader(Paths.get(CONFIG_FILE_NAME))) {
            HashMap data = GSON.fromJson(reader, HashMap.class);
            if (data != null && data.containsKey("EnableSpecialWeapon")) {
                enableSpecialWeapon = (Boolean) data.get("EnableSpecialWeapon");
            }
        } catch (IOException | JsonSyntaxException e) {
            Msg.printStackTrace(e);
        }
    }

    private static void saveConfig() {
        Map<String, Object> data = new HashMap<>();
        data.put("EnableSpecialWeapon", enableSpecialWeapon);

        try (Writer writer = Files.newBufferedWriter(Paths.get(CONFIG_FILE_NAME))) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            Msg.printStackTrace(e);
        }
    }

}
