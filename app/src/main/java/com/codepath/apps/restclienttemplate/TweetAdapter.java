package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    //pass in tweets array
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    // for each row inflate layout and pass into ViewHolder class

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    // bind values based on position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get data based on position
        Tweet tweet = mTweets.get(position);

        // populate views
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
    }


    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivProfileImage;

        public TextView tvUsername;

        public TextView tvBody;


        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
        }


    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}