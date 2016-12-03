package com.trelokopoi.dentist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.trelokopoi.dentist.util.AsyncApiCall;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.Fonts;
import com.trelokopoi.dentist.util.LocalStorage;
import com.trelokopoi.dentist.util.SettingsAdapter;
import com.trelokopoi.dentist.util.WebApi;
import com.trelokopoi.dentist.util.WebInterface;

import org.json.JSONObject;

public class SettingsActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    final Context context = this;
    private int CHANGE_EMAIL = 0;
    String button_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);
        Typeface latoRegular = Fonts.returnFont(this, Fonts.LATO_REGULAR);

        TextView emailHeaderTextView = (TextView)findViewById(R.id.settings_header);
        emailHeaderTextView.setTypeface(latoRegular);

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
                    // Inflate the about message contents
                    View messageView = getLayoutInflater().inflate(R.layout.about_dialog, null, false);

                    // When linking text, force to always use default color. This works
                    // around a pressed color state bug.
                    TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
                    int defaultColor = textView.getTextColors().getDefaultColor();
                    textView.setTextColor(defaultColor);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setTitle(R.string.app_name);
                    builder.setView(messageView);
                    builder.create();
                    builder.show();
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
                            builder.setMessage("Are you sure you want to sign out?")
                            .setPositiveButton("SIGN OUT", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LocalStorage.setUserLogout();
                                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            })
                            .setNegativeButton("CANCEL", null)
                            .show();
                }
            }
        });

        ImageView back_button = (ImageView) findViewById(R.id.settings_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.onBackPressed();
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
}
