package com.edumage.bmstu_enrollee;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    private static final String LAUNCH_STATE = "LAUNCH_STATE";
    private static final String APP_PREFERENCES = "APP_PREFERENCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        // for testing main activity set it to false again
        preferences.edit().putBoolean(LAUNCH_STATE, true).apply();


        if (preferences.getBoolean(LAUNCH_STATE, false)) {

            preferences.edit().putBoolean(LAUNCH_STATE, true).apply();
            // for testing welcome activity set it to false again
            //preferences.edit().putBoolean(LAUNCH_STATE, false).apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
