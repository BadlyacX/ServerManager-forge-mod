package com.badlyac.mod.autoshutdownserver;

import com.badlyac.mod.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;

import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class ShutdownMod {
    private static Timer shutdownTimer;
    private static TimerTask shutdownTask;

    public ShutdownMod() {
        setup();
    }

    public void setup() {
        shutdownTimer = new Timer();
        Main.info("Shutdown timer initialized.");
    }

    public static void setShutdownTimer(int minutes, MinecraftServer server) {
        initializeTimerIfNecessary();
        cancelShutdownTask();
        Main.info("Server will shutdown in " + minutes + " minutes");
        shutdownTask = new TimerTask() {
            @Override
            public void run() {
                saveDataAndStopServer(server, false, true, true);
            }
        };

        shutdownTimer.schedule(shutdownTask, minutes * 60 * 1000L);
    }

    public static void cancelShutdownTask() {
        if (shutdownTask != null) {
            shutdownTask.cancel();
            shutdownTask = null;
            Main.info("Shutdown task cancelled.");
        }
    }

    private static void initializeTimerIfNecessary() {
        if (shutdownTimer == null) {
            shutdownTimer = new Timer();
        }
    }

    public static void saveDataAndStopServer(MinecraftServer server, boolean pSuppressLog, boolean pFlush, boolean pForced) {
        server.saveAllChunks(pSuppressLog, pFlush, pForced);
        server.stopServer();
        Main.info("Server has been stopped.");
    }
}
