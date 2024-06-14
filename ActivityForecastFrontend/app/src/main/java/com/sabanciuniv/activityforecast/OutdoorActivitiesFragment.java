package com.sabanciuniv.activityforecast;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.sabanciuniv.activityforecast.databinding.FragmentOutdoorActivitiesBinding;

public class OutdoorActivitiesFragment extends Fragment {

    FragmentOutdoorActivitiesBinding binding;
    private ActivitiesAdapter adapter;
    private List<Activity> outdoorActivities;

    Handler uiHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            List<Activity> activities = (List<Activity>) msg.obj;

            outdoorActivities.clear();
            outdoorActivities.addAll(activities);
            adapter.notifyDataSetChanged();

            return true;
        }
    });


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOutdoorActivitiesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        binding.recyclerViewOutdoor.setLayoutManager(new LinearLayoutManager(getActivity()));
        ExecutorService srv = ((ActivityForecast) getActivity().getApplication()).srv;
        ;


        String location = requireActivity().getIntent().getStringExtra("location");

        outdoorActivities = new ArrayList<>();
        adapter = new ActivitiesAdapter(this.getContext(), outdoorActivities, location);
        binding.recyclerViewOutdoor.setAdapter(adapter);

        ActivityForecastRepo.activitiesByLocAndType(location, "outdoor", uiHandler, srv);


        return view;
    }
}