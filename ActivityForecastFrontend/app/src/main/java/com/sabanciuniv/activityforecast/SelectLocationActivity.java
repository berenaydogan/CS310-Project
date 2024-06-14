package com.sabanciuniv.activityforecast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.Message;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import java.util.logging.Logger;

import com.sabanciuniv.activityforecast.databinding.ActivitySelectLocationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;

public class SelectLocationActivity extends AppCompatActivity {

    ActivitySelectLocationBinding binding;

    Handler uiHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            List<String> locationNames = (List<String>) msg.obj;
            populateLocationSpinner(locationNames);
            return true;
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectLocationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ExecutorService srv = ((ActivityForecast)getApplication()).srv;


        ActivityForecastRepo.locNames(uiHandler, srv);


        binding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = binding.spLocation.getSelectedItem().toString();
                Intent intent = new Intent(SelectLocationActivity.this, WeatherDisplayActivity.class);
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });
    }

    private void populateLocationSpinner(List<String> locationNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spLocation.setAdapter(adapter);
    }
}
