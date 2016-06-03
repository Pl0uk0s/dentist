package com.trelokopoi.core.util;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trelokopoi.core.R;

import static android.R.attr.id;

public class Tools {

    /**
     * Simple String transformation by XOR-ing all characters by value.
     * http://w3facility.org/question/how-to-protect-google-play-public-key-when-doing-inapp-billing/
     * http://www.htmlescape.net/javaescape_tool.html
     */
    public static String stringTransform(String s, int i) {
        char[] chars = s.toCharArray();
        for(int j = 0; j<chars.length; j++)
            chars[j] = (char)(chars[j] ^ i);
        return String.valueOf(chars);
    }

    public static void toast(final Context ctx, final String msg) {

        L.debug("toast");

        final Handler mHandler = new Handler();
        final int mtoastLength = 20;

        //Log.d(LOG_TAG, “enter fireLongToast()”);
        final Runnable long_toast_run = new Runnable() {
            @Override
            public void run() {
                try {
                    L.debug("toast-in");
                    int toastLengh = 1;

                    if (msg.length() >= mtoastLength) {
                        toastLengh = (msg.length() / mtoastLength) / 2 + 1;
                    }

                    _customToast(ctx, msg, toastLengh);

                } catch (Exception e) {
                    //Log.e(LOG_TAG, e.getMessage());
                    Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                }

            }
        };
        L.debug("toast2");


        new Thread() {
            public void run() {
                mHandler.post(long_toast_run);
            }
        }.start();

        L.debug("toast3");
    }

    private static void _customToast(Context ctx, String msg, int toastLengh) {

        View toastLayout = LayoutInflater.from(ctx).inflate(R.layout._custom_alert_dialog, null);

        TextView text = (TextView) toastLayout.findViewById(R.id.txt);
        text.setText(msg);

        Toast toast = new Toast(ctx);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastLayout);

        int i = 0;
        while (i < toastLengh) {
            toast.show();
            i++;
            if (i == 10) break;
        }

    }

    public static boolean isCameraAvailable() {

        boolean hasCamera = false;

        int numCameras = Camera.getNumberOfCameras();
        if (numCameras > 0) {
            hasCamera = true;
        }

        return hasCamera;
    }

}
