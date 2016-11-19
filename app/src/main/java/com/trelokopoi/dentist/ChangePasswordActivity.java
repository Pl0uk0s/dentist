package com.trelokopoi.dentist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.trelokopoi.dentist.util.AsyncApiCall;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.LocalStorage;
import com.trelokopoi.dentist.util.WebApi;
import com.trelokopoi.dentist.util.WebInterface;

import org.json.JSONObject;

public class ChangePasswordActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    final Context context = this;
    private int CHANGE_PASSWORD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Button done = (Button) findViewById(R.id.submit);
        done.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            EditText oldPassword = (EditText) findViewById(R.id.oldPasswordEditText);
            EditText newPassword = (EditText) findViewById(R.id.newPasswordEditText);
            EditText newPasswordAgain = (EditText) findViewById(R.id.newPasswordAgainEditText);

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


    @Override
    public void onTaskCompleted(int thread, String result) {
        JSONObject jsonResult = WebInterface.validateJSON(ChangePasswordActivity.this, result);

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

