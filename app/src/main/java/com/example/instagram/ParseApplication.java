package com.example.instagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("instagram-aczhu") // should correspond to APP_ID env variable
                .clientKey("123Veggie")
                .server("https://instagram-aczhu.herokuapp.com/parse/").build());
    }
}
