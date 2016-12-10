package com.kieslog.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.kieslog.App;
import com.kieslog.BuildConfig;

public class L {

    public static void debug(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, getMethod()+msg);
        }
    }

    public static void debug(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(App.TAG, getMethod()+msg);
        }
    }

    public static void toast(Context context, String text) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, "debug: "+text, Toast.LENGTH_SHORT).show();
            Log.d(App.TAG, text);
        }
    }

    private static String getMethod() {

		/*
		 * maybe this will come handy sometime
		 Thread current = Thread.currentThread();
		    StackTraceElement[] stack = current.getStackTrace();
		    for(StackTraceElement element : stack)
		    {
		        if (!element.isNativeMethod()) {
		            String className = element.getClassName();
		            String fileName = element.getFileName();
		            int lineNumber = element.getLineNumber();
		            String methodName = element.getMethodName();
		        }
		    }
		*/
        //return Thread.currentThread().getStackTrace()[0].getMethodName()+"@ ";
        return "";
    }

}
