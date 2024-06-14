package com.sabanciuniv.activityforecast;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;

import com.sabanciuniv.activityforecast.databinding.ActivityPostReviewBinding;


public class PostReviewActivity extends AppCompatActivity {

    ActivityPostReviewBinding binding;
    String title;
    String type;
    String location;

    Handler toastHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            String toastText = (String) msg.obj;
            Toast.makeText(PostReviewActivity.this, toastText, Toast.LENGTH_SHORT).show();
            if (toastText.equals("Review posted successfully.")){
                finish();
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPostReviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ExecutorService srv = ((ActivityForecast)getApplication()).srv;

        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        location = getIntent().getStringExtra("location");

       binding.buttonPostReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextName.getText().toString().trim();
                String review = binding.editTextReview.getText().toString().trim();

                if (name.isEmpty() || review.isEmpty()) {
                    Toast.makeText(PostReviewActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityForecastRepo.postReview(location, type, title, name, review, toastHandler, srv);
                }
            }
        });
    }
}
