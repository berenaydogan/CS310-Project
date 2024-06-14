package com.sabanciuniv.activityforecast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.os.Message;

import android.graphics.Bitmap;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Handler;



import java.util.concurrent.ExecutorService;

import com.sabanciuniv.activityforecast.databinding.ActivityWeatherDisplayBinding;

public class WeatherDisplayActivity extends AppCompatActivity {

    ActivityWeatherDisplayBinding binding;

    Handler textHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            binding.textViewDegrees.setText((String)msg.obj);
            return true;

        }
    });

    Handler imgHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            binding.imageViewWeather.setImageBitmap((Bitmap)msg.obj);
            return true;

        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWeatherDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ExecutorService srv = ((ActivityForecast)getApplication()).srv;

        String location = getIntent().getStringExtra("location");
        binding.textViewLocation.setText(location);

        srv.submit(() -> {
            String response = WebConnection.fetchData("http://10.0.2.2:8080/ActivityForecast/weather/" + location);
            ActivityForecastRepo.weatherByLoc(location, imgHandler, textHandler, srv);
        });

        binding.btnActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherDisplayActivity.this, ActivitiesDisplayActivity.class);
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });
    }


}
