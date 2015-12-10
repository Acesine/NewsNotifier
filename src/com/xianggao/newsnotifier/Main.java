package com.xianggao.newsnotifier;

import com.xianggao.newsnotifier.notifier.Notifier;
import com.xianggao.newsnotifier.thread.ThreadUtils;
import org.joda.time.DateTime;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Acesine on 12/8/15.
 */
public class Main {
    public static void main(String [] args) throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("config.properties");
        properties.load(in);
        in.close();

        final String from = properties.getProperty("source_email");
        final String pwd = properties.getProperty("source_pwd");
        final String [] subscriptions = properties.getProperty("subscriptions").split(", ");

        Set<String> storedUrls = new HashSet<>();
        Notifier.started = DateTime.now();
        while (true) {
            new Thread(new Notifier(storedUrls, subscriptions, from, pwd)).start();
            ThreadUtils.sleepQuietly(1000 * 3600 * 5);
        }

    }
}
