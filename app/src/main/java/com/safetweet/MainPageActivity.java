package com.safetweet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;
import retrofit2.Call;

import java.util.List;

public class MainPageActivity extends AppCompatActivity {
	private LinearLayout layout = null;
	//	private ListView listView = null;
	private Button loadBtn = null;
	private TextView acceuilText = null;
	private ScrollView scrollView = null;
	private String userName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Twitter.initialize(this);
		setContentView(R.layout.activity_main_page);
		layout = findViewById(R.id.tweetLayout);
//		listView = findViewById(R.id.listView);
		loadBtn = findViewById(R.id.loadMoreBtn);
		loadBtn.setOnClickListener(v -> loadTweets());
		scrollView = findViewById(R.id.scrollView);
		acceuilText = findViewById(R.id.homeTextView);
		acceuilText.setOnClickListener(v -> scrollView.fullScroll(View.FOCUS_UP));
		userName = getIntent().getStringExtra("username");
		loadTweets();
	}
	
	private void loadTweets() {
		Toast.makeText(MainPageActivity.this, "Chargement de plus de tweets", Toast.LENGTH_LONG).show();
		int layoutNbChild = layout.getChildCount();
		Long maxId = null;
		if (layoutNbChild > 1) {
			TweetView v = (TweetView) layout.getChildAt(layoutNbChild - 2);
			Tweet t = v.getTweet();
			maxId = t.getId();
		}
		TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
		Call<List<Tweet>> show = twitterApiClient.getStatusesService().homeTimeline(10, null, maxId, null, null, null, null);
		show.enqueue(new Callback<List<Tweet>>() {
			@Override
			public void success(Result<List<Tweet>> result) {
				Toast.makeText(MainPageActivity.this, "Load ok : " + result.data.size(), Toast.LENGTH_LONG).show();
				//Retire le btn de chargement des tweets
				layout.removeViewAt(layoutNbChild - 1);
				for (Tweet tweet : result.data)
					layout.addView(new TweetView(MainPageActivity.this, tweet));
				layout.addView(loadBtn);
			}
			
			@Override
			public void failure(TwitterException exception) {
				Toast.makeText(MainPageActivity.this, "Load pas ok", Toast.LENGTH_LONG).show();
			}
		});


//		Affichage des tweets d'un compte donné (ici forbes)
//		UserTimeline userTimeline = new UserTimeline.Builder().screenName("forbes").build();
//		TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
//				.setTimeline(userTimeline)
//				.build();
//		listView.setAdapter(adapter);


//      Affichage de tweets connaissant leurs ids
//		List<Long> tweetIds = Arrays.asList(1229779625934454784L, 1229779874484752384L);
//		TweetUtils.loadTweets(tweetIds, new Callback<List<Tweet>>() {
//			@Override
//			public void success(Result<List<Tweet>> result) {
//				for(Tweet tweet : result.data)
//					layout.addView(new TweetView(MainPageActivity.this, tweet));
//			}
//
//			@Override
//			public void failure(TwitterException exception) {
//				Toast.makeText(MainPageActivity.this, "Echec du chargement des tweets !", Toast.LENGTH_LONG).show();
//			}
//		});
	}
	
	@Override
	public void onBackPressed() {
		Intent newActivity = new Intent(this, LoginPageActivity.class);
		startActivity(newActivity);
	}
}
