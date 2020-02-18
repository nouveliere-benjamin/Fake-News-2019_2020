package com.safetweet;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TweetView_old extends LinearLayout {
	public TweetView_old(Context ctx, Tweet_old tweet) {
		super(ctx);
		this.setOrientation(HORIZONTAL);
		TextView child;
		child = new TextView(ctx);
		child.setText(tweet.getUserName());
		this.addView(child);
		child = new TextView(ctx);
		child.setText(tweet.getContent());
		this.addView(child);
	}
}
