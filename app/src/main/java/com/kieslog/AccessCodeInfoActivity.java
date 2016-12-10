package com.kieslog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kieslog.util.AsyncApiCallOnTaskCompleted;
import com.kieslog.util.Fonts;
import com.kieslog.util.Tools;

public class AccessCodeInfoActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tools.setupGoogleAnalytics(AccessCodeInfoActivity.this);
        setContentView(R.layout.activity_accesscodeinfo);

        Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);

        TextView txt_title1 = (TextView) findViewById(R.id.txt_title1);
        txt_title1.setTypeface(latoBold);

        TextView txt_title2 = (TextView) findViewById(R.id.txt_title2);
        txt_title2.setTypeface(latoBold);

        ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccessCodeInfoActivity.this, RegisterActivity.class);
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
