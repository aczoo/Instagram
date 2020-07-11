package com.example.instagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Headers;

import static android.app.Activity.RESULT_OK;

public class DetailsFragment extends DialogFragment {
    public static final String TAG = "DialogFragment";
    private TextView tvUsername, tvCaption, tvTimeStamp, tvLikes;
    private ImageView ivPicture, ivLike;
    private int pos;


    public DetailsFragment() {
    }

    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        return inflater.inflate(R.layout.fragment_details, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvCaption = view.findViewById(R.id.tvCaption);
        tvTimeStamp = view.findViewById(R.id.tvTime);
        ivPicture = view.findViewById(R.id.ivPicture);
        ivLike = view.findViewById(R.id.ivLike);
        tvLikes = view.findViewById(R.id.tvLikes);
        for (String key:getArguments().keySet())
        {
            Log.i (TAG, key + " is a key in the bundle");
        }
        final Post post = getArguments().getParcelable("post");
        pos = getArguments().getInt("pos",0);
        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getCaption());
        tvTimeStamp.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
        tvLikes.setText(String.valueOf(post.getNumLikes()));

        if (isLiked(post)){
            ivLike.setImageResource(R.drawable.ic_vector_heart);}
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (isLiked(post)) {
                    ivLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                    post.unlike();
                } else {
                    ivLike.setImageResource(R.drawable.ic_vector_heart);
                    post.like();}
                tvLikes.setText(String.valueOf(post.getNumLikes()));
                post.saveInBackground();
            }

        });


        ParseFile picture = post.getPicture();
        if (picture != null)
            Glide.with(getContext()).load(picture.getUrl()).into(ivPicture);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String format = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        String detailedformat = "h:mm a d MMM yy";
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
        SimpleDateFormat sf2 = new SimpleDateFormat(detailedformat, Locale.ENGLISH);
        sf.setLenient(true);
        try {
            Date dateMillis = sf.parse(rawJsonDate);
            return sf2.format(dateMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    public boolean isLiked(Post post) {
        JSONArray jsonArray = post.getLikes();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                if (jsonArray.getJSONObject(i).getString("objectId").equals(ParseUser.getCurrentUser().getObjectId())) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }



}