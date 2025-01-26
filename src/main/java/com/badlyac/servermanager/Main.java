package com.badlyac.servermanager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Main.MODID)
public class Main {
    public static final String MODID = "servermanager";
    public static final boolean IS_SERVER_ENVIRONMENT = FMLEnvironment.dist == Dist.DEDICATED_SERVER;

    public void onInitialize() {
        if (!IS_SERVER_ENVIRONMENT) {
            System.out.println("Your mod is running on the client side and will disable all features.");
        }
    }
}

