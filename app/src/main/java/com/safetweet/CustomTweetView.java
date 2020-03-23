package com.safetweet;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.twitter.sdk.android.core.models.Tweet;

public class CustomTweetView extends com.twitter.sdk.android.tweetui.TweetView {
	private CustomTweet tweet;
	
	public CustomTweetView(Context context, CustomTweet tweet) {
		super(context, tweet.getTweet(), R.style.tw__TweetDarkWithActionsStyle);
		this.tweet = tweet;
		EnumTweetEval eval = tweet.getEval();
		if(eval != null) {
			ConstraintLayout layout = new ConstraintLayout(context);
			ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layout.setLayoutParams(p);
			
			ImageView v = new ImageView(context);
			ConstraintLayout.LayoutParams p1 = new ConstraintLayout.LayoutParams(getResources().getInteger(R.integer.max_icon_size), getResources().getInteger(R.integer.max_icon_size));
			p1.rightToRight = 0;
			p1.topToTop = 0;
			p1.rightMargin = 150;
			p1.topMargin = 20;
			v.setLayoutParams(p1);
			
			layout.addView(v);
			switch (eval) {
				case SAFE:
					v.setImageResource(R.drawable.icon_valide);
					break;
				case WARN:
					v.setImageResource(R.drawable.icon_exclamation_mark);
					break;
				case DANGER:
					v.setImageResource(R.drawable.icon_hand_sign);
					break;
			}
			this.addView(layout);
		}
	}
	
	public CustomTweet getCustomTweet() {
		return tweet;
	}
}
