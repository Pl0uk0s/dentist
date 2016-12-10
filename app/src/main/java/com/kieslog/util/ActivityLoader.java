package com.kieslog.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kieslog.R;

public class ActivityLoader {

	public static String act0  = "LoginActivity";
	public static String act1  = "MainActivity";

	private static String pkg = "com.kieslog.";
	
	public static String actionStart = "start";
	public static String actionGoBack = "back";
	 
	public static void load(Activity activity, String destination) {
		ActivityLoader al = new ActivityLoader();
		al.run(activity, destination, actionStart, new Bundle());
	}	
	
	public static void load(Activity activity, String destination, String action) {
		ActivityLoader al = new ActivityLoader();
		al.run(activity, destination, action, new Bundle());
	}
	
	public static void load(Activity activity, String destination, String action, Bundle bundle) {
		ActivityLoader al = new ActivityLoader();
		al.run(activity, destination, action, bundle);
	}	
	
	public static void load(Activity activity, String destination, Bundle bundle) {
		ActivityLoader al = new ActivityLoader();
		al.run(activity, destination, actionStart, bundle);		
	}	
	
	private void run(Activity activity, String destination, String action, Bundle bundle) {
		Intent intent;
		try {
			
			Class<?> startNewClass = Class.forName(pkg+destination);
			
			if (activity.getClass().equals(startNewClass)) {
				throw new RuntimeException("destination class has to be different than activity class");
			}
			
			intent = new Intent(activity.getApplicationContext(), startNewClass);
						 
			 for (String key : bundle.keySet()) {
			        intent.putExtra(key, bundle.get(key).toString()); 
			 }			
			
			activity.startActivity(intent);			
			activity.finish();
			if (action.equals(actionGoBack)) {				
				activity.overridePendingTransition(R.anim.slide_in_left, R.anim.fadeout2);
			} else {
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.fadeout2);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
	}
	
}
