package com.badlyac.servermanager;

import net.minecraftforge.fml.common.Mod;

import java.util.logging.Logger;

@Mod(Main.MODID)
public class Main {
    public static final String MODID = "servermanager";
    private static final Logger LOGGER = Logger.getLogger(MODID);

    public static void info(String s) {
        LOGGER.info(s);
    }


}

