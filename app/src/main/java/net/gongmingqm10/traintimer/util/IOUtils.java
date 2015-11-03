package net.gongmingqm10.traintimer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.client.Response;

public class IOUtils {
    public static String readFromResponse(Response result) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in(), "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String extractTime(String message) {
        int colonIndex = message.indexOf(":");
        if (colonIndex < 0) {
            return "";
        } else {
            return message.substring(colonIndex - 2, colonIndex + 3);
        }
    }
}
