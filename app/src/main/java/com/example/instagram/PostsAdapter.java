package com.example.instagram;

import android.content.Context;
import android.os.Bundle;
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

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts){
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

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUsername, tvCaption;
        private ImageView ivPicture;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            ivPicture = itemView.findViewById(R.id.ivPicture);

        }

        public void bind(final Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvCaption.setText(post.getCaption());
            ParseFile picture = post.getPicture();
            if (picture!=null)
                Glide.with(context).load(picture.getUrl()).into(ivPicture);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    FragmentManager fm= ((AppCompatActivity)context).getSupportFragmentManager();
                    DetailsFragment details = DetailsFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("post",post);
                    details.setArguments(bundle);
                    details.show(fm, "DetailsFragment");
                }
            });
        }
    }
}
