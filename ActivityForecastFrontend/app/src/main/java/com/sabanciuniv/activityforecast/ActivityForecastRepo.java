package com.sabanciuniv.activityforecast;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ActivityForecastRepo {

    public static void activitiesByLocAndType(String location, String type, Handler uiHandler, ExecutorService srv) {
        srv.submit(() -> {
            List<Activity> activities = new ArrayList<>();
            String buffer = WebConnection.fetchData("http://10.0.2.2:8080/ActivityForecast/weather/" + location + "/activities/" + type);

            try {
                JSONArray arr = new JSONArray(buffer.toString());

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject current = arr.getJSONObject(i);


                    Activity activity = new Activity(
                            current.getString("id"),
                            current.getString("icon"),
                            current.getString("type"),
                            current.getString("name"),
                            current.getString("photo"),
                            current.getString("details")
                    );

                    activities.add(activity);

                }

            } catch (JSONException e) {
                Log.e("DEV", e.getMessage());
            }

            Message msg = new Message();
            msg.obj = activities;
            uiHandler.sendMessage(msg);
        });
    }

    public static void locNames(Handler uiHandler, ExecutorService srv) {
        srv.submit(() -> {
            List<Location> locations = new ArrayList<>();
            String buffer = WebConnection.fetchData("http://10.0.2.2:8080/ActivityForecast/locations");

            try {
                JSONArray arr = new JSONArray(buffer);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject current = arr.getJSONObject(i);

                    Weather weather = new Weather(
                            current.getJSONObject("weather").getString("id"),
                            current.getJSONObject("weather").getDouble("temperature"),
                            current.getJSONObject("weather").getString("description"),
                            current.getJSONObject("weather").getString("icon")
                    );

                    Location location = new Location(
                            current.getString("id"),
                            current.getString("name"),
                            weather
                    );

                    locations.add(location);
                }

            } catch (JSONException e) {
                Log.e("DEV", e.getMessage());
            }

            List<String> locationNames = new ArrayList<>();
            for (Location location : locations) {
                locationNames.add(location.getName());
            }

            Message msg = Message.obtain();
            msg.obj = locationNames;
            uiHandler.sendMessage(msg);
        });
    }

    public static void weatherByLoc(String location, Handler imgHandler, Handler textHandler, ExecutorService srv) {
        srv.submit(() -> {
            String buffer = WebConnection.fetchData("http://10.0.2.2:8080/ActivityForecast/weather/" + location);
            try {

                JSONObject current = new JSONObject(buffer);

                Weather weather = new Weather(
                        current.getString("id"),
                        current.getDouble("temperature"),
                        current.getString("description"),
                        current.getString("icon")
                );

                int temperatureCelsius = (int) (weather.getTemperature() - 273.15);
                String temperatureString = Integer.toString(temperatureCelsius) + "°C";

                Message msg = new Message();
                msg.obj = temperatureString;
                textHandler.sendMessage(msg);

                String imageUrl = "https://openweathermap.org/img/wn/" + weather.getIcon() + "@2x.png";

                srv.submit(() -> {
                    WebConnection.downloadImage(imgHandler, imageUrl);
                });


            } catch (JSONException e) {
                Log.e("DEV", e.getMessage());
            }
        });

    }

    public static void reviewsByLocationAndActivity(String location, String type, String title, Handler uiHandler, ExecutorService srv) {
        srv.submit(() -> {
            List<Review> reviews = new ArrayList<>();
            String buffer = WebConnection.fetchData("http://10.0.2.2:8080/ActivityForecast/weather/" + location + "/activities/" + type + "/" + title + "/reviews");

            try {
                JSONArray arr = new JSONArray(buffer);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject current = arr.getJSONObject(i);


                    Activity activity = new Activity(
                            current.getJSONObject("activity").getString("id"),
                            current.getJSONObject("activity").getString("icon"),
                            current.getJSONObject("activity").getString("type"),
                            current.getJSONObject("activity").getString("name"),
                            current.getJSONObject("activity").getString("photo"),
                            current.getJSONObject("activity").getString("details")
                    );


                    Review review = new Review(
                            current.getString("id"),
                            current.getString("username"),
                            current.getString("content"),
                            activity
                    );

                    reviews.add(review);

                }

            }
            catch (JSONException e) {
                Log.e("DEV", e.getMessage());
            }

            Message msg = new Message();
            msg.obj = reviews;
            uiHandler.sendMessage(msg);
        });
    }

    public static void postReview(String location, String type, String title, String name, String review, Handler toastHandler, ExecutorService srv) {
        Message msg = new Message();
        srv.execute(()->{
            try {
                URL url = new URL("http://10.0.2.2:8080/ActivityForecast/weather/" + location + "/activities/" + type + "/" + title + "/reviews/post");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();


                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String params = "username=" + name + "&content=" + review;
                conn.setFixedLengthStreamingMode(params.getBytes().length);

                BufferedOutputStream writer = new BufferedOutputStream(conn.getOutputStream());

                writer.write(params.getBytes());
                writer.flush();

                int responseCode = conn.getResponseCode();
                Log.d("PostReview", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    msg.obj = "Review posted successfully.";

                }
                else {
                    msg.obj = "Failed to post review.";
                }
            }
            catch (Exception e) {
                Log.e("PostReview", "Error posting review", e);
                msg.obj = "Error: " + e.getMessage();
            }

            toastHandler.sendMessage(msg);

        });
    }
}
