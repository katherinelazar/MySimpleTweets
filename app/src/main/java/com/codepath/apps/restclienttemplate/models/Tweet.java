package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

    // List out attributes

    public String body;

    public long uid;
    public User user;

    public String createdAt;


    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract values from the JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;
    }
}

