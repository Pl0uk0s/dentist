package com.trelokopoi.dentist;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.google.android.gms.analytics.Tracker;
import com.trelokopoi.dentist.util.Tools;
import com.trelokopoi.dentist.util.LocalStorage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SplashScreen extends Activity {

    private int SPLASH_TIME_OUT = 2000;

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tools.setupGoogleAnalytics(SplashScreen.this);

        setContentView(R.layout.activity_splash_screen);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = df.format(c.getTime());

        LocalStorage.setDayForInfo(currentDate);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein,
                        R.anim.fadeout);
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
    }
}
