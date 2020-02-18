package com.safetweet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.twitter.sdk.android.core.Twitter;

public class MainPageActivity extends AppCompatActivity {
	Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		Twitter.initialize(ctx);
		setContentView(R.layout.activity_main_page);
		
		loadTweets();
	}
	
	private void loadTweets() {
		LinearLayout layout = findViewById(R.id.tweetLayout);
//		for (int i = 0; i < 15; i++) {
//
//			Tweet_old t = new Tweet_old("Toto" + i, "Je suis Toto " + i);
//			View v = new TweetView(this, t);
//			layout.addView(v);
//			Log.d("TweetLayout", t.getUserName());
//		}

//		final long tweetId = 510908133917487104L;
//		TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
//			@Override
//			public void success(Result<Tweet> result) {
//				layout.addView(new TweetView(ctx, result.data));
//			}
//
//			@Override
//			public void failure(TwitterException exception) {
//				// Toast.makeText(...).show();
//			}
//		});
	}
	
	@Override
	public void onBackPressed() {
		Intent newActivity = new Intent(this, LoginPageActivity.class);
		startActivity(newActivity);
	}
}
