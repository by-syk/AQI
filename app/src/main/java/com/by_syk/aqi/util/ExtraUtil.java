package com.by_syk.aqi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by By_syk on 2016-07-12.
 */
public class ExtraUtil {
    public static String downloadHtml(String urlStr, String charset) {
        StringBuilder sbHtmlText = new StringBuilder();

        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(4000);
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection
                    .getInputStream(), charset));
            String buffer;
            while ((buffer = bufferedReader.readLine()) != null) {
                sbHtmlText.append(buffer).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sbHtmlText.toString();
    }
}
