package com.trelokopoi.dentist;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.trelokopoi.dentist.dbutil.diaryDataSource;
import com.trelokopoi.dentist.dbutil.diaryObj;
import com.trelokopoi.dentist.util.AddProductToChild;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.LocalStorage;
import com.trelokopoi.dentist.util.WebApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ProductToChildrenActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    final Context context = this;
    private EditText timeEditText, quantityEditText;
    private String add_product_date, productId, diaryId, amount, productName;
    private JSONArray childrenIds;
    MyCustomAdapter dataAdapter = null;
    ArrayList<AddProductToChild> AddProductToChildList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_to_children);

        Bundle extras = getIntent().getExtras();
        add_product_date = extras.getString("date");
        productName = extras.getString("productName");
        productId = extras.getString("productId");
        diaryId = extras.getString("diaryId");
        amount = extras.getString("amount");

        Integer length = productName.length();
        String food30chars;
        if (length > 30)
        {
            String more = "...";
            food30chars = productName.substring(0, 30) + more;
        }
        else
        {
            food30chars = productName;
        }

        TextView titleTextView = (TextView) findViewById(R.id.product_to_children_header);
        titleTextView.setText(food30chars);

        quantityEditText = (EditText) findViewById(R.id.quantity_ed);
        if (diaryId != null && !diaryId.equals("")) {
            quantityEditText.setText(amount);
        }

        timeEditText = (EditText) findViewById(R.id.time_ed);
        if (diaryId != null && !diaryId.equals("")) {
            timeEditText.setText(add_product_date);
        }
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
            }
        });

        if (diaryId != null && !diaryId.equals("")) {
            JSONObject Response = WebApi.getOtherChildrenForSameEntry(ProductToChildrenActivity.this, diaryId);
            try {
                String success = (String) Response.getString("success");
                if (success.equals("1")) {
                    childrenIds = Response.optJSONArray("childrenIds");
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        displayListView();

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductToChildrenActivity.this.onBackPressed();
            }
        });

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeEditText = (EditText) findViewById(R.id.time_ed);
                quantityEditText = (EditText) findViewById(R.id.quantity_ed);


                AddProductToChild AddProductToChildFinal;
                Boolean aChildIsChecked = false;

                for (int i = 0; i < AddProductToChildList.size(); i++)
                {
                    AddProductToChildFinal = AddProductToChildList.get(i);
                    if(AddProductToChildFinal.isSelected()) {
                        aChildIsChecked = true;
                    }
                }

                if(timeEditText.getText().toString().trim().equals("") || quantityEditText.getText().toString().trim().equals("") || !(aChildIsChecked))
                {
                    Toast.makeText(ProductToChildrenActivity.this, "Fill both fields & check at least 1 child in order to Save the product", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    JSONArray children = LocalStorage.getChildren();

                    diaryObj object = new diaryObj();

                    String diaryDate = add_product_date.replaceAll("/", "-");

                    for (int i = 0; i < AddProductToChildList.size(); i++)
                    {
                        try {
                            AddProductToChildFinal = AddProductToChildList.get(i);
                            if(AddProductToChildFinal.isSelected()) {
                                JSONObject child = children.getJSONObject(i);
                                object.setProdId(Integer.parseInt(productId));
                                object.setDate(diaryDate);
                                object.setTime(timeEditText.getText().toString());
                                object.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
                                object.setBelongs(child.optInt("id"));
                                diaryDataSource.createDiaryObj(context, object);
                            }
                        }catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    Intent i = new Intent(ProductToChildrenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        });
    }

    private boolean userexists(JSONArray jsonArray, int useridToFind){
        return jsonArray.toString().contains("\""+useridToFind+"\"");
    }

    @Override
    public void onBackPressed() {
        Intent i;
        if (diaryId != null && !diaryId.equals("")) {
            i = new Intent(ProductToChildrenActivity.this, MainActivity.class);
        }
        else {
            i = new Intent(ProductToChildrenActivity.this, AddProductActivity.class);
            i.putExtra("productName", productName);
        }
        startActivity(i);
        finish();
    }

    public void showTimeDialog() {
        int hour, minute;
        if (timeEditText.getText().toString().equals("")) {
            Calendar mcurrentTime = Calendar.getInstance();
            hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            minute = mcurrentTime.get(Calendar.MINUTE);
        }
        else {
            String[] time = timeEditText.getText().toString().split(":");
            hour = Integer.parseInt(time[0]);
            minute = Integer.parseInt(time[1]);
        }
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ProductToChildrenActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeEditText.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                if (ProductToChildrenActivity.this.getCurrentFocus() != null)
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ProductToChildrenActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void displayListView() {

        AddProductToChildList = new ArrayList<AddProductToChild>();
        final JSONArray children = LocalStorage.getChildren();
        Integer chlength = children.length();
        try {
            for (int i = 0; i < chlength; i++) {
                JSONObject child = children.getJSONObject(i);
                AddProductToChild AddProductToChild = new AddProductToChild(child.optString("name", ""),false);
                AddProductToChildList.add(AddProductToChild);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dataAdapter = new MyCustomAdapter(this,
                R.layout.add_to_child_list_item, AddProductToChildList);
        ListView listView = (ListView) findViewById(R.id.childrenListView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                CheckBox cb = (CheckBox)view.findViewById(R.id.childCheckBox);
                AddProductToChild AddProductToChild = (AddProductToChild) parent.getItemAtPosition(position);
                if (cb.isChecked())
                {
                    cb.setChecked(false);
                    AddProductToChild.setSelected(false);
                }
                else
                {
                    cb.setChecked(true);
                    AddProductToChild.setSelected(true);

                }
            }
        });
    }

    private class MyCustomAdapter extends ArrayAdapter<AddProductToChild> {

        private ArrayList<AddProductToChild> AddProductToChildList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<AddProductToChild> AddProductToChildList) {
            super(context, textViewResourceId, AddProductToChildList);
            this.AddProductToChildList = new ArrayList<AddProductToChild>();
            this.AddProductToChildList.addAll(AddProductToChildList);
        }

        private class ViewHolder {
            TextView childName;
            CheckBox checkBox;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            MyCustomAdapter.ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.add_to_child_list_item, null);

                holder = new MyCustomAdapter.ViewHolder();
                holder.childName = (TextView) convertView.findViewById(R.id.childName);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.childCheckBox);
                convertView.setTag(holder);

                holder.checkBox.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        AddProductToChild AddProductToChild = (AddProductToChild) cb.getTag();
                        if (cb.isChecked())
                        {
                            AddProductToChild.setSelected(true);
                        }
                        else
                        {
                            AddProductToChild.setSelected(false);
                        }
                    }
                });
            }
            else {
                holder = (MyCustomAdapter.ViewHolder) convertView.getTag();
            }

            AddProductToChild AddProductToChild = AddProductToChildList.get(position);
            holder.childName.setText(AddProductToChild.getChildName());
            holder.checkBox.setChecked(AddProductToChild.isSelected());
            holder.checkBox.setTag(AddProductToChild);

            if (diaryId != null && !diaryId.equals("")) {
                try {
                    JSONArray children = LocalStorage.getChildren();
                    for(int i = 0; i < children.length(); i++) {
                        JSONObject child = children.getJSONObject(i);
                        String childname = child.optString("name", "");
                        int childid = child.optInt("id", 0);
                        if (childname.equals(AddProductToChild.getChildName()) && userexists(childrenIds, childid)) {
                            AddProductToChild.setSelected(true);
                            holder.checkBox.setChecked(true);
                            holder.checkBox.setEnabled(false);
                        }
                        else {
                            holder.checkBox.setEnabled(false);
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return convertView;

        }

    }

    @Override
    public void onTaskCompleted(int thread, String result) {

    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {

    }
}
