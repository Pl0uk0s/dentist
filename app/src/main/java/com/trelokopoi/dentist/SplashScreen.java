package com.trelokopoi.dentist;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.google.android.gms.analytics.Tracker;
import com.trelokopoi.dentist.util.ActivityLoader;
import com.trelokopoi.dentist.util.AsyncApiCall;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.NoInternetDialog;
import com.trelokopoi.dentist.util.Tools;
import com.trelokopoi.dentist.util.LocalStorage;
import com.trelokopoi.dentist.util.WebApi;
import com.trelokopoi.dentist.util.WebInterface;

import org.json.JSONArray;
import org.json.JSONObject;

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
                new AsyncApiCall(LOAD_CHILDREN, SplashScreen.this, false).execute(WebApi.getChidren());
            }
            else {
                new NoInternetDialog(SplashScreen.this)
                {
                    public void run()
                    {

                        return;
                    }
                };
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
        JSONObject jsonResult = WebInterface.validateJSON(SplashScreen.this, result);

        if (jsonResult != null) {
            if (thread == LOAD_CHILDREN) {

                String success = jsonResult.optString("success", "0");

                if (success.equals("1")) {
                    JSONArray children = jsonResult.optJSONArray("children");
                    LocalStorage.setChildren(children);

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String currentDate = df.format(c.getTime());

                    LocalStorage.setDayForInfo(currentDate);

                    ActivityLoader.load(SplashScreen.this, ActivityLoader.act1);
                }
                else {
                    Tools.toast(getApplicationContext(), "Sorry, an error occurred. Please reopen the app.");
                }
            }
        }
    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {
        // TODO Auto-generated method stub

    }
}
