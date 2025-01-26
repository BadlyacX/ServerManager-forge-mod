package com.badlyac.servermanager.permission;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PermissionManager {
    private static final String PERMISSIONS_FILE_PATH = "permissions.json";
    private static final String RANKS_FILE_PATH = "rank.json";
    private static final Gson gson = new Gson();
    private static final Type PERMISSIONS_TYPE = new TypeToken<Map<UUID, PlayerData>>() {}.getType();
    private static final Type RANKS_TYPE = new TypeToken<Map<String, Set<String>>>() {}.getType();
    private static Map<UUID, PlayerData> permissions = new HashMap<>();
    private static Map<String, Set<String>> rankPermissions = new HashMap<>();

    static {
        loadPermissions();
        loadRankPermissions();
        initializeDefaultRankPermissions();
    }

    public static void setPermission(UUID playerUUID, String permission) {
        PlayerData data = permissions.getOrDefault(playerUUID, new PlayerData("default"));
        data.permissions.add(permission);
        permissions.put(playerUUID, data);
        savePermissions();
    }

    public static void removePermission(UUID playerUUID, String permission) {
        PlayerData data = permissions.getOrDefault(playerUUID, new PlayerData("default"));
        data.permissions.remove(permission);
        permissions.put(playerUUID, data);
        savePermissions();
    }

    public static void setRank(UUID playerUUID, String rank) {
        PlayerData data = new PlayerData(rank);
        permissions.remove(playerUUID);
        data.rank = rank;
        data.permissions.addAll(rankPermissions.getOrDefault(rank, new HashSet<>()));
        permissions.put(playerUUID, data);
        savePermissions();
    }


    public static boolean hasPerm(ServerPlayer player, String permission) {
        PlayerData data = permissions.getOrDefault(player.getUUID(), new PlayerData("default"));
        Set<String> playerPermissions = new HashSet<>(rankPermissions.getOrDefault(data.rank, new HashSet<>()));
        playerPermissions.addAll(data.permissions);
        return playerPermissions.contains(permission);
    }

    public static String getRank(UUID playerUUID) {
        PlayerData data = permissions.getOrDefault(playerUUID, new PlayerData("default"));
        return data.rank;
    }

    public static boolean hasRank(UUID playerUUID) {
        return permissions.containsKey(playerUUID) && permissions.get(playerUUID).rank != null;
    }

    public static void loadPermissions() {
        try (FileReader reader = new FileReader(PERMISSIONS_FILE_PATH)) {
            permissions = gson.fromJson(reader, PERMISSIONS_TYPE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void savePermissions() {
        try (FileWriter writer = new FileWriter(PERMISSIONS_FILE_PATH)) {
            gson.toJson(permissions, writer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadRankPermissions() {
        File file = new File(RANKS_FILE_PATH);
        if (!file.exists()) {
            initializeDefaultRankPermissions();
            saveRankPermissions();
        } else {
            try (FileReader reader = new FileReader(RANKS_FILE_PATH)) {
                rankPermissions = gson.fromJson(reader, RANKS_TYPE);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void saveRankPermissions() {
        try (FileWriter writer = new FileWriter(RANKS_FILE_PATH)) {
            gson.toJson(rankPermissions, writer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void initializeDefaultRankPermissions() {
        Set<String> defaultPermissions = new HashSet<>();
        defaultPermissions.add("minecraft.commands.help");
        defaultPermissions.add("minecraft.commands.list");

        Set<String> adminPermissions = new HashSet<>();
        adminPermissions.add("minecraft.commands.super");
        adminPermissions.add("minecraft.blocks.super");
        adminPermissions.add("minecraft.blocks.commandblock");
        adminPermissions.add("minecraft.blocks.structureblock");
        adminPermissions.addAll(defaultPermissions);

        rankPermissions.put("default", defaultPermissions);
        rankPermissions.put("admin", adminPermissions);
    }

    public static void addRankPermission(String rank, String permission) {
        rankPermissions.computeIfAbsent(rank, k -> new HashSet<>()).add(permission);
        saveRankPermissions();
    }

    public static void removeRankPermission(String rank, String permission) {
        Set<String> permissions = rankPermissions.get(rank);
        if (permissions != null) {
            permissions.remove(permission);
            saveRankPermissions();
        }
    }

    private static class PlayerData {
        String rank;
        Set<String> permissions;

        PlayerData(String rank) {
            this.rank = rank;
            this.permissions = new HashSet<>();
        }
    }
}
