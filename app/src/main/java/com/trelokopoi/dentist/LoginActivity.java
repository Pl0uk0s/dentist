package com.trelokopoi.dentist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.trelokopoi.dentist.util.ActivityLoader;
import com.trelokopoi.dentist.util.AsyncApiCall;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.L;
import com.trelokopoi.dentist.util.LocalStorage;
import com.trelokopoi.dentist.util.Tools;
import com.trelokopoi.dentist.util.WebApi;
import com.trelokopoi.dentist.util.WebInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoginActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    private int LOAD_CHILDREN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tools.setupGoogleAnalytics(LoginActivity.this);

        if (LocalStorage.getUserLogin()) {
            getChildrenNames();
        }
        else {

            setContentView(R.layout.activity_login);

            Button login = (Button) findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText username_edittext = (EditText) findViewById(R.id.username);
                    EditText password_edittext = (EditText) findViewById(R.id.password);

                    String username = username_edittext.getText().toString();
                    String password = password_edittext.getText().toString();

                    if (username.length() == 0) {
                        Tools.toast(getApplicationContext(), "Please enter a username");
                    } else if (password.length() == 0) {
                        Tools.toast(getApplicationContext(), "Please enter a password");
                    } else {
                        JSONObject Response = WebApi.authenticate(LoginActivity.this, username, password);
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

                                    CheckBox checkRemember = (CheckBox) findViewById(R.id.checkRemember);
                                    Boolean remember = checkRemember.isChecked();

                                    if (remember) {
                                        LocalStorage.setLogin(username, WebApi.sha1Hash(password), userId);
                                        LocalStorage.setUserLogin(true);
                                    }
                                    getChildrenNames();
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void getChildrenNames() {
        new AsyncApiCall(LOAD_CHILDREN, LoginActivity.this, false).execute(WebApi.getChidren());
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
        JSONObject jsonResult = WebInterface.validateJSON(LoginActivity.this, result);

        if (jsonResult != null) {
            if (thread == LOAD_CHILDREN) {

                String success = jsonResult.optString("success", "0");

                if (success.equals("1")) {
                    JSONArray children = jsonResult.optJSONArray("children");
                    LocalStorage.setChildren(children);

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String currentDate = df.format(c.getTime());

                    LocalStorage.setDayForInfo(currentDate);

                    ActivityLoader.load(LoginActivity.this, ActivityLoader.act1);
                }
                else {
                    Tools.toast(getApplicationContext(), "Sorry, an error occurred. Please reopen the app.");
                }
            }
        }



    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {
        // TODO Auto-generated method stub

    }
}
