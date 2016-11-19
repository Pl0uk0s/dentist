package com.trelokopoi.dentist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.trelokopoi.dentist.util.AsyncApiCall;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.LocalStorage;
import com.trelokopoi.dentist.util.WebApi;
import com.trelokopoi.dentist.util.WebInterface;

import org.json.JSONObject;

public class SettingsActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    final Context context = this;
    private int CHANGE_PASSWORD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String[] values = getResources().getStringArray(R.array.settings_array);
        ListView listView = (ListView) findViewById(R.id.settings_list);
        ArrayAdapter<String> settingsAdapter = new ArrayAdapter<>(this, R.layout.settings_list_item, R.id.settings_item, values);
        listView.setAdapter(settingsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0)
                {
                    Toast.makeText(getApplicationContext(), "Position :"+position, Toast.LENGTH_LONG).show();
                }
                else if (position == 1)
                {
                    Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);
//                    final Dialog newPasswordDialog = new Dialog(context, R.style.DialogTheme);
//                    newPasswordDialog.setContentView(R.layout.activity_change_password);
//                    newPasswordDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                    Button dialogButton = (Button) newPasswordDialog.findViewById(R.id.submit);
//                    dialogButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            EditText oldPassword = (EditText) newPasswordDialog.findViewById(R.id.oldPasswordEditText);
//                            EditText newPassword = (EditText) newPasswordDialog.findViewById(R.id.newPasswordEditText);
//                            EditText newPasswordAgain = (EditText) newPasswordDialog.findViewById(R.id.newPasswordAgainEditText);
//
//                            String sOldPassword = oldPassword.getText().toString();
//                            String sNewPassword = newPassword.getText().toString();
//                            String sNewPasswordAgain = newPasswordAgain.getText().toString();
//
//                            String existingPassword = LocalStorage.getUserPassword();
//
//                            if (!WebApi.sha1Hash(sOldPassword).equals(existingPassword)) {
//                                Toast.makeText(getApplicationContext(), "The old password is not correct", Toast.LENGTH_LONG).show();
//                            }
//                            else if (!sNewPassword.equals(sNewPasswordAgain)) {
//                                Toast.makeText(getApplicationContext(), "The new passwords do not match", Toast.LENGTH_LONG).show();
//                            }
//                            else {
//                                new AsyncApiCall(CHANGE_PASSWORD, SettingsActivity.this, false).execute(WebApi.changePassword(WebApi.sha1Hash(newPassword.getText().toString())));
//                                newPasswordDialog.dismiss();
//                            }
//                        }
//                    });
//
//                    newPasswordDialog.show();

                }
                else if (position == 2)
                {
                    Intent intent = new Intent(SettingsActivity.this, TermsActivity.class);
                    startActivity(intent);
                }
                else if (position == 3)
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

        Button signOut = (Button) findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LocalStorage.setUserLogout();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView back_button = (ImageView) findViewById(R.id.settings_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.onBackPressed();
//                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
//                startActivity(i);
//                finish();
            }
        });
    }

    @Override
    public void onTaskCompleted(int thread, String result) {
        JSONObject jsonResult = WebInterface.validateJSON(SettingsActivity.this, result);

        if (jsonResult != null) {
            if (thread == CHANGE_PASSWORD) {
                String success = jsonResult.optString("success", "0");
                if (success.equals("1")) {
                    String pass = jsonResult.optString("pass", "");
                    LocalStorage.setUserPassword(pass);
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
