package com.sabanciuniv.activityforecast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class ActivitiesDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_display);

        String location = getIntent().getStringExtra("location");

        Button buttonIndoor = findViewById(R.id.buttonIndoor);
        Button buttonOutdoor = findViewById(R.id.buttonOutdoor);

        buttonIndoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndoorActivitiesFragment frgIndoor = new IndoorActivitiesFragment();
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

                trans.replace(R.id.frameLayout, frgIndoor);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        buttonOutdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutdoorActivitiesFragment frgOutdoor = new OutdoorActivitiesFragment();
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

                trans.replace(R.id.frameLayout, frgOutdoor);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        IndoorActivitiesFragment frgIndoor = new IndoorActivitiesFragment();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

        trans.add(R.id.frameLayout, frgIndoor);
        trans.addToBackStack(null);
        trans.commit();
    }
}
