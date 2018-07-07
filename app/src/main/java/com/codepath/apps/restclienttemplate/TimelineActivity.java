package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    private final int REQUEST_CODE = 20;

    private SwipeRefreshLayout swipeContainer;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#74bef7")));
        return true;
    }

    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {



            // Extract name value from result extras
            Tweet name = Parcels.unwrap(data.getParcelableExtra("tweet"));
            // Toast the name to display temporarily on screen
            tweets.add(0, name);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
    }

    public void launchComposeView() {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.miCompose) {

            launchComposeView();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        // find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        // init the arrayList
        tweets = new ArrayList<>();

        // construct adapter from data source
        tweetAdapter = new TweetAdapter(tweets);

        //recyclerView setup
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        // set the adapter
        rvTweets.setAdapter(tweetAdapter);

        populateTimeline();

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                swipeContainer.setRefreshing(false);
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }


    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweetAdapter.clear();
//                // ...the data has come back, add new items to your adapter...
                tweets.clear();
//                Log.d("TwitterClient", response.toString());
                // iterate through json array, deserialize json
                for (int i = 0; i < response.length(); i++) {
                    // convert each object to tweet model

                    // add tweet model to data source
                    // notify the adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
//            @Override
//            public void onSuccess{
//                // Remember to CLEAR OUT old items before appending in the new ones
//
//                for (int i = 0; i < json.length(); i++) {
//                    try {
//                        tweets.add(Tweet.fromJSON(json.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

//                tweetAdapter.addAll(newTweets);


                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Log.d("TwitterClient", response.toString());
                // iterate through json array, deserialize json
                for (int i = 0; i < response.length(); i++) {
                    // convert each object to tweet model

                    // add tweet model to data source
                    // notify the adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
