package com.example.instagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Headers;

import static android.app.Activity.RESULT_OK;

public class DetailsFragment extends DialogFragment {
    private TextView tvUsername, tvCaption, tvTimeStamp;
    private ImageView ivPicture;

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
        Post post = getArguments().getParcelable("post");

        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getCaption());
        tvTimeStamp.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));

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


}