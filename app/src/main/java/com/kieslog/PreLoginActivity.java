package com.kieslog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kieslog.util.AsyncApiCallOnTaskCompleted;
import com.kieslog.util.Fonts;
import com.kieslog.util.Tools;


public class PreLoginActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tools.setupGoogleAnalytics(PreLoginActivity.this);
        setContentView(R.layout.activity_prelogin);

        Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);

        TextView txt_welcome = (TextView) findViewById(R.id.txt_welcome);
        txt_welcome.setTypeface(latoBold);

        TextView txt_welcome2 = (TextView) findViewById(R.id.txt_welcome2);
        txt_welcome2.setTypeface(latoBold);

        TextView btn_signin = (TextView) findViewById(R.id.btn_signin);
        btn_signin.setTypeface(latoBold);

        Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setTypeface(latoBold);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreLoginActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onTaskCompleted(int thread, String result) {

    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {
        // TODO Auto-generated method stub

    }

}
