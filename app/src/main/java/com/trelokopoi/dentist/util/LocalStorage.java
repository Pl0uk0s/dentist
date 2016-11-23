package com.trelokopoi.dentist.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.trelokopoi.dentist.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocalStorage {

    public static final String PREFS_USER = "prefs_user";
    public static final String PREFS_DAY_FOR_INFO = "prefs_day_for_info";
    public static final String PREFS_USER_EMAIL = "prefs_user_email";
    public static final String PREFS_USER_PASSWORD = "prefs_user_password";
    public static final String PREFS_USER_ID = "prefs_user_id";
    public static final String PREFS_USER_LOGIN = "prefs_user_login";
    public static final String PREFS_USER_CHILDREN = "prefs_user_children";
    public static final String PREFS_USER_PRODUCTS = "prefs_user_products";

    private static SharedPreferences getPreferences() {
        Context applicationContext = App.getContextOfApplication();
        return applicationContext.getSharedPreferences(PREFS_USER, Context.MODE_PRIVATE);
    }

    public static void setLogin(String userEmail, String password, String userId) {
        L.debug("setLogin " + userEmail + " " +password + " " +userId);

        SharedPreferences.Editor editor = getPreferences().edit();
        editor.clear().apply();
        editor = getPreferences().edit();

        editor.putString(PREFS_USER_EMAIL, userEmail);
        editor.putString(PREFS_USER_PASSWORD, password);
        editor.putString(PREFS_USER_ID, userId);
        editor.apply();
    }

    public static String getUserEmail() {
        return getPreferences().getString(PREFS_USER_EMAIL, "");
    }

    public static String getUserPassword() {
        return getPreferences().getString(PREFS_USER_PASSWORD, "");
    }

    public static String getUserId() {
        return getPreferences().getString(PREFS_USER_ID, "");
    }

    public static void setUserPassword(String password) {
        L.debug("setUserPassword " + password);

        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(PREFS_USER_PASSWORD, password);
        editor.apply();
    }

    public static void setUserLogin(boolean login) {
        L.debug("setUserLogin " + login);

        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(PREFS_USER_LOGIN, login);
        editor.apply();
    }

    public static void setUserLogout() {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.remove(PREFS_USER_LOGIN);
        editor.apply();
    }

    public static boolean getUserLogin() {
        return getPreferences().getBoolean(PREFS_USER_LOGIN, false);
    }

    public static void setChildren(JSONArray children) {
        L.debug("setChildren" + children);

        cacheJSONArray(children, PREFS_USER_CHILDREN);
    }

    public static JSONArray getChildren() {
        return retrieveJSONArray(PREFS_USER_CHILDREN);
    }

    public static void setProducts(JSONArray products) {
        L.debug("setProducts" + products);

        cacheJSONArray(products, PREFS_USER_PRODUCTS);
    }

    public static JSONArray getProducts() {
        return  retrieveJSONArray(PREFS_USER_PRODUCTS);
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

    public static void cacheJSONArray(JSONArray jsonArray, String jsonType) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("json", jsonArray);
            cacheJSONObject(jsonObject, jsonType);
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static JSONArray retrieveJSONArray(String jsonType) {
        L.debug(App.TAG, "retrieve "+jsonType);
        JSONObject jsonObject = retrieveJSONObject(jsonType);
        if (jsonObject != null) {
            try {
                return jsonObject.getJSONArray("json");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
        //L.debug(App.TAG, "null");
        return null;
    }

    public static void cacheJSONObject(JSONObject jsonObject, String jsonType) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(jsonType, jsonObject.toString());
        editor.apply();
    }

    public static JSONObject retrieveJSONObject(String jsonType) {
        String strJson = getPreferences().getString(jsonType, null);
        if(strJson != null) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                return jsonObject;
            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

}
