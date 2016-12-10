package com.kieslog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kieslog.util.AsyncApiCallOnTaskCompleted;
import com.kieslog.util.Fonts;
import com.kieslog.util.L;
import com.kieslog.util.LocalStorage;
import com.kieslog.util.ProductAdapter;
import com.kieslog.util.RecentProductAdapter;
import com.kieslog.util.Tools;
import com.kieslog.util.WebApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class AddProductActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    private ListView productsListView;
    private ProductAdapter adapter;
    private EditText inputSearch;
    private String add_product_date;
    private View line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Typeface latoRegular = Fonts.returnFont(this, Fonts.LATO_REGULAR);
        Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);

        TextView headerTextView = (TextView)findViewById(R.id.add_product_title);
        headerTextView.setTypeface(latoBold);

        TextView addNewProdTextView = (TextView)findViewById(R.id.add_new_product_tv);
        addNewProdTextView.setTypeface(latoRegular);

        TextView recentTextView = (TextView)findViewById(R.id.recent_title);
        recentTextView.setTypeface(latoRegular);

        add_product_date = getIntent().getStringExtra("date");

        final RelativeLayout new_product = (RelativeLayout) findViewById(R.id.add_new_product);
        productsListView = (ListView) findViewById(R.id.products_list);
        inputSearch = (EditText) findViewById(R.id.search_ed);
        inputSearch.setTypeface(latoRegular);
        line = findViewById(R.id.view1);

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
//        Bundle extras = getIntent().getExtras();
//        String productName = extras.getString("productName");
//        if (productName != null && !productName.equals("")) {
//            inputSearch.setText(productName);
//        }

        /**
         * Enabling Search Filter
         **/
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() > 1) {
                    //api call
                    JSONObject Response = WebApi.getProducts(cs.toString());
                    try {
                        String showMsgBox = (String) Response.getString("showMsgBox");
                        if (showMsgBox.equals("1")) {
                            L.debug(App.TAG, "show msgbox");
                            String msg = (String) Response.getString("msg");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
                }
                else {
                    HashMap<String, String> hmProducts = new HashMap<String, String>();
                    adapter = new ProductAdapter(hmProducts);
                    productsListView.setAdapter(adapter);
                }

                final float scale = getResources().getDisplayMetrics().density;
                final int pixels = (int) (50 * scale + 0.5f);
                final int pixels2 = (int) (10 * scale + 0.5f);
                // When user changed the Text
                RelativeLayout.LayoutParams p;
                if (adapter.getCount() > 5) {
                    new_product.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.VISIBLE);
                    View item = adapter.getView(0, null, productsListView);
                    item.measure(0, 0);
                    productsListView.setVisibility(View.VISIBLE);
//                    p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (5 * pixels));
                    p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (5 * item.getMeasuredHeight()));
                    p.setMargins(pixels2, 0, pixels2, 0);
                    p.addRule(RelativeLayout.BELOW, R.id.view1);
                    productsListView.setLayoutParams(p);
                    productsListView.setDivider(null);
                    productsListView.setDividerHeight(0);
                } else if (adapter.getCount() != 0 && adapter.getCount() <= 5) {
                    new_product.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.VISIBLE);
                    View item = adapter.getView(0, null, productsListView);
                    item.measure(0, 0);
                    productsListView.setVisibility(View.VISIBLE);
                    p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, pixels);
                    p.setMargins(pixels2, 0, pixels2, 0);
                    p.addRule(RelativeLayout.BELOW, R.id.view1);
                    productsListView.setLayoutParams(p);
                    productsListView.setDivider(null);
                    productsListView.setDividerHeight(0);
                } else {
                    line.setVisibility(View.GONE);
                    new_product.setVisibility(View.VISIBLE);
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

        new_product.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(AddProductActivity.this, NewProductActivity.class);
                i.putExtra("date", add_product_date);
                startActivity(i);
                finish();
            }
        });

        JSONObject Response = WebApi.getTop10();
        try {
            JSONArray products = Response.getJSONArray("products");
            HashMap<String, String> hmProducts = new HashMap<String, String>();
            for (int i = 0; i < products.length(); i++) {
                String value = products.getJSONObject(i).getString("name");
                String name = products.getJSONObject(i).getString("id");
                hmProducts.put(name, value);
            }

            final RecentProductAdapter recentAdapter = new RecentProductAdapter(hmProducts);

            ListView listView = (ListView) findViewById(R.id.recent_list);
            listView.setAdapter(recentAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap.Entry<String, String> selectedItem = recentAdapter.getItem(position);
//                    inputSearch.setText(selectedItem.getValue());

                    Intent intent = new Intent(AddProductActivity.this, ProductToChildrenActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("date", add_product_date);
                    extras.putString("productName", selectedItem.getValue());
                    extras.putString("productId", selectedItem.getKey());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
