package com.trelokopoi.core.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;

import com.trelokopoi.core.App;

public class DeviceInfo {
	
	public static String returnAndroidVersion() {
				
		final String OS = "Android+";
		
		return OS + Build.VERSION.RELEASE;
	}
	
	public static String returnScreenDimensions() {
		
		int Measuredwidth = 0;
		int Measuredheight = 0;
		Point size = new Point();
		
		WindowManager w = (WindowManager) App.getContextOfApplication().getSystemService(Context.WINDOW_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
		{
		    w.getDefaultDisplay().getSize(size);

		    Measuredwidth = size.x;
		    Measuredheight = size.y;
		}
		else
		{
		    Display d = w.getDefaultDisplay();
		    Measuredwidth = d.getWidth();
		    Measuredheight = d.getHeight();
		}		
		
		return String.valueOf(Measuredwidth) + "x" + String.valueOf(Measuredheight);		
	}

	public static String returnDeviceName() {
	  String manufacturer = Build.MANUFACTURER;
	  String model = Build.MODEL;
	  String device = "";
	  
	  if (model.startsWith(manufacturer)) {
		 device = capitalize(model);
	  } else {
		  device = capitalize(manufacturer) + " " + model;
	  }
	  
	  device = device.replace(" ", "+");
	  return device;
	}
	
	public static String returnNetworkCarrier() {
						
		TelephonyManager manager = (TelephonyManager)App.getContextOfApplication().getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getNetworkOperatorName();	
	}
	
	public static String returnNetworkOperator() {
		
		TelephonyManager manager = (TelephonyManager)App.getContextOfApplication().getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getSimOperatorName();	
	}	
	
	public static String returnPhoneNumber() {
		
		TelephonyManager manager = (TelephonyManager)App.getContextOfApplication().getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getLine1Number();	
	}		
		
	private static String capitalize(String s) {
	  if (s == null || s.length() == 0) {
	    return "";
	  }
	  char first = s.charAt(0);
	  if (Character.isUpperCase(first)) {
	    return s;
	  } else {
	    return Character.toUpperCase(first) + s.substring(1);
	  }
	} 

	
	
}
