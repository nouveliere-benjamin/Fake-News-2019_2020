package com.safetweet;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;

//import com.twitter.sdk.android.core.models.Tweet;

public class MainPageActivity extends AppCompatActivity {
	private int nbLoadedTweets = 0;
	private LinearLayout layout = null;
	private ProgressBar progressBar = null;
	private Button loadBtn = null;
	private TextView acceuilText = null;
	private ScrollView scrollView = null;
	private String userName;
	private int maxTweetsLoad;
	private EnumTweetEval[] filterChoice = null;
	
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
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					filterTweets(filterChoice);
				}
			}, 1000);

		});
		scrollView = findViewById(R.id.scrollView);
		acceuilText = findViewById(R.id.homeTextView);
		acceuilText.setOnClickListener(v -> {
			scrollView.fullScroll(View.FOCUS_UP);
			progressBar.setVisibility(View.VISIBLE);
			Toast.makeText(MainPageActivity.this, getResources().getText(R.string.new_tweets_loading_message), Toast.LENGTH_LONG).show();
			loadNewTweets();
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					filterTweets(filterChoice);
					progressBar.setVisibility(View.GONE);
				}
			}, 1000);

		});
		userName = getIntent().getStringExtra("username");
		maxTweetsLoad = getResources().getInteger(R.integer.max_tweets_load);
		loadTweets();
		
		//Menu déroulant de filtrage
		Spinner spinner = findViewById(R.id.menuSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.menu_choices, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				switch (i) {
					case 0:
						filterChoice = null;
						((TextView)view).setText(null);
						spinner.setBackgroundResource(R.drawable.logo);
						break;
					case 1:
						filterChoice = new EnumTweetEval[]{EnumTweetEval.WARN, EnumTweetEval.DANGER};
						((TextView)view).setText(null);
						spinner.setBackgroundResource(R.drawable.logovertentier);
						break;
					case 2:
						finish();
					default:
						filterChoice = null;
						((TextView)view).setText(null);
						spinner.setBackgroundResource(R.drawable.logo);
				}
				filterTweets(filterChoice);

			}
			
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
			
			}
		});
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
		if (nbLoadedTweets == 0) {
			return;
		}
		Tweet t = null;
		try{
			t = (Tweet) ((TweetView) layout.getChildAt(1)).getTweet();
		}catch (ClassCastException e){
			t = (Tweet) ((TweetView) layout.getChildAt(0)).getTweet();
		}


		TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
		Call<List<Tweet>> show = twitterApiClient.getStatusesService().homeTimeline(null, t.getId(), null, null, null, null, null);
		show.enqueue(new Callback<List<Tweet>>() {
			@Override
			public void success(Result<List<Tweet>> result) {
				List<Tweet> tweets = new LinkedList(result.data);
				//message si aucun nouveaux tweets a afficher
				if (tweets.size() == 0) {
					Toast.makeText(MainPageActivity.this, getResources().getText(R.string.no_new_tweets_loading_message), Toast.LENGTH_LONG).show();
					return;
				}
				//affiche les nouveaux tweets
				int idx = 0;
				for (Tweet tweet : tweets) {
					CustomTweet ct = new CustomTweet(tweet);
					TweetView tv = new CustomTweetView(MainPageActivity.this, ct);
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
		Call<List<Tweet>> show = twitterApiClient.getStatusesService().homeTimeline(maxTweetsLoad + 1, null, maxId, null, null, null, null);
		show.enqueue(new Callback<List<Tweet>>() {
			@Override
			public void success(Result<List<Tweet>> result) {
				List<Tweet> tweets = new LinkedList(result.data);
				//retire le premier tweet de la liste, apparaitra en double sinon
				tweets.remove(0);
				//affiche les tweets
				int idx = layoutNbChild - 1;
				for (Tweet tweet : tweets) {
					CustomTweet ct = new CustomTweet(tweet);
					TweetView tv = new CustomTweetView(MainPageActivity.this, ct);
					tv.setClickable(false);
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
	
	private void filterTweets(@Nullable EnumTweetEval... evals) {
		for(int i = 0; i < layout.getChildCount(); i++) {
			View v = layout.getChildAt(i);
			if(v instanceof CustomTweetView) {
				CustomTweetView tv = (CustomTweetView)v;
				EnumTweetEval eval = tv.getCustomTweet().getEval();
				tv.setVisibility(View.VISIBLE);
				if(evals != null && eval != null) {
					for (EnumTweetEval param_eval : evals) {
						if (eval == param_eval) {
							tv.setVisibility(View.GONE);
						}
					}
				}
			}
		}
	}
}