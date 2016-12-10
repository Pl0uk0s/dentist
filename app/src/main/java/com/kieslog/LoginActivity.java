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
import android.widget.CheckBox;
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

public class LoginActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    private EditText useremail_edittext;
    private EditText password_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tools.setupGoogleAnalytics(LoginActivity.this);

        if (LocalStorage.getUserLogin()) {
            App.userEmail = LocalStorage.getUserEmail();
            App.password = LocalStorage.getUserPassword();
            App.userId = LocalStorage.getUserId();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = df.format(c.getTime());

            LocalStorage.setDayForInfo(currentDate);

            ActivityLoader.load(LoginActivity.this, ActivityLoader.act1);
        }
        else {

            setContentView(R.layout.activity_login);

            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);
            Typeface latoRegular = Fonts.returnFont(this, Fonts.LATO_REGULAR);

            TextView txt_login = (TextView) findViewById(R.id.txt_login);
            txt_login.setTypeface(latoBold);

            TextView txt_email = (TextView) findViewById(R.id.txt_email);
            txt_email.setTypeface(latoRegular);

            EditText edit_email = (EditText) findViewById(R.id.edit_email);
            edit_email.setTypeface(latoRegular);

            TextView txt_password = (TextView) findViewById(R.id.txt_password);
            txt_password.setTypeface(latoRegular);

            final EditText edit_password = (EditText) findViewById(R.id.edit_password);
            edit_password.setTypeface(latoRegular);

//            CheckBox chk_rememberme = (CheckBox) findViewById(R.id.chk_rememberme);
//            chk_rememberme.setTypeface(latoRegular);

            TextView txt_forgotPassword = (TextView) findViewById(R.id.txt_forgotPassword);
            txt_forgotPassword.setTypeface(latoRegular);

            Button btn_done = (Button) findViewById(R.id.btn_done);
            btn_done.setTypeface(latoBold);

            ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, PreLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            ImageView imgShow = (ImageView) findViewById(R.id.imgShow);
            imgShow.setOnTouchListener(new View.OnTouchListener() {
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

            useremail_edittext = (EditText) findViewById(R.id.edit_email);
            useremail_edittext.setText(LocalStorage.getUserEmail());
            if (!useremail_edittext.getText().toString().equals("")) {
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

            password_edittext = (EditText) findViewById(R.id.edit_password);
            password_edittext.addTextChangedListener(new TextWatcher() {
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

            Button login = (Button) findViewById(R.id.btn_done);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String useremail = useremail_edittext.getText().toString();
                    String password = password_edittext.getText().toString();

                    if (useremail.length() == 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.str_noEmail), Toast.LENGTH_LONG).show();
                    } else if (password.length() == 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.str_noPass), Toast.LENGTH_LONG).show();
                    } else if (isValidPassword(password) && isValidEmail(useremail)) {
                        JSONObject Response = WebApi.authenticate(LoginActivity.this, useremail, password);
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

                            } else {
                                L.debug(App.TAG, "user exist");
                                if (loginSuccess.equals("0")) {
                                    L.debug(App.TAG, "login failed");
                                    //showDialogBox("Login failed", "email/password wrong").show();
                                } else {
                                    L.debug(App.TAG, "login successful");
                                    String userId = (String) Response.getString("userId");

//                                    CheckBox checkRemember = (CheckBox) findViewById(R.id.chk_rememberme);
//                                    Boolean remember = checkRemember.isChecked();

                                    LocalStorage.setLogin(useremail, WebApi.sha1Hash(password), userId);

//                                    if (remember) {
//                                        LocalStorage.setUserLogin(true);
//                                    }
                                    App.userEmail = useremail;
                                    App.password = WebApi.sha1Hash(password);
                                    App.userId = userId;

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                    String currentDate = df.format(c.getTime());

                                    LocalStorage.setDayForInfo(currentDate);

                                    ActivityLoader.load(LoginActivity.this, ActivityLoader.act1);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getString(R.string.str_noValid), Toast.LENGTH_LONG).show();
                    }
                }
            });

            TextView forgot_password = (TextView) findViewById(R.id.txt_forgotPassword);
            forgot_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                    intent.putExtra("email", useremail_edittext.getText().toString());
                    startActivity(intent);
                }
            });
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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        }
        else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
    public void onTaskCompleted(int thread, String result) {

    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {
        // TODO Auto-generated method stub

    }
}
