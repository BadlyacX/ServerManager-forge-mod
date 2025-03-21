package com.badlyac.servermanager.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * 執行計時器，時間到後執行指定任務。
     * @param delay 延遲時間（毫秒）
     * @param task  要執行的任務
     */
    public void countDownMs(int delay, Runnable task) {
        scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 設置秒級計時器，時間到後執行指定任務。
     * @param seconds 延遲時間（秒）
     * @param task    要執行的任務
     */
    public void countDownSec(int seconds, Runnable task) {
        scheduler.schedule(task, seconds, TimeUnit.SECONDS);
    }

    /**
     * 設置分鐘級計時器，時間到後執行指定任務。
     * @param minutes 延遲時間（分鐘）
     * @param task    要執行的任務
     */
    public void countDownMin(int minutes, Runnable task) {
        scheduler.schedule(task, minutes, TimeUnit.MINUTES);
    }

    /**
     * 設置小時級計時器，時間到後執行指定任務。
     * @param hours 延遲時間（小時）
     * @param task  要執行的任務
     */
    public void countDownHour(int hours, Runnable task) {
        scheduler.schedule(task, hours, TimeUnit.HOURS);
    }

    /**
     * 關閉計時器。
     */
    public void shutdown() {
        scheduler.shutdown();
    }
}
