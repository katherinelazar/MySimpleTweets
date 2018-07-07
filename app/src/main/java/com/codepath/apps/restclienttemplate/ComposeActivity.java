package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    ProgressBar pb;

    TwitterClient client;

    EditText editText;

    Tweet tweet;

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        pb = (ProgressBar) findViewById(R.id.pbLoading);
        client = TwitterApp.getRestClient(this);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        button.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().trim().length() == 0){
                    button.setEnabled(false);

                } else {
                    button.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length() < 1 || s.toString().trim().length() > 140){
                    button.setEnabled(false);

                } else {
                    button.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }


    public void composeTweet(View view) {
//        pb.setVisibility(ProgressBar.VISIBLE);

//        TwitterApp.getRestClient(this);

        client.sendTweet(editText.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    tweet = Tweet.fromJSON(response);
                    Intent data = new Intent();

                    // pass data back
                    data.putExtra("tweet", Parcels.wrap(tweet));

                    // once activity finishes, return data
                    setResult(RESULT_OK, data);

//                    pb.setVisibility(ProgressBar.INVISIBLE);

                    // close activity and pass data to parent
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("SendTweet", errorResponse.toString());
                pb.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("SendTweet", errorResponse.toString());
                pb.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("SendTweet", responseString);
                pb.setVisibility(ProgressBar.INVISIBLE);
            }
        });


    }
}
