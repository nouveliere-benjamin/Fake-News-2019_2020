package com.safetweet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainPageActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		
		loadTweets();
	}
	
	private void loadTweets() {
		LinearLayout layout = findViewById(R.id.tweetLayout);
		for (int i = 0; i < 15; i++) {
			
			Tweet t = new Tweet("Toto" + i, "Je suis Toto " + i);
			View v = new TweetView(this, t);
			layout.addView(v);
			Log.d("TweetLayout", t.getUserName());
		}
		
	}
	
	@Override
	public void onBackPressed() {
		Intent newActivity = new Intent(this, LoginPageActivity.class);
		startActivity(newActivity);
	}
}
