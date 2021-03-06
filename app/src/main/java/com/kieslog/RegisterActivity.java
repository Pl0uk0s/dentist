package com.kieslog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kieslog.util.ActivityLoader;
import com.kieslog.util.AsyncApiCallOnTaskCompleted;
import com.kieslog.util.Fonts;
import com.kieslog.util.L;
import com.kieslog.util.LocalStorage;
import com.kieslog.util.Tools;
import com.kieslog.util.WebApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    private int LOAD_CHILDREN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tools.setupGoogleAnalytics(RegisterActivity.this);
        setContentView(R.layout.activity_register);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);
        Typeface latoRegular = Fonts.returnFont(this, Fonts.LATO_REGULAR);

        TextView txt_register = (TextView) findViewById(R.id.txt_register);
        txt_register.setTypeface(latoBold);

        TextView txt_email = (TextView) findViewById(R.id.txt_email);
        txt_email.setTypeface(latoRegular);

        EditText edit_email = (EditText) findViewById(R.id.edit_email);
        edit_email.setTypeface(latoRegular);

        TextView txt_access_code = (TextView) findViewById(R.id.txt_accessCode);
        txt_access_code.setTypeface(latoRegular);

        EditText edit_accessCode = (EditText) findViewById(R.id.edit_accessCode);
        edit_accessCode.setTypeface(latoRegular);

        TextView txt_password = (TextView) findViewById(R.id.txt_password);
        txt_password.setTypeface(latoRegular);

        final EditText edit_password = (EditText) findViewById(R.id.edit_password);
        edit_password.setTypeface(latoRegular);

        TextView txt_repassword = (TextView) findViewById(R.id.txt_repassword);
        txt_repassword.setTypeface(latoRegular);

        final EditText edit_repassword = (EditText) findViewById(R.id.edit_repassword);
        edit_repassword.setTypeface(latoRegular);

        Button btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setTypeface(latoBold);

        TextView txt_terms = (TextView) findViewById(R.id.txt_terms);
        txt_terms.setTypeface(latoRegular);

        ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.onBackPressed();
                finish();
            }
        });

        ImageView imgShowPass = (ImageView) findViewById(R.id.imgShowPass);
        imgShowPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Typeface latoRegular = Fonts.returnFont(getApplicationContext(), Fonts.LATO_REGULAR);
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        edit_password.setInputType(InputType.TYPE_CLASS_TEXT);
                        edit_password.setSelection(edit_password.getText().length());
                        edit_password.setTypeface(latoRegular);
                        break;
                    case MotionEvent.ACTION_UP:
                        edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edit_password.setSelection(edit_password.getText().length());
                        edit_password.setTypeface(latoRegular);
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
                        edit_repassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        edit_repassword.setSelection(edit_repassword.getText().length());
                        edit_repassword.setTypeface(latoRegular);
                        break;
                    case MotionEvent.ACTION_UP:
                        edit_repassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edit_repassword.setSelection(edit_repassword.getText().length());
                        edit_repassword.setTypeface(latoRegular);
                        break;
                }
                return true;
            }
        });

        edit_email.addTextChangedListener(new TextWatcher() {
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

        edit_accessCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidAccessCode(s)) {
                    ImageView img_checkAccessCode = (ImageView) findViewById(R.id.img_checkAccessCode);
                    img_checkAccessCode.setVisibility(ImageView.VISIBLE);
                }
                else {
                    ImageView img_checkAccessCode = (ImageView) findViewById(R.id.img_checkAccessCode);
                    img_checkAccessCode.setVisibility(ImageView.INVISIBLE);
                }
            }
        });

        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidPassword(s)) {
                    ImageView img_checkPassword = (ImageView) findViewById(R.id.img_checkPassword);
                    img_checkPassword.setVisibility(ImageView.VISIBLE);
                }
                else {
                    ImageView img_checkPassword = (ImageView) findViewById(R.id.img_checkPassword);
                    img_checkPassword.setVisibility(ImageView.INVISIBLE);
                }
            }
        });

        edit_repassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("") && s.toString().equals(edit_password.getText().toString())) {
                    ImageView img_checkRePassword = (ImageView) findViewById(R.id.img_checkRePassword);
                    img_checkRePassword.setVisibility(ImageView.VISIBLE);
                }
                else {
                    ImageView img_checkRePassword = (ImageView) findViewById(R.id.img_checkRePassword);
                    img_checkRePassword.setVisibility(ImageView.INVISIBLE);
                }
            }
        });

        ImageView img_accessCode = (ImageView) findViewById(R.id.img_accessCode);
        img_accessCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, AccessCodeInfoActivity.class);
                startActivity(intent);
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit_email = (EditText) findViewById(R.id.edit_email);
                EditText edit_accessCode = (EditText) findViewById(R.id.edit_accessCode);
                EditText edit_password = (EditText) findViewById(R.id.edit_password);
                EditText edit_repassword = (EditText) findViewById(R.id.edit_repassword);
                if (isValidEmail(edit_email.getText()) && isValidAccessCode(edit_accessCode.getText()) && isValidPassword(edit_password.getText()) && edit_password.getText().toString().equals(edit_repassword.getText().toString())) {
                    JSONObject Response = WebApi.newUser(RegisterActivity.this, edit_email.getText().toString(), edit_accessCode.getText().toString(), edit_password.getText().toString());
                    try {

                        String exists = (String) Response.getString("exists");
                        String loginSuccess = (String) Response.getString("loginSuccess");

                        L.debug(App.TAG, "exists: " + exists + " loginSuccess: " + loginSuccess);

                        String showMsgBox = (String) Response.getString("showMsgBox");
                        if (showMsgBox.equals("1")) {
                            L.debug(App.TAG, "show msgbox");
                            String msg = (String) Response.getString("msg");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }

                        if (exists.equals("0")) {

                        }
                        else {
                            L.debug(App.TAG, "user exist");
                            if (loginSuccess.equals("0")) {
                                L.debug(App.TAG, "login failed");
                                //showDialogBox("Login failed", "email/password wrong").show();
                            } else {
                                L.debug(App.TAG, "login successful");
                                String userId = (String) Response.getString("userId");

                                LocalStorage.setLogin(edit_email.getText().toString(), WebApi.sha1Hash(edit_password.getText().toString()), userId);

                                App.userEmail = edit_email.getText().toString();
                                App.password = WebApi.sha1Hash(edit_password.getText().toString());
                                App.userId = userId;

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                String currentDate = df.format(c.getTime());

                                LocalStorage.setDayForInfo(currentDate);

                                ActivityLoader.load(RegisterActivity.this, ActivityLoader.act1);
                            }
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.str_fillAllFields), Toast.LENGTH_LONG).show();
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

    public final static boolean isValidAccessCode(CharSequence target) {
        if (target == null) {
            return false;
        }
        else {
            if (target.length() >= 5) {
                return true;
            }
            else {
                return false;
            }
        }
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
