//package com.myproject.firebase.ahmad.candaanku;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.google.android.gms.ads.MobileAds;
//import AsyncDBHelper;
//
///**
// * This activity displays a splash page with a skip button.
// *
// * @author Natacha Gabbamonte 0932340
// * @author Caroline Castonguay-Boisvert 084348
// *
// */
//public class SplashActivity1 extends Activity {
//
//	public static boolean copyDatabase = false;
//	static AsyncDBHelper asyncDBHelper;
//	Handler handler;
//	Runnable newActivity;
//	Button skipButton;
//
//	/**
//	 * Instantiates a handler to skip the splash page after 6 seconds, if the
//	 * user didn't click on the skip button.
//	 */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_splash);
//		asyncDBHelper = new AsyncDBHelper(this);
//		MobileAds.initialize(getApplicationContext(), "ca-app-pub-9783650227413863~1266989033");
//
//		// Displays the main page after 6 seconds
//		handler = new Handler();
//		newActivity = new Runnable() {
//			public void run() {
//				startActivity(new Intent(SplashActivity1.this, MainActivity.class));
//				SplashActivity1.this.finish();
//			}
//		};
//
//		TextView textViewSplash = (TextView) findViewById(R.id.textViewSplash);
//		if (copyDatabase) {
//			handler.postDelayed(newActivity, 6000);
//			textViewSplash.setText("Loading pertama kali. Harap tunggu untuk menyalin data..");
//		}
//		else {
//			handler.postDelayed(newActivity, 1000);
//			textViewSplash.setText("Harap Tunggu..");
//		}
//
//
//	}
//
//}
