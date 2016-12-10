package com.kieslog;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.kieslog.util.ActivityLoader;
import com.kieslog.util.AsyncApiCallOnTaskCompleted;
import com.kieslog.util.Tools;
import com.kieslog.util.LocalStorage;
import com.kieslog.util.WebInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SplashScreen extends Activity implements AsyncApiCallOnTaskCompleted {

    private int LOAD_CHILDREN = 0;

    private int SPLASH_TIME_OUT = 2000;

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tools.setupGoogleAnalytics(SplashScreen.this);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                checkForLogin();
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

    private void checkForLogin() {
        if (LocalStorage.getUserLogin()) {
            App.userEmail = LocalStorage.getUserEmail();
            App.password = LocalStorage.getUserPassword();
            App.userId = LocalStorage.getUserId();
            if (WebInterface.hasInternetConnection()) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = df.format(c.getTime());

                LocalStorage.setDayForInfo(currentDate);

                ActivityLoader.load(SplashScreen.this, ActivityLoader.act1);
            }
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
            }
        }
        else {
            Intent intent = new Intent(SplashScreen.this, PreLoginActivity.class);
            startActivity(intent);
            finish();
//            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    @Override
    public void onTaskCompleted(int thread, String result) {

    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {
        // TODO Auto-generated method stub

    }
}
