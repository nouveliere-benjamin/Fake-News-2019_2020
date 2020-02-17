package com.safetweet;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TweetView extends LinearLayout {
	public TweetView(Context ctx, Tweet tweet) {
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
