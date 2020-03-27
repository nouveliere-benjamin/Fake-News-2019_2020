package com.safetweet;

import android.os.StrictMode;

import androidx.annotation.Nullable;

import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CustomTweet {
	@Nullable
	private EnumTweetEval eval = null;
	private Tweet tweet;

	private final OkHttpClient client = new OkHttpClient();
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public CustomTweet(Tweet tweet) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		this.tweet = tweet;
		this.eval = evalTweet(tweet.text);

	}

	private EnumTweetEval evalTweet(String tweet){
		String url = "http://18.222.213.247";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tweet", tweet);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String json = jsonObject.toString();
		String resp = "rien";
		try {
			resp = this.post(url, json);
		} catch (IOException e) {
			e.printStackTrace();
		}

		float nb = Float.parseFloat(resp.substring(9,15));

		if(nb > 0.95 ){
			return EnumTweetEval.DANGER;
		}else if(nb > 0.7){
			return EnumTweetEval.WARN;
		}else{
			return EnumTweetEval.SAFE;
		}
	}

	private String post(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(JSON, json); // new
		// RequestBody body = RequestBody.create(JSON, json); // old
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		Response response = client.newCall(request).execute();
		return response.body().string();
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
