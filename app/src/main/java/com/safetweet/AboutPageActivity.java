package com.safetweet;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Page informative permettant à l'utilisateur d'en savoir plus sur l'application
 */
public class AboutPageActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_page);
	}
}
