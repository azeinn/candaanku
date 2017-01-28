package com.google.firebase.codelab.friendlychat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.firebase.codelab.friendlychat.MainActivity1;
import com.google.firebase.codelab.friendlychat.database.Utilities;

/**
 * This activity displays a splash page with a skip button.
 * 
 * @author Natacha Gabbamonte 0932340
 * @author Caroline Castonguay-Boisvert 084348
 * 
 */
public class SplashActivity extends Activity {

	Handler handler;
	Runnable newActivity;
	Button skipButton;

	/**
	 * Instantiates a handler to skip the splash page after 6 seconds, if the
	 * user didn't click on the skip button.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// Displays the main page after 6 seconds
		handler = new Handler();
		newActivity = new Runnable() {
			public void run() {
				startActivity(new Intent(SplashActivity.this, MainActivity1.class));
				SplashActivity.this.finish();
			}
		};
		handler.postDelayed(newActivity, 6000);

		// Skip button onClickListener
		skipButton = (Button) findViewById(R.id.buttonSplash);
		skipButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				handler.removeCallbacks(newActivity);
				Utilities.changeButtonOnClick(SplashActivity.this, MainActivity1.class,
						skipButton);
				SplashActivity.this.finish();
			}
		});
	}

}
