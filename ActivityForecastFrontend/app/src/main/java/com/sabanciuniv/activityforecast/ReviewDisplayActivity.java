package com.sabanciuniv.activityforecast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.sabanciuniv.activityforecast.databinding.ActivityReviewDisplayBinding;

public class ReviewDisplayActivity extends AppCompatActivity {

    ActivityReviewDisplayBinding binding;
    private ReviewsAdapter adapter;
    private List<Review> reviewList;
    private String title;
    private String type;
    private String location;
    Handler uiHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            List<Review> reviews = (List<Review>) msg.obj;
            reviewList.clear();
            reviewList.addAll(reviews);
            adapter.notifyDataSetChanged();
            return true;
        }
    });
    ExecutorService srv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewDisplayBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        srv =((ActivityForecast)getApplication()).srv;

        binding.recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewList = new ArrayList<>();
        adapter = new ReviewsAdapter(reviewList);
        binding.recyclerViewReviews.setAdapter(adapter);

        // Initialize the intent data inside onCreate
        title = getIntent().getStringExtra("activityTitle");
        type = getIntent().getStringExtra("type");
        location = getIntent().getStringExtra("location");

        binding.buttonPostReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewDisplayActivity.this, PostReviewActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("type", type);
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });

        ActivityForecastRepo.reviewsByLocationAndActivity(location, type, title, uiHandler, srv);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ActivityForecastRepo.reviewsByLocationAndActivity(location, type, title, uiHandler, srv);
    }
}
