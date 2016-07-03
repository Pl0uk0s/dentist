package com.trelokopoi.dentist;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class AddProductActivity extends Activity {

    private ListView lv;
    ArrayAdapter<String> adapter;
    EditText inputSearch;
//    ArrayList<HashMap<String, String>> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
