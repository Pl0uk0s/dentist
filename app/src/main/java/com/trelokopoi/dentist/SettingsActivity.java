package com.trelokopoi.dentist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.trelokopoi.dentist.util.LocalStorage;

public class SettingsActivity extends Activity {

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
                    Toast.makeText(getApplicationContext(), "Position :"+position, Toast.LENGTH_LONG)
                            .show();
                }
                else if (position == 1)
                {
                    Toast.makeText(getApplicationContext(), "Position :"+position, Toast.LENGTH_LONG)
                            .show();
                }
                else if (position == 2)
                {
                    LocalStorage.setUserLogout();
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
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
}
