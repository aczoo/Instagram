package com.example.instagram.models;

import android.util.Log;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_CAPTION = "caption";
    public static final String KEY_USER = "user";
    public static final String KEY_PICTURE = "picture";
    public static final String KEY_CREATED = "createdAt";
    public static final String KEY_LIKES = "likes";


    public String getCaption(){
        return getString(KEY_CAPTION);
    }
    public void setCaption(String caption){
        put(KEY_CAPTION, caption);
    }
    public ParseFile getPicture(){
        return getParseFile(KEY_PICTURE);
    }
    public void setPicture(ParseFile parseFile){
        put(KEY_PICTURE, parseFile);

    }
    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser parseUser){
        put(KEY_USER, parseUser);
    }

    public JSONArray getLikes() {
        JSONArray jsonArray = getJSONArray(KEY_LIKES);
        if (jsonArray == null) {
            return new JSONArray();
        }
        else {
            return jsonArray;
        }
    }
    public void like() {
        ParseUser user = ParseUser.getCurrentUser();
        add(KEY_LIKES, user);
    }

    public void unlike() {
        ParseUser user = ParseUser.getCurrentUser();
        ArrayList<ParseUser> users = new ArrayList<ParseUser>();
        users.add(user);
        removeAll(KEY_LIKES, users);
    }
    public int getNumLikes() {
        return getLikes().length();
    }


}
