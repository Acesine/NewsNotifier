package com.xianggao.newsnotifier.page;

import java.util.regex.Pattern;

/**
 * Created by Acesine on 12/8/15.
 */
public class Patterns {
    private Patterns() {}

    private static String URL_PATTERN_STRING = "(https?):\\/\\/(www\\.)?[a-z0-9\\.:].*?(?=[\\s\"<'\\\\>,;()])";
    public static Pattern URL_PATTERN = Pattern.compile(URL_PATTERN_STRING);
}
