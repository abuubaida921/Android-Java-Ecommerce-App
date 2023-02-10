package com.abuubaida921.esupermarkt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.abuubaida921.esupermarkt.R;
import com.google.firebase.FirebaseApp;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        FirebaseApp.initializeApp(SplashActivity.this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent mInHome = new Intent(SplashActivity.this, OnBordingActivity.class);
                startActivity(mInHome);
                finish();
            }
        }, 1000);
    }
}