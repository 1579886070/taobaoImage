package com.example.image.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * @date 2020/2/22 18:24
 */
public class commonUtils {
    /**
     * 特殊符号转码 utf-8
     *
     * @param str
     * @return
     */
    public static String encodeUrlComponent(String str) {
        String result = null;

        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            result = str;
        }
        return result;
    }

    public static String splitimage(String str) {
        String img = null;
        Pattern compile = Pattern.compile("//.*.</img>");
        Matcher matcher = compile.matcher(str);
        while (matcher.find()) {
            img = matcher.group();
        }
        if (img == null) {
            return null;
        }
        return img.replace("</img>", "");
    }

    public static String httpUrl(String url) {
        if (StringUtils.isNoneBlank(url)) {
            if (url.contains("http:") || url.contains("https:")) {
                return url;
            } else {
                return "http:" + url;
            }
        }
        return url;
    }
}
