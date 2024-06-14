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

import com.sabanciuniv.activityforecast.databinding.FragmentIndoorActivitiesBinding;


public class IndoorActivitiesFragment extends Fragment {

    FragmentIndoorActivitiesBinding binding;
    //private RecyclerView recyclerView;
    private ActivitiesAdapter adapter;
    private List<Activity> indoorActivities;

    Handler uiHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            List<Activity> activities = (List<Activity>) msg.obj;

            indoorActivities.clear();
            indoorActivities.addAll(activities);
            adapter.notifyDataSetChanged();

            return true;
        }
    });



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIndoorActivitiesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        binding.recyclerViewIndoor.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ExecutorService srv = ((ActivityForecast)getActivity().getApplication()).srv; ;


        String location = requireActivity().getIntent().getStringExtra("location");

        indoorActivities = new ArrayList<>();
        adapter = new ActivitiesAdapter(this.getContext(), indoorActivities, location);
        binding.recyclerViewIndoor.setAdapter(adapter);

        ActivityForecastRepo.activitiesByLocAndType(location, "indoor", uiHandler, srv);
    }
}
