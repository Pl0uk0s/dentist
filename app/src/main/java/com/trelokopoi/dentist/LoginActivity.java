package com.trelokopoi.dentist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trelokopoi.dentist.util.ActivityLoader;
import com.trelokopoi.dentist.util.AsyncApiCall;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.Fonts;
import com.trelokopoi.dentist.util.L;
import com.trelokopoi.dentist.util.LocalStorage;
import com.trelokopoi.dentist.util.Tools;
import com.trelokopoi.dentist.util.WebApi;
import com.trelokopoi.dentist.util.WebInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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

            EditText edit_password = (EditText) findViewById(R.id.edit_password);
            edit_password.setTypeface(latoRegular);

            CheckBox chk_rememberme = (CheckBox) findViewById(R.id.chk_rememberme);
            chk_rememberme.setTypeface(latoRegular);

            TextView txt_forgotPassword = (TextView) findViewById(R.id.txt_forgotPassword);
            txt_forgotPassword.setTypeface(latoRegular);

            Button btn_done = (Button) findViewById(R.id.btn_done);
            btn_done.setTypeface(latoBold);

            ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginActivity.this.onBackPressed();
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
                        Tools.toast(getApplicationContext(), getString(R.string.str_noEmail));
                    } else if (password.length() == 0) {
                        Tools.toast(getApplicationContext(), getString(R.string.str_noPass));
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
                                Tools.toast(getApplicationContext(), msg);
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

                                    CheckBox checkRemember = (CheckBox) findViewById(R.id.chk_rememberme);
                                    Boolean remember = checkRemember.isChecked();

                                    LocalStorage.setLogin(useremail, WebApi.sha1Hash(password), userId);

                                    if (remember) {
                                        LocalStorage.setUserLogin(true);
                                    }
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
