package com.example.image.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * @author Administrator
 * @date 2020/2/22 13:54
 */
public class DownloadPicFromUtils {

    public static void downloadPicture(String urlList, int i, int area,File file) throws IOException {

        String suffix = urlList.substring(urlList.lastIndexOf("."));
        if (suffix.length() > 4) {
            //处理可能没有扩展名的问题
            suffix = ".jpg";
        }
        String path;
        if (area == 0) {
            path = file + "/" + i + "-轮播图" + UUID.randomUUID().toString() + suffix;
        } else {
            path = file + "/" + i + "-页面图文" + UUID.randomUUID().toString() + suffix;
        }

        URL url = null;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
