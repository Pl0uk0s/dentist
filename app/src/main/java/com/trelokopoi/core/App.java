package com.trelokopoi.core;

import android.app.Application;
import android.content.Context;

public class App extends Application{

    public static final String TAG = "Core";
    public static String VERSION = "";
    public static Context contextOfApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        contextOfApplication = getApplicationContext();
    }

    public static Context getContextOfApplication(){

        return App.contextOfApplication;
    }


}
