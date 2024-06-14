package com.sabanciuniv.activityforecast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebConnection {

    public static String fetchData(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            conn.disconnect();
            return buffer.toString();
        }
        catch (MalformedURLException e) {
            Log.e("DEV",e.getMessage());
        }
        catch (IOException e) {
            Log.e("DEV", e.getMessage());
        }

        return null;
    }

    public static void downloadImage(Handler uiHandler, String path){

        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());

            Message msg = new Message();
            msg.obj = bitmap;
            uiHandler.sendMessage(msg);

        }
        catch (MalformedURLException e) {
            Log.e("DEV",e.getMessage());
        }
        catch (IOException e) {
            Log.e("DEV", e.getMessage());
        }

    }

}
