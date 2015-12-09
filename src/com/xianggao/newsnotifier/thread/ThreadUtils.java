package com.xianggao.newsnotifier.thread;

/**
 * Created by Acesine on 12/8/15.
 */
public class ThreadUtils {
    private ThreadUtils() {}

    public static void sleepQuietly(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
        }
    }
}
