package com.kieslog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kieslog.util.AsyncApiCall;
import com.kieslog.util.AsyncApiCallOnTaskCompleted;
import com.kieslog.util.Fonts;
import com.kieslog.util.UnitAdapter;
import com.kieslog.util.WebApi;
import com.kieslog.util.WebInterface;

import org.json.JSONObject;

public class NewProductActivity extends Activity  implements AsyncApiCallOnTaskCompleted {

    private Integer ADD_PRODUCT = 0;
    private String prodName, prodUnit, add_product_date;
    private View line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        add_product_date = getIntent().getStringExtra("date");

        final Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);
        final Typeface latoRegular = Fonts.returnFont(this, Fonts.LATO_REGULAR);

        line = findViewById(R.id.unit_view);

        TextView headerTextView = (TextView)findViewById(R.id.new_product_header);
        headerTextView.setTypeface(latoBold);

        TextView nameTitleTextView = (TextView)findViewById(R.id.name_title);
        nameTitleTextView.setTypeface(latoBold);

        TextView unitTitleTextView = (TextView)findViewById(R.id.unit_title);
        unitTitleTextView.setTypeface(latoBold);

        final EditText new_prod_name = (EditText) findViewById(R.id.name_ed);
        new_prod_name.setTypeface(latoRegular);

        RelativeLayout name_layout = (RelativeLayout) findViewById(R.id.name_layout);
        name_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_prod_name.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(new_prod_name, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        final TextView unit = (TextView) findViewById(R.id.unit_tv);
        unit.setTypeface(latoRegular);
        final Button save = (Button) findViewById(R.id.save);
        save.setTypeface(latoRegular);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_prod_name.getText().toString().trim().equals("") || unit.getText().toString().trim().equals("Portion unit")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewProductActivity.this);
                    LayoutInflater inflater = NewProductActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_prod_to_child_dialog, null);
                    final AlertDialog alert = builder.create();
                    alert.setView(dialogView);
                    TextView new_prod_header = (TextView) dialogView.findViewById(R.id.prod_to_children_header);
                    new_prod_header.setTypeface(latoRegular);
                    new_prod_header.setText(R.string.new_prod_dialog_title);
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
                else {
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
                line.setVisibility(View.GONE);
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
                line.setVisibility(View.VISIBLE);
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
