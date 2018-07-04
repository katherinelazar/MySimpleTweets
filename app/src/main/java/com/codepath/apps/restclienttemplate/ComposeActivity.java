package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    ProgressBar pb;

    TwitterClient client;

    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        pb = (ProgressBar) findViewById(R.id.pbLoading);
        client = TwitterApp.getRestClient(this);

        editText = (EditText) findViewById(R.id.editText);
    }


    public void composeTweet(View view) {
        pb.setVisibility(ProgressBar.VISIBLE);

        TwitterApp.getRestClient(this);
        client.sendTweet(editText.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }
        } catch (JSONException e){

        }
    

    });




}
