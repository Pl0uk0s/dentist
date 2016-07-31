package com.trelokopoi.dentist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.trelokopoi.dentist.util.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AddProductActivity extends Activity{

    final Context context = this;
    private ListView productsListView;
    private ArrayAdapter<String> productsAdapter;
    private EditText inputSearch, dateEditText, timeEditText, quantityEditText;
    private ImageView date_img, time_img;
//    ArrayList<HashMap<String, String>> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE"};

        final LinearLayout prod_detail_layout = (LinearLayout) findViewById(R.id.prod_detail_layout);
        productsListView = (ListView) findViewById(R.id.products_list);
        productsAdapter = new ArrayAdapter<>(this, R.layout.products_list_item, R.id.product, products);
        productsListView.setAdapter(productsAdapter);
        inputSearch = (EditText) findViewById(R.id.search_product);
        date_img = (ImageView) findViewById(R.id.date_img);
        time_img = (ImageView) findViewById(R.id.time_img);

        inputSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    prod_detail_layout.setVisibility(View.GONE);
                    productsListView.setVisibility(View.VISIBLE);
                }else {
                    productsListView.setVisibility(View.GONE);
                }
            }
        });

        /**
        * Enabling Search Filter
        **/
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                AddProductActivity.this.productsAdapter.getFilter().filter(cs);
                RelativeLayout.LayoutParams p;
                if (productsAdapter.getCount() > 5)
                {
                    View item = productsAdapter.getView(0, null, productsListView);
                    item.measure(0, 0);
                    productsListView.setVisibility(View.VISIBLE);
                    p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (5 * item.getMeasuredHeight()));
                    p.setMargins(60, 0,60, 0);
                    p.addRule(RelativeLayout.BELOW, R.id.add_top);
                    productsListView.setLayoutParams(p);
                }
                else if (productsAdapter.getCount() != 0)
                {
                    View item = productsAdapter.getView(0, null, productsListView);
                    item.measure(0, 0);
                    productsListView.setVisibility(View.VISIBLE);
                    p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    p.setMargins(60, 0,60, 0);
                    p.addRule(RelativeLayout.BELOW, R.id.add_top);
                    productsListView.setLayoutParams(p);}
                else
                {
                    productsListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String selectedItem = productsAdapter.getItem(position);
                Toast.makeText(AddProductActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
                inputSearch.setText(selectedItem);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                productsListView.setVisibility(View.GONE);
                prod_detail_layout.setVisibility(View.VISIBLE);
                dateEditText = (EditText) findViewById(R.id.date_ed);
                dateEditText.requestFocus();
                quantityEditText = (EditText) findViewById(R.id.quantity_ed);

                CheckBox child1CheckBox = (CheckBox) findViewById(R.id.child1);
                CheckBox child2CheckBox = (CheckBox) findViewById(R.id.child2);
                CheckBox child3CheckBox = (CheckBox) findViewById(R.id.child3);
                CheckBox child4CheckBox = (CheckBox) findViewById(R.id.child4);

                JSONArray children = LocalStorage.getChildren();
                Integer i = children.length();

                try {
                    if (i == 1) {
                        JSONObject child1 = children.getJSONObject(0);
                        child1CheckBox.setText(child1.optString("name", ""));
                        child1CheckBox.setVisibility(View.VISIBLE);
                    }
                    else if (i == 2) {
                        JSONObject child1 = children.getJSONObject(0);
                        child1CheckBox.setText(child1.optString("name", ""));
                        child1CheckBox.setVisibility(View.VISIBLE);
                        JSONObject child2 = children.getJSONObject(1);
                        child2CheckBox.setText(child2.optString("name", ""));
                        child2CheckBox.setVisibility(View.VISIBLE);

                    }
                    else if (i == 3) {
                        JSONObject child1 = children.getJSONObject(0);
                        child1CheckBox.setText(child1.optString("name", ""));
                        child1CheckBox.setVisibility(View.VISIBLE);
                        JSONObject child2 = children.getJSONObject(1);
                        child2CheckBox.setText(child2.optString("name", ""));
                        child2CheckBox.setVisibility(View.VISIBLE);
                        JSONObject child3 = children.getJSONObject(2);
                        child3CheckBox.setText(child3.optString("name", ""));
                        child3CheckBox.setVisibility(View.VISIBLE);
                    }
                    else if (i == 4) {
                        JSONObject child1 = children.getJSONObject(0);
                        child1CheckBox.setText(child1.optString("name", ""));
                        child1CheckBox.setVisibility(View.VISIBLE);
                        JSONObject child2 = children.getJSONObject(1);
                        child2CheckBox.setText(child2.optString("name", ""));
                        child2CheckBox.setVisibility(View.VISIBLE);
                        JSONObject child3 = children.getJSONObject(2);
                        child3CheckBox.setText(child3.optString("name", ""));
                        child3CheckBox.setVisibility(View.VISIBLE);
                        JSONObject child4 = children.getJSONObject(3);
                        child4CheckBox.setText(child4.optString("name", ""));
                        child4CheckBox.setVisibility(View.VISIBLE);
                    }
                }
                catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        if (productsAdapter.getCount() > 5)
        {
            View item = productsAdapter.getView(0, null, productsListView);
            item.measure(0, 0);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (5 * item.getMeasuredHeight()));
            p.setMargins(60, 0,60, 0);
            p.addRule(RelativeLayout.BELOW, R.id.add_top);
            productsListView.setLayoutParams(p);
        }

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductActivity.this.onBackPressed();
            }
        });

        date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddProductActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateEditText = (EditText) findViewById(R.id.date_ed);
                        dateEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        time_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddProductActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeEditText = (EditText) findViewById(R.id.time_ed);
                        timeEditText.setText(String.valueOf(selectedHour)
                                + " : " + String.valueOf(selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateEditText = (EditText) findViewById(R.id.date_ed);
                timeEditText = (EditText) findViewById(R.id.time_ed);
                quantityEditText = (EditText) findViewById(R.id.quantity_ed);
                CheckBox child1CheckBox = (CheckBox) findViewById(R.id.child1);
                if(dateEditText.getText().toString().trim().equals("") || timeEditText.getText().toString().trim().equals("") || quantityEditText.getText().toString().trim().equals("") || !child1CheckBox.isChecked())
                {
                    Toast.makeText(AddProductActivity.this, "Fill all 3 fields & check at least 1 child in order to Save the product", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AddProductActivity.this.onBackPressed();
                }
            }
        });

        ImageView new_product = (ImageView) findViewById(R.id.add_product);
        new_product.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog new_prod_dialog = new Dialog(context);
                new_prod_dialog.setContentView(R.layout.new_product_dialog);
                new_prod_dialog.setTitle("Add New Product...");

                Button dialogButton = (Button) new_prod_dialog.findViewById(R.id.submit);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText new_prod_name = (EditText) new_prod_dialog.findViewById(R.id.new_prod_ed);
                        EditText quantity = (EditText) new_prod_dialog.findViewById(R.id.sugar_ed);

                        if(new_prod_name.getText().toString().trim().equals("") || quantity.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(AddProductActivity.this, "Fill both fields in order to Submit new product", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            new_prod_dialog.dismiss();
                        }
                    }
                });

                new_prod_dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
