package com.safetweet;

import androidx.annotation.Nullable;
import com.twitter.sdk.android.core.models.*;

import java.util.List;

public class CustomTweet {
	@Nullable
	private EnumTweetEval eval = null;
	private Tweet tweet;
	
	public CustomTweet(Tweet tweet) {
		this.tweet = tweet;
		this.eval = Math.random() < 0.5 ? EnumTweetEval.SAFE : (Math.random() < 0.5 ? EnumTweetEval.WARN : EnumTweetEval.DANGER); //null;
	}
	
	@Nullable
	public EnumTweetEval getEval() {
		return eval;
	}
	
	public Tweet getTweet() {
		return tweet;
	}
	
	public void setEval(@Nullable EnumTweetEval eval) {
		this.eval = eval;
	}
}
