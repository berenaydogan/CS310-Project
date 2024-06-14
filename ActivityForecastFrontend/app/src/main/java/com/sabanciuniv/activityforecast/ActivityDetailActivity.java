package com.sabanciuniv.activityforecast;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sabanciuniv.activityforecast.databinding.ActivityDetailBinding;

import java.util.concurrent.ExecutorService;


public class ActivityDetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;

    Handler imgHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            binding.imageViewActivity.setImageBitmap((Bitmap)msg.obj);
            return true;

        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ExecutorService srv = ((ActivityForecast)getApplication()).srv;

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String type = getIntent().getStringExtra("type");
        String location = getIntent().getStringExtra("location");

        binding.textViewTitle.setText(title);
        binding.textViewDescription.setText(description);

        srv.submit(() -> {
            WebConnection.downloadImage(imgHandler, imageUrl);
        });

        binding.buttonShowReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDetailActivity.this, ReviewDisplayActivity.class);
                intent.putExtra("activityTitle", title);  // Pass the activity title to the ReviewDisplayActivity
                intent.putExtra("type", type);
                intent.putExtra("location", location);

                startActivity(intent);
            }
        });
    }
}
