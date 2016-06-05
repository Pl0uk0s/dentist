package com.trelokopoi.dentist.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.trelokopoi.dentist.App;

public class LocalStorage {

    public static final String PREFS_USER = "prefs_user";

    private static SharedPreferences getPreferences() {
        Context applicationContext = App.getContextOfApplication();
        return applicationContext.getSharedPreferences(PREFS_USER, Context.MODE_PRIVATE);
    }

    public static void setActiveGroup(int group) {

//        L.debug("set active group " + group);
//        SharedPreferences.Editor editor = getPreferences().edit();
//        editor.putInt(PREFS_GROUP_ACTIVE, group);
//        editor.apply();
    }

}
