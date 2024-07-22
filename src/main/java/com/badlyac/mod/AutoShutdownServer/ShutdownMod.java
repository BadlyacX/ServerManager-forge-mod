package com.badlyac.mod.AutoShutdownServer;

import com.badlyac.mod.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class ShutdownMod {
    private static Timer shutdownTimer;
    private static TimerTask shutdownTask;
    private static ShutdownMod instance;

    public ShutdownMod() {
        instance = this;
    }

    public static ShutdownMod getInstance() {
        return instance;
    }

    public void setup(final FMLCommonSetupEvent event) {
        shutdownTimer = new Timer();
        Main.info("Shutdown timer initialized.");
    }

    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {
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
            Main.info("Shutdown timer was null and has been reinitialized.");
        }
    }

    public static void saveDataAndStopServer(MinecraftServer server, boolean pSuppressLog, boolean pFlush, boolean pForced) {
        server.saveAllChunks(pSuppressLog, pFlush, pForced);
        server.stopServer();
        Main.info("Server has been stopped.");
    }
}
