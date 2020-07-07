package com.example.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("instagram-aczhu") // should correspond to APP_ID env variable
                .clientKey("123Veggie")
                .server("https://instagram-aczhu.herokuapp.com/parse/").build());
    }
}
