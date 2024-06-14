package com.sabanciuniv.activityforecast;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {

    private List<Activity> activities;
    private Context context;
    private String location;
    private ExecutorService srv;

    public ActivitiesAdapter(Context context, List<Activity> activities, String location) {
        this.context = context;
        this.activities = activities;
        this.location = location;
        this.srv = ((ActivityForecast) ((android.app.Activity) context).getApplication()).srv; // Initialize the ExecutorService
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity activity = activities.get(position);
        holder.bind(activity);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textViewTitle;
        private ImageView imageView;
        private Activity activity;
        private Handler uiHandler;

        ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);

            uiHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    return true;
                }
            });
        }

        void bind(Activity activity) {
            this.activity = activity;
            textViewTitle.setText(activity.getName());
            srv.submit(() -> {
                WebConnection.downloadImage(uiHandler, activity.getPhoto());
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ActivityDetailActivity.class);
            intent.putExtra("title", activity.getName());
            intent.putExtra("description", activity.getDetails());
            intent.putExtra("imageUrl", activity.getPhoto());
            intent.putExtra("type", activity.getType());
            intent.putExtra("location", location);
            context.startActivity(intent);
        }
    }
}
