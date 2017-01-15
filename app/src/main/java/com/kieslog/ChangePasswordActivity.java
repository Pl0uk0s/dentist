package com.kieslog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kieslog.util.AsyncApiCall;
import com.kieslog.util.AsyncApiCallOnTaskCompleted;
import com.kieslog.util.Fonts;
import com.kieslog.util.LocalStorage;
import com.kieslog.util.WebApi;
import com.kieslog.util.WebInterface;

import org.json.JSONObject;

public class ChangePasswordActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    final Context context = this;
    private int CHANGE_PASSWORD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        final Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);
        final Typeface latoRegular = Fonts.returnFont(this, Fonts.LATO_REGULAR);

        TextView titleTextView = (TextView)findViewById(R.id.change_pass_header);
        titleTextView.setTypeface(latoBold);

        TextView headerTextView = (TextView)findViewById(R.id.change_pass_title);
        headerTextView.setTypeface(latoBold);

        TextView oldPassTextView = (TextView)findViewById(R.id.oldPassword);
        oldPassTextView.setTypeface(latoBold);

        TextView newPassTextView = (TextView)findViewById(R.id.newPassword);
        newPassTextView.setTypeface(latoBold);

        TextView newPassAgainTextView = (TextView)findViewById(R.id.newPasswordAgain);
        newPassAgainTextView.setTypeface(latoBold);

        final EditText oldPassword = (EditText) findViewById(R.id.oldPasswordEditText);
        oldPassword.setTypeface(latoRegular);
        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidPassword(s)) {
                    ImageView img_checkOldPass = (ImageView) findViewById(R.id.img_checkOldPass);
                    img_checkOldPass.setVisibility(ImageView.VISIBLE);
                }
                else {
                    ImageView img_checkOldPass = (ImageView) findViewById(R.id.img_checkOldPass);
                    img_checkOldPass.setVisibility(ImageView.INVISIBLE);
                }
            }
        });

        final EditText newPassword = (EditText) findViewById(R.id.newPasswordEditText);
        newPassword.setTypeface(latoRegular);
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidPassword(s)) {
                    ImageView img_checkNewPass = (ImageView) findViewById(R.id.img_checkNewPass);
                    img_checkNewPass.setVisibility(ImageView.VISIBLE);
                }
                else {
                    ImageView img_checkNewPass = (ImageView) findViewById(R.id.img_checkNewPass);
                    img_checkNewPass.setVisibility(ImageView.INVISIBLE);
                }
            }
        });

        final EditText newPasswordAgain = (EditText) findViewById(R.id.newPasswordAgainEditText);
        newPasswordAgain.setTypeface(latoRegular);
        newPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("") && s.toString().equals(newPassword.getText().toString())) {
                    ImageView img_checkNewPassAgain = (ImageView) findViewById(R.id.img_checkNewPassAgain);
                    img_checkNewPassAgain.setVisibility(ImageView.VISIBLE);
                }
                else {
                    ImageView img_checkNewPassAgain = (ImageView) findViewById(R.id.img_checkNewPassAgain);
                    img_checkNewPassAgain.setVisibility(ImageView.INVISIBLE);
                }
            }
        });

        ImageView imgShowOldPass = (ImageView) findViewById(R.id.imgShowOldPass);
        imgShowOldPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Typeface latoRegular = Fonts.returnFont(getApplicationContext(), Fonts.LATO_REGULAR);
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        oldPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        oldPassword.setSelection(oldPassword.getText().length());
                        oldPassword.setTypeface(latoRegular);
                        break;
                    case MotionEvent.ACTION_UP:
                        oldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        oldPassword.setSelection(oldPassword.getText().length());
                        oldPassword.setTypeface(latoRegular);
                        break;
                }
                return true;
            }
        });

        ImageView imgShowPass = (ImageView) findViewById(R.id.imgShowPass);
        imgShowPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Typeface latoRegular = Fonts.returnFont(getApplicationContext(), Fonts.LATO_REGULAR);
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        newPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        newPassword.setSelection(newPassword.getText().length());
                        newPassword.setTypeface(latoRegular);
                        break;
                    case MotionEvent.ACTION_UP:
                        newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        newPassword.setSelection(newPassword.getText().length());
                        newPassword.setTypeface(latoRegular);
                        break;
                }
                return true;
            }
        });

        ImageView imgShowRePass = (ImageView) findViewById(R.id.imgShowRePass);
        imgShowRePass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Typeface latoRegular = Fonts.returnFont(getApplicationContext(), Fonts.LATO_REGULAR);
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        newPasswordAgain.setInputType(InputType.TYPE_CLASS_TEXT);
                        newPasswordAgain.setSelection(newPasswordAgain.getText().length());
                        newPasswordAgain.setTypeface(latoRegular);
                        break;
                    case MotionEvent.ACTION_UP:
                        newPasswordAgain.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        newPasswordAgain.setSelection(newPasswordAgain.getText().length());
                        newPasswordAgain.setTypeface(latoRegular);
                        break;
                }
                return true;
            }
        });

        Button done = (Button) findViewById(R.id.submit);
        done.setTypeface(latoRegular);
        done.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            EditText oldPassword = (EditText) findViewById(R.id.oldPasswordEditText);
            oldPassword.setTypeface(latoRegular);
            EditText newPassword = (EditText) findViewById(R.id.newPasswordEditText);
            newPassword.setTypeface(latoRegular);

            String sOldPassword = oldPassword.getText().toString();
            String sNewPassword = newPassword.getText().toString();
            String sNewPasswordAgain = newPasswordAgain.getText().toString();

            String existingPassword = LocalStorage.getUserPassword();

            if (!WebApi.sha1Hash(sOldPassword).equals(existingPassword)) {
                Toast.makeText(getApplicationContext(), "The old password is not correct", Toast.LENGTH_LONG).show();
            } else if (!sNewPassword.equals(sNewPasswordAgain)) {
                Toast.makeText(getApplicationContext(), "The new passwords do not match", Toast.LENGTH_LONG).show();
            } else {
                new AsyncApiCall(CHANGE_PASSWORD, ChangePasswordActivity.this, false).execute(WebApi.changePassword(WebApi.sha1Hash(newPassword.getText().toString())));
                Intent intent = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }

            }
        });

        ImageView back_button = (ImageView) findViewById(R.id.change_pass_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordActivity.this.onBackPressed();
            }
        });
    }

    public final static boolean isValidPassword(CharSequence target) {
        if (target == null) {
            return false;
        }
        else {
            if (target.length() >= 6) {
                return true;
            }
            else {
                return false;
            }
        }
    }


    @Override
    public void onTaskCompleted(int thread, String result) {
        JSONObject jsonResult = WebInterface.validateJSON(ChangePasswordActivity.this, result);

        if (jsonResult != null) {
            if (thread == CHANGE_PASSWORD) {
                String success = jsonResult.optString("success", "0");
                if (success.equals("1")) {
                    String pass = jsonResult.optString("pass", "");
                    LocalStorage.setUserPassword(pass);
                    App.password = pass;
                    Toast.makeText(getApplicationContext(), "The password has been changed.", Toast.LENGTH_LONG).show();
                }
                else if (success.equals("0")) {
                    Toast.makeText(getApplicationContext(), "An error occurred. The password was not changed.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {

    }
}

