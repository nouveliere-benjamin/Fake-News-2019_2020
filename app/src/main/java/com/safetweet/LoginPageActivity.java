package com.safetweet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Première page que l'utilisateur vera en ouvrant l'application.
 * L'utilisateur peut se connecter à son compte Twitter.
 */
public class LoginPageActivity extends AppCompatActivity {
	private TextView clickableTxtHelp = null;
	private TextView clickableTxtWho = null;
	private TwitterLoginButton loginButton = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Twitter.initialize(this);
		setContentView(R.layout.activity_login_page);
		
		clickableTxtHelp = findViewById(R.id.loginPageHelp);
		clickableTxtHelp.setOnClickListener((v) -> {
			Intent newActivity = new Intent(this, AboutPageActivity.class);
			startActivity(newActivity);
		});

		loginButton = findViewById(R.id.twitterLoginButton);
		loginButton.setText("Connexion via Twitter");
		loginButton.setCallback(new Callback<TwitterSession>() {
			@Override
			public void success(Result<TwitterSession> result) {
				TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
				TwitterAuthToken authToken = session.getAuthToken();
				String token = authToken.token;
				String secret = authToken.secret;
				
				login(session);
			}
			
			@Override
			public void failure(TwitterException exception) {
				Toast.makeText(LoginPageActivity.this, "Echec d'authentication !", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	public void login(TwitterSession session) {
		String username = session.getUserName();
		Intent intent = new Intent(LoginPageActivity.this, MainPageActivity.class);
		intent.putExtra("username", username);
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loginButton.onActivityResult(requestCode, resultCode, data);
	}
}
