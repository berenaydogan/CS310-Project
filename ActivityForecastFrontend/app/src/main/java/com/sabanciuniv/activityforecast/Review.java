package com.sabanciuniv.activityforecast;

public class Review {

    private String id;
    private Activity activity;
    private String username;
    private String content;

    public Review(String id, String name, String review, Activity activity) {
        this.id = id;
        username = name;
        content = review;
        this.activity = activity;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }


}