package com.samer.waveformapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		Thread background = new Thread() {
            public void run() {
                try {
                    sleep(3000); // Thread will sleep for 3 seconds
                     
                   Intent i=new Intent(getBaseContext(), AudioActivity.class);
                   startActivity(i);
                     
                    finish();
                } catch (Exception e) {
                }
            }
        };
        background.start();

	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
