package com.kieslog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kieslog.util.AsyncApiCall;
import com.kieslog.util.AsyncApiCallOnTaskCompleted;
import com.kieslog.util.Fonts;
import com.kieslog.util.LocalStorage;
import com.kieslog.util.SettingsAdapter;
import com.kieslog.util.WebApi;
import com.kieslog.util.WebInterface;

import org.json.JSONObject;

public class SettingsActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    final Context context = this;
    private int CHANGE_EMAIL = 0;
    String button_txt;
    private Typeface latoBold, latoRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);
        latoRegular = Fonts.returnFont(this, Fonts.LATO_REGULAR);

        TextView emailHeaderTextView = (TextView)findViewById(R.id.settings_header);
        emailHeaderTextView.setTypeface(latoBold);

        TextView emailTitleTextView = (TextView)findViewById(R.id.settings_title);
        emailTitleTextView.setTypeface(latoBold);

        final TextView emailTextView = (TextView)findViewById(R.id.emailTextView);
        emailTextView.setTypeface(latoBold);
        final TextView emailTextView2 = (TextView)findViewById(R.id.emailTextView2);
        emailTextView2.setTypeface(latoRegular);
        final EditText emailEditText = (EditText)findViewById(R.id.emailEditText);
        emailEditText.setTypeface(latoRegular);
        final Button signOut = (Button) findViewById(R.id.sign_out);
        signOut.setTypeface(latoRegular);
        button_txt = signOut.getText().toString();
        emailTextView2.setText(LocalStorage.getUserEmail());
        emailEditText.setText(LocalStorage.getUserEmail());

        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                emailTextView2.setVisibility(View.GONE);
                emailEditText.setVisibility(View.VISIBLE);
                emailEditText.requestFocus();
                signOut.setText(R.string.button_save);
                button_txt = signOut.getText().toString();
            }
        });

        emailTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                emailTextView2.setVisibility(View.GONE);
                emailEditText.setVisibility(View.VISIBLE);
                emailEditText.requestFocus();
                signOut.setText(R.string.button_save);
                button_txt = signOut.getText().toString();
            }
        });

        String[] values = getResources().getStringArray(R.array.settings_array);
        ListView listView = (ListView) findViewById(R.id.settings_list);
        SettingsAdapter settingsAdapter = new SettingsAdapter(this, R.id.settings_item, R.layout.settings_list_item, values);
        listView.setAdapter(settingsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0)
                {
                    Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);
                }
                else if (position == 1)
                {
                    Intent intent = new Intent(SettingsActivity.this, TermsActivity.class);
                    startActivity(intent);
                }
                else if (position == 2)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    LayoutInflater inflater = SettingsActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_about_dialog, null);
                    final AlertDialog alert = builder.create();
                    alert.setView(dialogView);
                    TextView about_title = (TextView) dialogView.findViewById(R.id.about_title);
                    about_title.setTypeface(latoBold);
                    TextView about_header = (TextView) dialogView.findViewById(R.id.about_header);
                    about_header.setTypeface(latoRegular);
                    Button button_ok = (Button) dialogView.findViewById(R.id.dialog_ok);
                    button_ok.setTypeface(latoBold);
                    button_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });
                    alert.show();
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (button_txt.equals("Done")) {
                    new AsyncApiCall(CHANGE_EMAIL, SettingsActivity.this, false).execute(WebApi.changeEmail(emailEditText.getText().toString()));
                    emailEditText.setVisibility(View.GONE);
                    emailTextView2.setText(emailEditText.getText().toString());
                    emailTextView2.setVisibility(View.VISIBLE);
                    signOut.setText(R.string.button_sign_out);
                    button_txt = signOut.getText().toString();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    LayoutInflater inflater = SettingsActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_sign_out_dialog, null);
                    final AlertDialog alert = builder.create();
                    alert.setView(dialogView);
                    TextView sign_out_header = (TextView) dialogView.findViewById(R.id.sign_out_header);
                    sign_out_header.setTypeface(latoBold);
                    Button button_cancel = (Button) dialogView.findViewById(R.id.sign_out_cancel);
                    button_cancel.setTypeface(latoBold);
                    button_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });
                    Button button_sign_out = (Button) dialogView.findViewById(R.id.dialog_sign_out);
                    button_sign_out.setTypeface(latoBold);
                    button_sign_out.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LocalStorage.setUserLogout();
                            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    });
                    alert.show();
                }
            }
        });

        ImageView back_button = (ImageView) findViewById(R.id.settings_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onTaskCompleted(int thread, String result) {
        JSONObject jsonResult = WebInterface.validateJSON(SettingsActivity.this, result);

        if (jsonResult != null) {
            if (thread == CHANGE_EMAIL) {
                String success = jsonResult.optString("success", "0");
                if (success.equals("1")) {
                    String email = jsonResult.optString("email", "");
                    LocalStorage.setUserEmail(email);
                    App.userEmail = email;
                    Toast.makeText(getApplicationContext(), "The email has been changed.", Toast.LENGTH_LONG).show();
                }
                else if (success.equals("0")) {
                    Toast.makeText(getApplicationContext(), "An error occurred. The email was not changed.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
