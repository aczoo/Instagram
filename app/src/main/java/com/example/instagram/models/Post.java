package com.example.instagram.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_CAPTION = "caption";
    public static final String KEY_USER = "user";
    public static final String KEY_PICTURE = "picture";
    public static final String KEY_CREATED = "createdAt";
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


}
