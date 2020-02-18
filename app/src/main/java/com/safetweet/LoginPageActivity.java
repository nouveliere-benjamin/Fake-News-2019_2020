package com.safetweet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
	private EditText userNameEntry = null;
	private EditText passwordEntry = null;
	private TextView clickableTxtHelp = null;
	private TextView clickableTxtForgotPass = null;
	private TextView clickableTxtWho = null;
	private Button loginBtn = null;
	private static final String TWITTER_KEY = "Your Key";
	private static final String TWITTER_SECRET = "Your Secret";
	private TwitterLoginButton loginButton = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//		Fabric.with(this, new Twitter(authConfig));
		Twitter.initialize(this);
		setContentView(R.layout.activity_login_page);
		
		userNameEntry = findViewById(R.id.userNameEntry);
		passwordEntry = findViewById(R.id.passwordEntry);
		clickableTxtHelp = findViewById(R.id.loginPageHelp);
		clickableTxtForgotPass = findViewById(R.id.loginPageForgotPassword);
		clickableTxtWho = findViewById(R.id.loginPageWho);
		loginBtn = findViewById(R.id.loginPageBtn);
		
		clickableTxtHelp.setOnClickListener((v) -> {
			Intent newActivity = new Intent(this, AboutPageActivity.class);
			startActivity(newActivity);
		});
		
		loginBtn.setOnClickListener((v) -> login());
		
		
		loginButton = findViewById(R.id.twitter_login_button);
		loginButton.setCallback(new Callback<TwitterSession>() {
			@Override
			public void success(Result<TwitterSession> result) {
                /*
                  This provides TwitterSession as a result
                  This will execute when the authentication is successful
                 */
				TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
				TwitterAuthToken authToken = session.getAuthToken();
				String token = authToken.token;
				String secret = authToken.secret;
				
				//Calling login method and passing twitter session
				login(session);
			}
			
			@Override
			public void failure(TwitterException exception) {
				//Displaying Toast message
				Toast.makeText(LoginPageActivity.this, "Authentication failed!", Toast.LENGTH_LONG).show();
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
		
		// Pass the activity result to the login button.
		loginButton.onActivityResult(requestCode, resultCode, data);
	}
	
	private void login() {
		Intent newActivity = new Intent(this, MainPageActivity.class);
		startActivity(newActivity);
	}
}
