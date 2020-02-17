package com.safetweet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	}
	
	private void login() {
		Intent newActivity = new Intent(this, MainPageActivity.class);
		startActivity(newActivity);
	}
}
