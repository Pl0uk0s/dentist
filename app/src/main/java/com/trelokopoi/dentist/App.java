package com.trelokopoi.dentist;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.trelokopoi.dentist.util.L;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import java.util.HashMap;

public class App extends Application{

    public static final String TAG = "Core";
    public static String VERSION = "";
    public static int VERSION_ID = 0;
    public static Context contextOfApplication;
    private static final String PROPERTY_ID = "UA-82120732-1";
    public static String username = "";
    public static String password = "";
    public static String userId = "";

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

            analytics.enableAutoActivityReports(this);



            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker("") : null; //R.xml.global_tracker

            //Tracker t = analytics.newTracker(R.xml.global_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.debug("onCreate App");
        contextOfApplication = getApplicationContext();

        PackageInfo pinfo;
        try {
            pinfo = contextOfApplication.getPackageManager().getPackageInfo(contextOfApplication.getPackageName(), 0);
            //VERSION = "a"+pinfo.versionName;
            VERSION = contextOfApplication.getString(R.string.versionBackend)+pinfo.versionName; // a-t- / a-m-
            VERSION_ID = pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Fabric.with(this, new Crashlytics());
    }

    public static Context getContextOfApplication(){

        return App.contextOfApplication;
    }


}
