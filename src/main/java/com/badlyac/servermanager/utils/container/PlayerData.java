package com.badlyac.servermanager.utils.container;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record PlayerData(String name, ResourceKey<Level> dimension, double x, double y, double z, float yaw,
                         float pitch) {

    @Override
    public String toString() {
        return "PlayerData{" +
                "name='" + name + '\'' +
                ", dimension=" + dimension +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                '}';
    }
}
