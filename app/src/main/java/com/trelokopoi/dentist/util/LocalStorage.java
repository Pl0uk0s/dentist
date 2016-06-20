package com.trelokopoi.dentist.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.trelokopoi.dentist.App;

public class LocalStorage {

    public static final String PREFS_USER = "prefs_user";
    public static final String PREFS_DAY_FOR_INFO = "prefs_day_for_info";

    private static SharedPreferences getPreferences() {
        Context applicationContext = App.getContextOfApplication();
        return applicationContext.getSharedPreferences(PREFS_USER, Context.MODE_PRIVATE);
    }

    public static void setDayForInfo(String day) {

        L.debug("set day for info " + day);
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(PREFS_DAY_FOR_INFO, day);
        editor.apply();
    }

    public static String getDayForInfo() {
        L.debug("get day for info");
        return getPreferences().getString(PREFS_DAY_FOR_INFO, "");
    }

}
