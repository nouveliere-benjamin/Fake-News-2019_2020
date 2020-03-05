package com.safetweet;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;
import retrofit2.Call;

import java.util.LinkedList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {
	private int nbLoadedTweets = 0;
	private LinearLayout layout = null;
	private ProgressBar progressBar = null;
	private Button loadBtn = null;
	private TextView acceuilText = null;
	private ScrollView scrollView = null;
	private String userName;
	private int maxTweetsLoad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Twitter.initialize(this);
		setContentView(R.layout.activity_main_page);
		layout = findViewById(R.id.tweetLayout);
		progressBar = findViewById(R.id.progressBar);
		loadBtn = findViewById(R.id.loadMoreBtn);
		loadBtn.setVisibility(View.INVISIBLE);
		loadBtn.setOnClickListener(v -> {
			loadBtn.setVisibility(View.INVISIBLE);
			Toast.makeText(MainPageActivity.this, getResources().getText(R.string.tweets_loading_message), Toast.LENGTH_LONG).show();
			loadTweets();
		});
		scrollView = findViewById(R.id.scrollView);
		acceuilText = findViewById(R.id.homeTextView);
		acceuilText.setOnClickListener(v -> {
			scrollView.fullScroll(View.FOCUS_UP);
			progressBar.setVisibility(View.VISIBLE);
			Toast.makeText(MainPageActivity.this, getResources().getText(R.string.new_tweets_loading_message), Toast.LENGTH_LONG).show();
			loadNewTweets();
		});
		userName = getIntent().getStringExtra("username");
		maxTweetsLoad = getResources().getInteger(R.integer.max_tweets_load);
		loadTweets();
	}
	
	/**
	 * Charge le tweets récents n'ayant pas encore étaient chargé.
	 * C'est-à-dire, charge les tweets posté entre l'instant présent
	 * et le tweet le plus récent deja chargé (celui qui apparait en premier dans la timeline.
	 * Les nouveaux tweets sont ajouter au debut de la timeline.
	 * Si aucun tweet n'est a charger, informe l'utilisateur via un toast.
	 */
	private void loadNewTweets() {
		int layoutNbChild = layout.getChildCount();
		if(nbLoadedTweets == 0) {
			return;
		}
		Tweet t = ((TweetView) layout.getChildAt(1)).getTweet();
		TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
		Call<List<Tweet>> show = twitterApiClient.getStatusesService().homeTimeline(null, t.getId(), null, null, null, null, null);
		show.enqueue(new Callback<List<Tweet>>() {
			@Override
			public void success(Result<List<Tweet>> result) {
				List<Tweet> tweets = new LinkedList(result.data);
				//message si aucun nouveaux tweets a afficher
				if(tweets.size() == 0) {
					Toast.makeText(MainPageActivity.this, getResources().getText(R.string.no_new_tweets_loading_message), Toast.LENGTH_LONG).show();
					return;
				}
				//affiche les nouveaux tweets
				int idx = 0;
				for (Tweet tweet : tweets) {
					TweetView tv = new TweetView(MainPageActivity.this, tweet);
					layout.addView(tv, idx++);
					nbLoadedTweets++;
				}
				updateView();
			}
			
			@Override
			public void failure(TwitterException exception) {
				Toast.makeText(MainPageActivity.this, getResources().getText(R.string.tweets_loading_error_message), Toast.LENGTH_LONG).show();
				updateView();
			}
			
			private void updateView() {
				progressBar.setVisibility(View.GONE);
			}
		});
	}
	
	/**
	 * Charge de nouveaux tweets.
	 * C'est-à-dire, charge des tweets plus ancien que le dernier (plus ancien) tweet deja chargé.
	 * Le nombre de tweet a chargé est fixé par une constante (maxTweetsLoad).
	 * Les tweets sont affichés à la suite des précédents.
	 */
	private void loadTweets() {
		int layoutNbChild = layout.getChildCount();
		//contiendra l'id du dernier tweet chargé (plus ancien)
		Long maxId = null;
		if (nbLoadedTweets != 0) {
			//recupere le dernier tweet chargé
			//attention -> le dernier element du layout est le bouton, le premier la progressbar
			Tweet t = ((TweetView) layout.getChildAt(layoutNbChild - 2)).getTweet();
			maxId = t.getId();
		}
		TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
		//charge un tweet en plus du nombre fixé car le requete retourne aussi le tweet maxId
		Call<List<Tweet>> show = twitterApiClient.getStatusesService().homeTimeline(maxTweetsLoad+1, null, maxId, null, null, null, null);
		show.enqueue(new Callback<List<Tweet>>() {
			@Override
			public void success(Result<List<Tweet>> result) {
				List<Tweet> tweets = new LinkedList(result.data);
				//retire le premier tweet de la liste, apparaitra en double sinon
				tweets.remove(0);
				//affiche les tweets
				int idx = layoutNbChild-1;
				for (Tweet tweet : tweets) {
					TweetView tv = new TweetView(MainPageActivity.this, tweet);
					layout.addView(tv, idx++);
					nbLoadedTweets++;
				}
				updateView();
			}
			
			@Override
			public void failure(TwitterException exception) {
				Toast.makeText(MainPageActivity.this, getResources().getText(R.string.tweets_loading_error_message), Toast.LENGTH_LONG).show();
				updateView();
			}
			
			private void updateView() {
				loadBtn.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
			}
		});
	}
}
