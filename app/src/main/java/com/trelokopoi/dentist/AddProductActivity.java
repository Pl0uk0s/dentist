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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.trelokopoi.dentist.dbutil.diaryDataSource;
import com.trelokopoi.dentist.dbutil.diaryObj;
import com.trelokopoi.dentist.util.AddProductToChild;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddProductActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    private ListView productsListView;
    private ProductAdapter adapter;
    private EditText inputSearch;
    private String add_product_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        add_product_date = getIntent().getStringExtra("date");

        final LinearLayout prod_detail_layout = (LinearLayout) findViewById(R.id.prod_detail_layout);
        productsListView = (ListView) findViewById(R.id.products_list);
        inputSearch = (EditText) findViewById(R.id.search_product);

        inputSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    productsListView.setVisibility(View.VISIBLE);
                } else {
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

                        HashMap<String, String> hmProducts = new HashMap<String, String>();
                        for (int i = 0; i < products.length(); i++) {
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
                } else {
                    HashMap<String, String> hmProducts = new HashMap<String, String>();
                    adapter = new ProductAdapter(hmProducts);
                    productsListView.setAdapter(adapter);
                }


                // When user changed the Text
                RelativeLayout.LayoutParams p;
                if (adapter.getCount() > 5) {
                    View item = adapter.getView(0, null, productsListView);
                    item.measure(0, 0);
                    productsListView.setVisibility(View.VISIBLE);
                    p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (5 * item.getMeasuredHeight()));
                    p.setMargins(60, 0, 60, 0);
                    p.addRule(RelativeLayout.BELOW, R.id.add_top);
                    productsListView.setLayoutParams(p);
                } else if (adapter.getCount() != 0) {
                    View item = adapter.getView(0, null, productsListView);
                    item.measure(0, 0);
                    productsListView.setVisibility(View.VISIBLE);
                    p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    p.setMargins(60, 0, 60, 0);
                    p.addRule(RelativeLayout.BELOW, R.id.add_top);
                    productsListView.setLayoutParams(p);
                } else {
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

        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap.Entry<String, String> selectedItem = adapter.getItem(position);
                inputSearch.setText(selectedItem.getValue());

                Tools.hideKeyboard(AddProductActivity.this);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Intent intent = new Intent(AddProductActivity.this, ProductToChildrenActivity.class);
                Bundle extras = new Bundle();
                extras.putString("date", add_product_date);
                extras.putString("productName", selectedItem.getValue());
                extras.putString("productId", selectedItem.getKey());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddProductActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        RelativeLayout new_product = (RelativeLayout) findViewById(R.id.add_new_product);
        new_product.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

//                final Dialog new_prod_dialog = new Dialog(context, R.style.DialogTheme);
//                new_prod_dialog.setContentView(R.layout.new_product_dialog);
//                new_prod_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//
//                Button dialogButton = (Button) new_prod_dialog.findViewById(R.id.submit);
//                // if button is clicked, close the custom dialog
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        EditText new_prod_name = (EditText) new_prod_dialog.findViewById(R.id.new_prod_ed);
//                        EditText sugar_per_100 = (EditText) new_prod_dialog.findViewById(R.id.sugar_per_100_ed);
//
//                        if (new_prod_name.getText().toString().trim().equals("") || sugar_per_100.getText().toString().trim().equals("")) {
//                            Toast.makeText(AddProductActivity.this, "Fill the product name & sugar per 100 gr/ml in order to Submit new product", Toast.LENGTH_SHORT).show();
//                        } else {
//                            new AsyncApiCall(ADD_PRODUCT, AddProductActivity.this, false).execute(WebApi.addProduct(new_prod_name.getText().toString(), sugar_per_100.getText().toString()));
//                            new_prod_dialog.dismiss();
//                        }
//                    }
//                });
//
//                new_prod_dialog.show();
                Intent i = new Intent(AddProductActivity.this, NewProductActivity.class);
                startActivity(i);
            }
        });

        }

    @Override
    public void onBackPressed() {
            Intent i = new Intent(AddProductActivity.this, MainActivity.class);
            startActivity(i);
            finish();
    }

    @Override
    public void onTaskCompleted(int thread, String result) {

    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {

    }
}
