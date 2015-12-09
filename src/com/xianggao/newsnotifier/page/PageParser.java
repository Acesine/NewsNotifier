package com.xianggao.newsnotifier.page;

import com.xianggao.newsnotifier.thread.ThreadUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Acesine on 12/8/15.
 */
public class PageParser {
    public static String source = "https://news.ycombinator.com/news";

    private static int MAX_RETRIES = 5;
    private static int MAX_LINES = 20000;

    public static List<String> parsePage() throws MalformedURLException {
        return parsePage(new URL(source));
    }

    public static List<String> parsePage(URL url) {
        String body = "";
        int retries = 0;
        boolean succeeded = false;
        while (!succeeded && retries < MAX_RETRIES) {
            try {
                body = "";
                BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                int lineCount = 0;
                while ((line = input.readLine()) != null && lineCount < MAX_LINES) {
                    body += line + "\n";
                    lineCount ++;
                }
                succeeded = true;
            } catch (IOException e) {
                retries ++;
                ThreadUtils.sleepQuietly(1000);
            }
        }

        Pattern pattern = Patterns.URL_PATTERN;
        Matcher matcher = pattern.matcher(body);
        List<String> urls = new ArrayList<>();
        while (matcher.find()) {
            urls.add(matcher.group());
            //System.out.println("Found url: "+matcher.group());
        }
        return urls;
    }
}
