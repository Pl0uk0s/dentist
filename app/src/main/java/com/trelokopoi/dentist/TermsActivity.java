package com.trelokopoi.dentist;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.trelokopoi.dentist.util.Fonts;

public class TermsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);

        TextView termsHeaderTextView = (TextView)findViewById(R.id.terms_header);
        termsHeaderTextView.setTypeface(latoBold);

        WebView web_terms = (WebView) findViewById(R.id.web_terms);
        web_terms.loadUrl("http://52.58.254.12/K2m/terms.html");

        ImageView back_button = (ImageView) findViewById(R.id.terms_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsActivity.this.onBackPressed();
            }
        });
    }

}
