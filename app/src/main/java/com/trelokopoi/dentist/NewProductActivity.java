package com.trelokopoi.dentist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trelokopoi.dentist.util.AsyncApiCall;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.Fonts;
import com.trelokopoi.dentist.util.SettingsAdapter;
import com.trelokopoi.dentist.util.UnitAdapter;
import com.trelokopoi.dentist.util.WebApi;
import com.trelokopoi.dentist.util.WebInterface;

import org.json.JSONObject;

public class NewProductActivity extends Activity  implements AsyncApiCallOnTaskCompleted {

    private Integer ADD_PRODUCT = 0;
    private String prodName, prodUnit, add_product_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        add_product_date = getIntent().getStringExtra("date");

        final Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);
        final Typeface latoRegular = Fonts.returnFont(this, Fonts.LATO_REGULAR);

        TextView headerTextView = (TextView)findViewById(R.id.new_product_header);
        headerTextView.setTypeface(latoBold);

        TextView nameTitleTextView = (TextView)findViewById(R.id.name_title);
        nameTitleTextView.setTypeface(latoBold);

        TextView unitTitleTextView = (TextView)findViewById(R.id.unit_title);
        unitTitleTextView.setTypeface(latoBold);

        final TextView unit = (TextView) findViewById(R.id.unit_tv);
        unit.setTypeface(latoRegular);
        final Button save = (Button) findViewById(R.id.save);
        save.setTypeface(latoRegular);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText new_prod_name = (EditText) findViewById(R.id.name_ed);
                new_prod_name.setTypeface(latoRegular);
                if (new_prod_name.getText().toString().trim().equals("") || unit.getText().toString().trim().equals("")) {
                    Toast.makeText(NewProductActivity.this, "Fill the product name & sugar per 100 gr/ml in order to Submit new product", Toast.LENGTH_SHORT).show();
                } else {
                    prodName = new_prod_name.getText().toString();
                    prodUnit = unit.getText().toString();
                    new AsyncApiCall(ADD_PRODUCT, NewProductActivity.this, false).execute(WebApi.addProduct(new_prod_name.getText().toString(), unit.getText().toString()));
                }
            }
        });

        String[] values = getResources().getStringArray(R.array.unit_array);
        final ListView listView = (ListView) findViewById(R.id.unit_list);
        UnitAdapter unitAdapter = new UnitAdapter(this, R.id.unit_item, R.layout.unit_list_item, values);
        listView.setAdapter(unitAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                unit.setTextColor(Color.BLACK);
                unit.setText(parent.getItemAtPosition(position).toString());
                prodUnit = unit.getText().toString();
                listView.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
            }
        });

        unit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                View view = NewProductActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                listView.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
            }
        });

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewProductActivity.this, AddProductActivity.class);
                i.putExtra("date", add_product_date);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onTaskCompleted(int thread, String result) {

        JSONObject jsonResult = WebInterface.validateJSON(NewProductActivity.this, result);

        if (jsonResult != null) {
            if (thread == ADD_PRODUCT) {
                String success = jsonResult.optString("success", "0");
                if (success.equals("1")) {
                    String productId = jsonResult.optString("productId", "0");
                    Intent i = new Intent(NewProductActivity.this, ProductToChildrenActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("date", add_product_date);
                    extras.putString("productName", prodName);
                    extras.putString("productId", productId);
                    extras.putString("unit",prodUnit);
                    i.putExtras(extras);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {

    }
}
