package com.safetweet;

public class Tweet {
	private String userName;
	private String content;
	
	
	public Tweet(String userName, String content) {
		this.userName = userName;
		this.content = content;
	}
	
	
	public String getUserName() {
		return userName;
	}
	
	public String getContent() {
		return content;
	}
}
