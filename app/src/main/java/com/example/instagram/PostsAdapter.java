package com.example.instagram;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.fragments.DetailsFragment;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvCaption, tvTime;
        private ImageView ivPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            tvTime = itemView.findViewById(R.id.tvTime);

        }

        public void bind(final Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvCaption.setText(post.getCaption());
            tvTime.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));

            ParseFile picture = post.getPicture();
            if (picture != null)
                Glide.with(context).load(picture.getUrl()).into(ivPicture);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                    DetailsFragment details = DetailsFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("post", post);
                    details.setArguments(bundle);
                    details.show(fm, "DetailsFragment");
                }
            });
        }

        public String getRelativeTimeAgo(String rawJsonDate) {
            String format = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
            sf.setLenient(true);
            String relativeDate = "";
            try {
                Date dateMillis = sf.parse(rawJsonDate);
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis.getTime(),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (relativeDate.charAt(1) == ' ')
                return "" + relativeDate.charAt(0) + relativeDate.charAt(2);
            else if (relativeDate.charAt(2) == ' ')
                return "" + relativeDate.substring(0, 2) + relativeDate.charAt(3);
            else
                return relativeDate;
        }
    }
}
