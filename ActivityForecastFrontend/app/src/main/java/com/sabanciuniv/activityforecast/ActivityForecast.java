package com.sabanciuniv.activityforecast;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivityForecast extends Application {

    ExecutorService srv = Executors.newCachedThreadPool();

}
