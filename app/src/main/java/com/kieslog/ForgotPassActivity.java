package com.kieslog;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kieslog.util.AsyncApiCall;
import com.kieslog.util.AsyncApiCallOnTaskCompleted;
import com.kieslog.util.Fonts;
import com.kieslog.util.Tools;
import com.kieslog.util.WebApi;
import com.kieslog.util.WebInterface;

import org.json.JSONObject;

public class ForgotPassActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    private int FORGOT_PASS = 0;
    private EditText useremail_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tools.setupGoogleAnalytics(ForgotPassActivity.this);

        setContentView(R.layout.activity_forgot_pass);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);
        Typeface latoRegular = Fonts.returnFont(this, Fonts.LATO_REGULAR);

        TextView txt_header = (TextView) findViewById(R.id.txt_header);
        txt_header.setTypeface(latoBold);

        TextView txt_text1 = (TextView) findViewById(R.id.txt_text1);
        txt_text1.setTypeface(latoBold);

        TextView txt_email = (TextView) findViewById(R.id.txt_email);
        txt_email.setTypeface(latoRegular);

        EditText edit_email = (EditText) findViewById(R.id.edit_email);
        edit_email.setTypeface(latoRegular);

        Button btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setTypeface(latoBold);

        ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPassActivity.this.onBackPressed();
            }
        });

        useremail_edittext = (EditText) findViewById(R.id.edit_email);

        useremail_edittext.setText(getIntent().getStringExtra("email"));
        if (!useremail_edittext.getText().toString().equals("")  && isValidEmail(useremail_edittext.getText())) {
            ImageView img_checkEmail = (ImageView) findViewById(R.id.img_checkEmail);
            img_checkEmail.setVisibility(ImageView.VISIBLE);
        }
        useremail_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidEmail(s)) {
                    ImageView img_checkEmail = (ImageView) findViewById(R.id.img_checkEmail);
                    img_checkEmail.setVisibility(ImageView.VISIBLE);
                }
                else {
                    ImageView img_checkEmail = (ImageView) findViewById(R.id.img_checkEmail);
                    img_checkEmail.setVisibility(ImageView.INVISIBLE);
                }
            }
        });

        Button done = (Button) findViewById(R.id.btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(useremail_edittext.getText())) {
                    new AsyncApiCall(FORGOT_PASS, ForgotPassActivity.this, false).execute(WebApi.forgotPassword(useremail_edittext.getText().toString()));
                }
            }
        });

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        }
        else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onTaskCompleted(int thread, String result) {
        JSONObject jsonResult = WebInterface.validateJSON(ForgotPassActivity.this, result);
    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {
        // TODO Auto-generated method stub

    }

}
