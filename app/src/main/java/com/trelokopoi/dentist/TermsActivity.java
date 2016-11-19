package com.trelokopoi.dentist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TermsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        ImageView back_button = (ImageView) findViewById(R.id.terms_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsActivity.this.onBackPressed();
//                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
//                startActivity(i);
//                finish();
            }
        });
    }

}
