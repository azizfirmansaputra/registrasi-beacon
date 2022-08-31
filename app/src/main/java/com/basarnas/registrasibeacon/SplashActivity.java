package com.basarnas.registrasibeacon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.basarnas.registrasibeacon.tools.Preferences;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if(Preferences.isLogin()){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            else{
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

            finishAfterTransition();
        }, 1000);
    }

    @Override
    public void onBackPressed() {}
}