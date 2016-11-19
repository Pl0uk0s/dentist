package com.trelokopoi.dentist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.trelokopoi.dentist.dbutil.diaryDataSource;
import com.trelokopoi.dentist.dbutil.diaryObj;
import com.trelokopoi.dentist.util.ActivityLoader;
import com.trelokopoi.dentist.util.AsyncApiCall;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.L;
import com.trelokopoi.dentist.util.LocalStorage;
import com.trelokopoi.dentist.util.OnSwipeTouchListener;
import com.trelokopoi.dentist.util.Tools;
import com.trelokopoi.dentist.util.WebApi;
import com.trelokopoi.dentist.util.WebInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    private Integer LOAD_CHILD_DATA1 = 1;
    private Integer LOAD_CHILD_DATA2 = 2;
    private Integer LOAD_CHILD_DATA3 = 3;
    private Integer LOAD_CHILD_DATA4 = 4;

    private Integer CHILD1_HAS_DATA = 0;
    private Integer CHILD2_HAS_DATA = 0;
    private Integer CHILD3_HAS_DATA = 0;
    private Integer CHILD4_HAS_DATA = 0;

    private int DELETE_PRODUCT = 0;

    final private Context ctx = this;

    private String currentDate;
    private Date valid_date;
    private Integer backButtonCount = 0;
    private ImageView edit, delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tools.setupGoogleAnalytics(MainActivity.this);

        sendDataToBackend();

        ScrollView children_scrollview = (ScrollView)findViewById(R.id.children_scrollview);

        JSONArray children = LocalStorage.getChildren();

        TextView dateTxtView = (TextView) findViewById(R.id.date);
        currentDate = LocalStorage.getDayForInfo();
        dateTxtView.setText(currentDate);

        ImageView next = (ImageView) findViewById(R.id.next);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date curr_date = df.parse(currentDate);
            Calendar cal = Calendar.getInstance();
            String validDateUntil = df.format(cal.getTime());
            valid_date = df.parse(validDateUntil);
            if (curr_date.equals((valid_date)))
            {
                next.setVisibility(View.INVISIBLE);
            }
        } catch (ParseException e) {
            L.debug(e.toString());
        }

        TextView child_name1 = (TextView) findViewById(R.id.child_name1);
        TextView child_name2 = (TextView) findViewById(R.id.child_name2);
        TextView child_name3 = (TextView) findViewById(R.id.child_name3);
        TextView child_name4 = (TextView) findViewById(R.id.child_name4);

        LinearLayout llchild1 = (LinearLayout) findViewById(R.id.child1);
        LinearLayout llchild2 = (LinearLayout) findViewById(R.id.child2);
        LinearLayout llchild3 = (LinearLayout) findViewById(R.id.child3);
        LinearLayout llchild4 = (LinearLayout) findViewById(R.id.child4);

        Integer i = children.length();

        try {
            if (i == 1) {
                JSONObject child1 = children.getJSONObject(0);
                llchild1.setVisibility(View.VISIBLE);
                child_name1.setVisibility(View.VISIBLE);
                child_name1.setText(child1.optString("name", ""));
            }
            else if (i == 2) {
                JSONObject child1 = children.getJSONObject(0);
                llchild1.setVisibility(View.VISIBLE);
                child_name1.setVisibility(View.VISIBLE);
                child_name1.setText(child1.optString("name", ""));

                JSONObject child2 = children.getJSONObject(1);
                llchild2.setVisibility(View.VISIBLE);
                child_name2.setVisibility(View.VISIBLE);
                child_name2.setText(child2.optString("name", ""));
            }
            else if (i == 3) {

                JSONObject child1 = children.getJSONObject(0);
                llchild1.setVisibility(View.VISIBLE);
                child_name1.setVisibility(View.VISIBLE);
                child_name1.setText(child1.optString("name", ""));

                JSONObject child2 = children.getJSONObject(1);
                llchild2.setVisibility(View.VISIBLE);
                child_name2.setVisibility(View.VISIBLE);
                child_name2.setText(child2.optString("name", ""));

                JSONObject child3 = children.getJSONObject(2);
                llchild3.setVisibility(View.VISIBLE);
                child_name3.setVisibility(View.VISIBLE);
                child_name3.setText(child3.optString("name", ""));
            }
            else if (i == 4) {
                JSONObject child1 = children.getJSONObject(0);
                llchild1.setVisibility(View.VISIBLE);
                child_name1.setVisibility(View.VISIBLE);
                child_name1.setText(child1.optString("name", ""));

                JSONObject child2 = children.getJSONObject(1);
                llchild2.setVisibility(View.VISIBLE);
                child_name2.setVisibility(View.VISIBLE);
                child_name2.setText(child2.optString("name", ""));

                JSONObject child3 = children.getJSONObject(2);
                llchild3.setVisibility(View.VISIBLE);
                child_name3.setVisibility(View.VISIBLE);
                child_name3.setText(child3.optString("name", ""));

                JSONObject child4 = children.getJSONObject(3);
                llchild4.setVisibility(View.VISIBLE);
                child_name4.setVisibility(View.VISIBLE);
                child_name4.setText(child4.optString("name", ""));
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int j=0; j<i; j++) {
            try {
                JSONObject child = children.getJSONObject(j);
                JSONObject Response = WebApi.getChildInfo(MainActivity.this, LocalStorage.getDayForInfo(), child.optInt("id", -1));
                LinearLayout child_data = null;
                if (j == 0) {
                    child_data = (LinearLayout) findViewById(R.id.child1_data);
                    CHILD1_HAS_DATA = 1;
                }
                else if (j == 1) {
                    child_data = (LinearLayout) findViewById(R.id.child2_data);
                    CHILD2_HAS_DATA = 1;
                }
                else if (j == 2) {
                    child_data = (LinearLayout) findViewById(R.id.child3_data);
                    CHILD3_HAS_DATA = 1;
                }
                else if (j == 3) {
                    child_data = (LinearLayout) findViewById(R.id.child4_data);
                    CHILD4_HAS_DATA = 1;
                }

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                JSONArray childData = Response.optJSONArray("childData");

                for(int k = 0; k < childData.length(); k++) {
                    try {
                        JSONObject oneFood = childData.getJSONObject(k);
                        String time = oneFood.optString("time", "");
                        String food = oneFood.optString("food", "");
                        String diaryId = oneFood.optString("diaryId", "");
                        String amount = oneFood.optString("amount", "");
                        final LinearLayout child_detail;
                        child_detail = getChildDetail(time, food, diaryId, amount);

                        child_detail.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
                            public void onSwipeTop() {
                                //Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
                            }
                            public void onSwipeRight() {
                                //Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                                swipeRight();
                            }
                            public void onSwipeLeft() {
                                //Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                                swipeLeft();
                            }
                            public void onSwipeBottom() {
                                //Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                            }
                            public void onLongClick() {
                                child_detail.setBackgroundColor(ContextCompat.getColor(ctx, R.color.blue));
                                String[] bArray = new String[child_detail.getChildCount()];

                                for (int i = 0; i < child_detail.getChildCount(); i++) {
                                    TextView children = (TextView) child_detail.getChildAt(i);
                                    String b = children.getText().toString();
                                    bArray[i] = b;
                                }
                                new AsyncApiCall(DELETE_PRODUCT, MainActivity.this, false).execute(WebApi.deleteProductFromUser(bArray[3]));
                            }

                        });

                        child_data.addView(child_detail, p);
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        child_name1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CHILD1_HAS_DATA == 0) {
                    JSONArray children = LocalStorage.getChildren();
                    try {
                        JSONObject child1 = children.getJSONObject(0);
                        new AsyncApiCall(LOAD_CHILD_DATA1, MainActivity.this, true, "Loading...").execute(WebApi.getChildInfo(LocalStorage.getDayForInfo(), child1.optInt("id", -1)));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else {
                    LinearLayout child1_data = (LinearLayout) findViewById(R.id.child1_data);

                    if (child1_data.getVisibility() == View.VISIBLE)
                    {
                        child1_data.setVisibility(View.GONE);
                    }
                    else
                    {
                        child1_data.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        child_name2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CHILD2_HAS_DATA == 0) {
                    JSONArray children = LocalStorage.getChildren();
                    try {
                        JSONObject child2 = children.getJSONObject(1);
                        new AsyncApiCall(LOAD_CHILD_DATA2, MainActivity.this, true, "Loading...").execute(WebApi.getChildInfo(LocalStorage.getDayForInfo(), child2.optInt("id", -1)));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else {
                    LinearLayout child2_data = (LinearLayout) findViewById(R.id.child2_data);

                    if (child2_data.getVisibility() == View.VISIBLE)
                    {
                        child2_data.setVisibility(View.GONE);
                    }
                    else
                    {
                        child2_data.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        child_name3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CHILD3_HAS_DATA == 0) {
                    JSONArray children = LocalStorage.getChildren();
                    try {
                        JSONObject child3 = children.getJSONObject(2);
                        new AsyncApiCall(LOAD_CHILD_DATA3, MainActivity.this, true, "Loading...").execute(WebApi.getChildInfo(LocalStorage.getDayForInfo(), child3.optInt("id", -1)));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else {
                    LinearLayout child3_data = (LinearLayout) findViewById(R.id.child3_data);

                    if (child3_data.getVisibility() == View.VISIBLE)
                    {
                        child3_data.setVisibility(View.GONE);
                    }
                    else
                    {
                        child3_data.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        child_name4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CHILD4_HAS_DATA == 0) {
                    JSONArray children = LocalStorage.getChildren();
                    try {
                        JSONObject child4 = children.getJSONObject(3);
                        new AsyncApiCall(LOAD_CHILD_DATA4, MainActivity.this, true, "Loading...").execute(WebApi.getChildInfo(LocalStorage.getDayForInfo(), child4.optInt("id", -1)));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else {
                    LinearLayout child4_data = (LinearLayout) findViewById(R.id.child4_data);

                    if (child4_data.getVisibility() == View.VISIBLE)
                    {
                        child4_data.setVisibility(View.GONE);
                    }
                    else
                    {
                        child4_data.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        ImageView previous = (ImageView) findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRight();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeLeft();
            }
        });

        ImageView add_product = (ImageView) findViewById(R.id.add_product);
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                intent.putExtra("date", currentDate);
                startActivity(intent);
                finish();
            }
        });

        ImageView settings = (ImageView) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        children_scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        children_scrollview.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                //Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                swipeRight();
            }
            public void onSwipeLeft() {
                //Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                swipeLeft();
            }
            public void onSwipeBottom() {
                //Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private void swipeRight() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = (Date) df.parse(currentDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, -1);
            String dateToSave = df.format(cal.getTime());
            LocalStorage.setDayForInfo(dateToSave);
        } catch (ParseException e) {
            L.debug(e.toString());
        }
        Tools.startNewActivityLeft(MainActivity.this, MainActivity.class);
    }

    private void swipeLeft() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = df.parse(currentDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            String dateToSave = df.format(cal.getTime());
            Date newDate = df.parse(dateToSave);
            if (!newDate.after(valid_date))
            {
                LocalStorage.setDayForInfo(dateToSave);
                Tools.startNewActivityRight(MainActivity.this, MainActivity.class);
            }

        } catch (ParseException e) {
            L.debug(e.toString());
        }
    }

    private LinearLayout getChildDetail(String time, String food, String diaryId, String amount) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout child1_detail = new LinearLayout(ctx);
        child1_detail.setOrientation(LinearLayout.HORIZONTAL);
        child1_detail.setPadding(50, 10, 10, 20);

        TextView view1 = new TextView(ctx);
        TextView view2 = new TextView(ctx);
        TextView view3 = new TextView(ctx);
        view2.setPadding(25, 0, 0, 0);
        view3.setPadding(25, 0, 0, 0);

        TextView view4 = new TextView(ctx);
        view4.setVisibility(View.GONE);

        Integer length = food.length();
        String food30chars;
        if (length > 25)
        {
            String more = "...";
            food30chars = food.substring(0, 25) + more;
        }
        else
        {
            food30chars = food;
        }

        if (Build.VERSION.SDK_INT < 23) {
            view1.setTextAppearance(getApplicationContext(), R.style.boldText);
            view2.setTextAppearance(getApplicationContext(), R.style.boldText);
        } else {
            view1.setTextAppearance(R.style.boldText);
            view2.setTextAppearance(R.style.boldText);
        }

        view1.setText(time);
        view2.setText(amount);
        view3.setText(food30chars);
        view4.setText(diaryId);

        view1.setTextSize(16);
        view2.setTextSize(16);
        view3.setTextSize(16);
//        view1.setTextColor(0x000000);
//        view2.setTextColor(0x000000);
//        view3.setTextColor(0x000000);
        child1_detail.addView(view1, p);
        child1_detail.addView(view2, p);
        child1_detail.addView(view3, p);
        child1_detail.addView(view4, p);

        return child1_detail;
    }

    @Override
    public void onTaskCompleted(int thread, String result) {
        JSONObject jsonResult = WebInterface.validateJSON(MainActivity.this, result);

        if (jsonResult != null) {
            if (thread == LOAD_CHILD_DATA1) {
                LinearLayout child1_data = (LinearLayout) findViewById(R.id.child1_data);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout child1_detail;

                JSONArray childData = jsonResult.optJSONArray("childData");

                for(int i = 0; i < childData.length(); i++) {
                    try {
                        JSONObject oneFood = childData.getJSONObject(i);
                        String time = oneFood.optString("time", "");
                        String food = oneFood.optString("food", "");
                        String diaryId = oneFood.optString("diaryId", "");
                        String amount = oneFood.optString("amount", "");
                        child1_detail = getChildDetail(time, food, diaryId, amount);
                        child1_data.addView(child1_detail, p);
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                CHILD1_HAS_DATA = 1;
            }
            else if (thread == LOAD_CHILD_DATA2) {
                LinearLayout child2_data = (LinearLayout) findViewById(R.id.child2_data);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout child2_detail;

                JSONArray childData = jsonResult.optJSONArray("childData");

                for(int i = 0; i < childData.length(); i++) {
                    try {
                        JSONObject oneFood = childData.getJSONObject(i);
                        String time = oneFood.optString("time", "");
                        String food = oneFood.optString("food", "");
                        String diaryId = oneFood.optString("diaryId", "");
                        String amount = oneFood.optString("amount", "");
                        child2_detail = getChildDetail(time, food, diaryId, amount);
                        child2_data.addView(child2_detail, p);
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                CHILD2_HAS_DATA = 1;
            }
            else if (thread == LOAD_CHILD_DATA3) {
                LinearLayout child3_data = (LinearLayout) findViewById(R.id.child3_data);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout child3_detail;

                JSONArray childData = jsonResult.optJSONArray("childData");

                for(int i = 0; i < childData.length(); i++) {
                    try {
                        JSONObject oneFood = childData.getJSONObject(i);
                        String time = oneFood.optString("time", "");
                        String food = oneFood.optString("food", "");
                        String diaryId = oneFood.optString("diaryId", "");
                        String amount = oneFood.optString("amount", "");
                        child3_detail = getChildDetail(time, food, diaryId, amount);
                        child3_data.addView(child3_detail, p);
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                CHILD3_HAS_DATA = 1;
            }
            else if (thread == LOAD_CHILD_DATA4) {
                LinearLayout child4_data = (LinearLayout) findViewById(R.id.child4_data);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout child4_detail;

                JSONArray childData = jsonResult.optJSONArray("childData");

                for(int i = 0; i < childData.length(); i++) {
                    try {
                        JSONObject oneFood = childData.getJSONObject(i);
                        String time = oneFood.optString("time", "");
                        String food = oneFood.optString("food", "");
                        String diaryId = oneFood.optString("diaryId", "");
                        String amount = oneFood.optString("amount", "");
                        child4_detail = getChildDetail(time, food, diaryId, amount);
                        child4_data.addView(child4_detail, p);
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                CHILD4_HAS_DATA = 1;
            }
            else if (thread == DELETE_PRODUCT) {
                String success = jsonResult.optString("success", "0");
                if (success.equals("1")) {
                    Toast.makeText(getApplicationContext(), "The product has been deleted.", Toast.LENGTH_LONG).show();
                }
                else if (success.equals("0")) {
                    Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }
        else
        {
            Toast.makeText(this, "Press Back once again to exit the Application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    public void sendDataToBackend() {
        if (WebInterface.hasInternetConnection()) {
            ArrayList<diaryObj> aDiaryObjs = diaryDataSource.retrieveDiaryObj(this.getApplicationContext());
            for (diaryObj obj : aDiaryObjs) {
                Integer id = obj.getId();
                Integer prodId = obj.getProdId();
                Integer quantity = obj.getQuantity();
                String date = obj.getDate();
                String time = obj.getTime();
                Integer belongs = obj.getBelongs();

                //async call
                JSONObject Response = WebApi.addProductToChild(prodId, quantity, date, time, belongs);
                try {
                    String success = (String) Response.getString("success");

                    if (success.equals("1")) {
                        diaryDataSource.deleteOld(this.getApplicationContext(), id);
                    }
                    else {
                        L.debug(Response.toString());
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}