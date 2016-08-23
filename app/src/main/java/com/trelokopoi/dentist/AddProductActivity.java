package com.trelokopoi.dentist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

import com.trelokopoi.dentist.dbutil.diaryDataSource;
import com.trelokopoi.dentist.dbutil.diaryObj;
import com.trelokopoi.dentist.util.AsyncApiCall;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.L;
import com.trelokopoi.dentist.util.LocalStorage;
import com.trelokopoi.dentist.util.ProductAdapter;
import com.trelokopoi.dentist.util.Tools;
import com.trelokopoi.dentist.util.WebApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddProductActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    final Context context = this;
    private int ADD_PRODUCT = 0;
    private ListView productsListView;
    private ProductAdapter adapter;
    private EditText inputSearch, dateEditText, timeEditText, quantityEditText, productId;
    private ImageView date_img, time_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

//        String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE"};
        String products[] = {};

        final LinearLayout prod_detail_layout = (LinearLayout) findViewById(R.id.prod_detail_layout);
        productsListView = (ListView) findViewById(R.id.products_list);
        inputSearch = (EditText) findViewById(R.id.search_product);
        productId = (EditText) findViewById(R.id.productId);
        date_img = (ImageView) findViewById(R.id.date_img);
        time_img = (ImageView) findViewById(R.id.time_img);

        inputSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    prod_detail_layout.setVisibility(View.GONE);
                    productsListView.setVisibility(View.VISIBLE);
                }
                else {
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
                if (cs.length() > 3) {
                    //api call
                    JSONObject Response = WebApi.getProducts(cs.toString());
                    try {
                        String showMsgBox = (String) Response.getString("showMsgBox");
                        if (showMsgBox.equals("1")) {
                            L.debug(App.TAG, "show msgbox");
                            String msg = (String) Response.getString("msg");
                            Tools.toast(getApplicationContext(), msg);
                        }

                        //put the products in the list view
                        String success = (String) Response.getString("success");
                        JSONArray products = Response.optJSONArray("products");
                        LocalStorage.setProducts(products);

                        HashMap<String, String> hmProducts = new HashMap<String,String>();
                        for(int i=0; i<products.length(); i++){
                            String value = products.getJSONObject(i).getString("name");
                            String name = products.getJSONObject(i).getString("id");
                            hmProducts.put(name, value);
                        }

                        adapter = new ProductAdapter(hmProducts);
                        productsListView.setAdapter(adapter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else {
                    HashMap<String, String> hmProducts = new HashMap<String,String>();
                    adapter = new ProductAdapter(hmProducts);
                    productsListView.setAdapter(adapter);
                }


                // When user changed the Text
                RelativeLayout.LayoutParams p;
                if (adapter.getCount() > 5)
                {
                    View item = adapter.getView(0, null, productsListView);
                    item.measure(0, 0);
                    productsListView.setVisibility(View.VISIBLE);
                    p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (5 * item.getMeasuredHeight()));
                    p.setMargins(60, 0,60, 0);
                    p.addRule(RelativeLayout.BELOW, R.id.add_top);
                    productsListView.setLayoutParams(p);
                }
                else if (adapter.getCount() != 0)
                {
                    View item = adapter.getView(0, null, productsListView);
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

                clearValues();

                HashMap.Entry<String, String> selectedItem = adapter.getItem(position);
                inputSearch.setText(selectedItem.getValue());
                productId.setText(selectedItem.getKey());

                Tools.hideKeyboard(AddProductActivity.this);

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                productsListView.setVisibility(View.GONE);
                prod_detail_layout.setVisibility(View.VISIBLE);

                dateEditText = (EditText) findViewById(R.id.date_ed);
                dateEditText.setInputType(EditorInfo.TYPE_NULL);
                dateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDateDialog();
                    }
                });
                dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus){
                            showDateDialog();
                        }
                        else {

                        }
                    }
                });
                dateEditText.requestFocus();

                timeEditText = (EditText) findViewById(R.id.time_ed);
                timeEditText.setInputType(EditorInfo.TYPE_NULL);
                timeEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTimeDialog();
                    }
                });
                timeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus){
                            showTimeDialog();
                        }
                        else {

                        }
                    }
                });

                CheckBox child1CheckBox = (CheckBox) findViewById(R.id.child1checkbox);
                CheckBox child2CheckBox = (CheckBox) findViewById(R.id.child2checkbox);
                CheckBox child3CheckBox = (CheckBox) findViewById(R.id.child3checkbox);
                CheckBox child4CheckBox = (CheckBox) findViewById(R.id.child4checkbox);

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
                showDateDialog();
            }
        });

        time_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTimeDialog();
            }
        });

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateEditText = (EditText) findViewById(R.id.date_ed);
                timeEditText = (EditText) findViewById(R.id.time_ed);
                quantityEditText = (EditText) findViewById(R.id.quantity_ed);
                CheckBox child1CheckBox = (CheckBox) findViewById(R.id.child1checkbox);
                CheckBox child2CheckBox = (CheckBox) findViewById(R.id.child2checkbox);
                CheckBox child3CheckBox = (CheckBox) findViewById(R.id.child3checkbox);
                CheckBox child4CheckBox = (CheckBox) findViewById(R.id.child4checkbox);

                Boolean aChildIsChecked = false;

                if (child1CheckBox.isChecked() || child2CheckBox.isChecked() || child3CheckBox.isChecked() || child4CheckBox.isChecked()) {
                    aChildIsChecked = true;
                }

                if(dateEditText.getText().toString().trim().equals("") || timeEditText.getText().toString().trim().equals("") || quantityEditText.getText().toString().trim().equals("") || !aChildIsChecked)
                {
                    Toast.makeText(AddProductActivity.this, "Fill all 3 fields & check at least 1 child in order to Save the product", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //save to sqllite and go to mainactivity
                    JSONArray children = LocalStorage.getChildren();

                    diaryObj object = new diaryObj();

                    try {
                        if (child1CheckBox.isChecked()) {
                            JSONObject child = children.getJSONObject(0);

                            object.setProdId(Integer.parseInt(productId.getText().toString()));
                            object.setDate(dateEditText.getText().toString());
                            object.setTime(timeEditText.getText().toString());
                            object.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
                            object.setBelongs(child.optInt("id"));

                            diaryDataSource.createDiaryObj(context, object);
                        }
                        if (child2CheckBox.isChecked()) {
                            JSONObject child = children.getJSONObject(1);

                            object.setProdId(Integer.parseInt(productId.getText().toString()));
                            object.setDate(dateEditText.getText().toString());
                            object.setTime(timeEditText.getText().toString());
                            object.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
                            object.setBelongs(child.optInt("id"));

                            diaryDataSource.createDiaryObj(context, object);
                        }
                        if (child3CheckBox.isChecked()) {
                            JSONObject child = children.getJSONObject(2);

                            object.setProdId(Integer.parseInt(productId.getText().toString()));
                            object.setDate(dateEditText.getText().toString());
                            object.setTime(timeEditText.getText().toString());
                            object.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
                            object.setBelongs(child.optInt("id"));

                            diaryDataSource.createDiaryObj(context, object);
                        }
                        if (child4CheckBox.isChecked()) {
                            JSONObject child = children.getJSONObject(3);

                            object.setProdId(Integer.parseInt(productId.getText().toString()));
                            object.setDate(dateEditText.getText().toString());
                            object.setTime(timeEditText.getText().toString());
                            object.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
                            object.setBelongs(child.optInt("id"));

                            diaryDataSource.createDiaryObj(context, object);
                        }
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

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

                        if(new_prod_name.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(AddProductActivity.this, "Fill the name in order to Submit new product", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            new AsyncApiCall(ADD_PRODUCT, AddProductActivity.this, false).execute(WebApi.addProduct(new_prod_name.getText().toString()));
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
        LinearLayout prod_detail_layout = (LinearLayout) findViewById(R.id.prod_detail_layout);
        if (prod_detail_layout.isShown()) {
            Intent i = new Intent(AddProductActivity.this, AddProductActivity.class);
            startActivity(i);
            finish();
        }
        else {
            Intent i = new Intent(AddProductActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void showDateDialog() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(AddProductActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateEditText = (EditText) findViewById(R.id.date_ed);
                dateEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                timeEditText = (EditText) findViewById(R.id.time_ed);
                timeEditText.requestFocus();
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void showTimeDialog() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddProductActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeEditText = (EditText) findViewById(R.id.time_ed);
                timeEditText.setText(String.valueOf(selectedHour) + " : " + String.valueOf(selectedMinute));

                quantityEditText = (EditText) findViewById(R.id.quantity_ed);
                quantityEditText.requestFocus();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void clearValues() {
        dateEditText = (EditText) findViewById(R.id.date_ed);
        timeEditText = (EditText) findViewById(R.id.time_ed);
        quantityEditText = (EditText) findViewById(R.id.quantity_ed);

        dateEditText.setText("");
        timeEditText.setText("");
        quantityEditText.setText("");
    }

    @Override
    public void onTaskCompleted(int thread, String result) {

    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {

    }
}
