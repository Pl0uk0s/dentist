package com.kieslog.util;

import android.app.Activity;
import android.view.View;

public abstract class NoInternetDialog implements Runnable
{
    public NoInternetDialog(final Activity a) {
        final CustomAlertDialog alert = new CustomAlertDialog(a);
        alert.setText("No internet connection. Cannot connect to the server. Please reopen the app.");

        alert.setYes(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Sounds.buttonClick();
            alert.dismiss();
            run();
            a.finish();
        }});

        if (!a.isFinishing()) {
            alert.show();
        }
    }
}