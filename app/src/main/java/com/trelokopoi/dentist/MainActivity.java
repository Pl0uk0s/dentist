package com.trelokopoi.dentist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.trelokopoi.dentist.dbutil.diaryDataSource;
import com.trelokopoi.dentist.dbutil.diaryObj;
import com.trelokopoi.dentist.util.ActivityLoader;
import com.trelokopoi.dentist.util.AsyncApiCall;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.Fonts;
import com.trelokopoi.dentist.util.L;
import com.trelokopoi.dentist.util.LocalStorage;
import com.trelokopoi.dentist.util.OnSwipeTouchListener;
import com.trelokopoi.dentist.util.Tools;
import com.trelokopoi.dentist.util.WebApi;
import com.trelokopoi.dentist.util.WebInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements AsyncApiCallOnTaskCompleted {

    private Integer DELETE_PRODUCT = 0;
    private Integer LOAD_CHILD_DATA1 = 1;
    private Integer LOAD_CHILD_DATA2 = 2;
    private Integer LOAD_CHILD_DATA3 = 3;
    private Integer LOAD_CHILD_DATA4 = 4;

    private Integer CHILD1_HAS_DATA = 0;
    private Integer CHILD2_HAS_DATA = 0;
    private Integer CHILD3_HAS_DATA = 0;
    private Integer CHILD4_HAS_DATA = 0;

    final private Context ctx = this;

    private String currentDate;
    private Date valid_date;
    private Integer backButtonCount = 0;
    private ImageView edit, delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_floating);

        Tools.setupGoogleAnalytics(MainActivity.this);

        Typeface latoBold = Fonts.returnFont(this, Fonts.LATO_BOLD);
        Typeface latoRegular = Fonts.returnFont(this, Fonts.LATO_REGULAR);
        Typeface latoItalic = Fonts.returnFont(this, Fonts.LATO_ITALIC);

        TextView main_title = (TextView) findViewById(R.id.main_title);
        main_title.setTypeface(latoRegular);

        TextView today = (TextView) findViewById(R.id.today);
        today.setTypeface(latoBold);

        TextView child_name1 = (TextView) findViewById(R.id.child_name1);
        child_name1.setTypeface(latoBold);

        TextView child1_last_entry = (TextView) findViewById(R.id.child1_last_entry);
        child1_last_entry.setTypeface(latoItalic);

        TextView child_name2 = (TextView) findViewById(R.id.child_name2);
        child_name2.setTypeface(latoBold);

        TextView child2_last_entry = (TextView) findViewById(R.id.child2_last_entry);
        child2_last_entry.setTypeface(latoItalic);

        TextView child_name3 = (TextView) findViewById(R.id.child_name3);
        child_name3.setTypeface(latoBold);

        TextView child3_last_entry = (TextView) findViewById(R.id.child3_last_entry);
        child3_last_entry.setTypeface(latoItalic);

        TextView child_name4 = (TextView) findViewById(R.id.child_name4);
        child_name4.setTypeface(latoBold);

        TextView child4_last_entry = (TextView) findViewById(R.id.child4_last_entry);
        child4_last_entry.setTypeface(latoItalic);



        sendDataToBackend();

        ScrollView children_scrollview = (ScrollView)findViewById(R.id.children_scrollview);

        JSONArray children = LocalStorage.getChildren();

        TextView dateTxtView = (TextView) findViewById(R.id.date);
        TextView todayTxtView = (TextView) findViewById(R.id.today);
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
                dateTxtView.setVisibility(View.INVISIBLE);
                todayTxtView.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            L.debug(e.toString());
        }

        RelativeLayout child1_relative = (RelativeLayout) findViewById(R.id.child1_relative);
        RelativeLayout child2_relative = (RelativeLayout) findViewById(R.id.child2_relative);
        RelativeLayout child3_relative = (RelativeLayout) findViewById(R.id.child3_relative);
        RelativeLayout child4_relative = (RelativeLayout) findViewById(R.id.child4_relative);

        TextView child_lastentry1 = (TextView) findViewById(R.id.child1_last_entry);
        TextView child_lastentry2 = (TextView) findViewById(R.id.child2_last_entry);
        TextView child_lastentry3 = (TextView) findViewById(R.id.child3_last_entry);
        TextView child_lastentry4 = (TextView) findViewById(R.id.child4_last_entry);

        RelativeLayout llchild1 = (RelativeLayout) findViewById(R.id.child1);
        RelativeLayout llchild2 = (RelativeLayout) findViewById(R.id.child2);
        RelativeLayout llchild3 = (RelativeLayout) findViewById(R.id.child3);
        RelativeLayout llchild4 = (RelativeLayout) findViewById(R.id.child4);

        Integer i = children.length();

        try {
            if (i == 1) {
                JSONObject child1 = children.getJSONObject(0);
                llchild1.setVisibility(View.VISIBLE);
                child1_relative.setVisibility(View.VISIBLE);
                child_name1.setText(child1.optString("name", ""));
                child_lastentry1.setText("Last entry at "+child1.optString("lastEntry", ""));

            }
            else if (i == 2) {
                JSONObject child1 = children.getJSONObject(0);
                llchild1.setVisibility(View.VISIBLE);
                child1_relative.setVisibility(View.VISIBLE);
                child_name1.setText(child1.optString("name", ""));
                child_lastentry1.setText("Last entry at "+child1.optString("lastEntry", ""));


                JSONObject child2 = children.getJSONObject(1);
                llchild2.setVisibility(View.VISIBLE);
                child2_relative.setVisibility(View.VISIBLE);
                child_name2.setText(child2.optString("name", ""));
                child_lastentry2.setText("Last entry at "+child2.optString("lastEntry", ""));
            }
            else if (i == 3) {

                JSONObject child1 = children.getJSONObject(0);
                llchild1.setVisibility(View.VISIBLE);
                child1_relative.setVisibility(View.VISIBLE);
                child_name1.setText(child1.optString("name", ""));
                child_lastentry1.setText("Last entry at "+child1.optString("lastEntry", ""));


                JSONObject child2 = children.getJSONObject(1);
                llchild2.setVisibility(View.VISIBLE);
                child2_relative.setVisibility(View.VISIBLE);
                child_name2.setText(child2.optString("name", ""));
                child_lastentry2.setText("Last entry at "+child2.optString("lastEntry", ""));


                JSONObject child3 = children.getJSONObject(2);
                llchild3.setVisibility(View.VISIBLE);
                child3_relative.setVisibility(View.VISIBLE);
                child_name3.setText(child3.optString("name", ""));
                child_lastentry3.setText("Last entry at "+child3.optString("lastEntry", ""));
            }
            else if (i == 4) {
                JSONObject child1 = children.getJSONObject(0);
                llchild1.setVisibility(View.VISIBLE);
                child1_relative.setVisibility(View.VISIBLE);
                child_name1.setText(child1.optString("name", ""));
                child_lastentry1.setText("Last entry at "+child1.optString("lastEntry", ""));


                JSONObject child2 = children.getJSONObject(1);
                llchild2.setVisibility(View.VISIBLE);
                child2_relative.setVisibility(View.VISIBLE);
                child_name2.setText(child2.optString("name", ""));
                child_lastentry2.setText("Last entry at "+child2.optString("lastEntry", ""));


                JSONObject child3 = children.getJSONObject(2);
                llchild3.setVisibility(View.VISIBLE);
                child3_relative.setVisibility(View.VISIBLE);
                child_name3.setText(child3.optString("name", ""));
                child_lastentry3.setText("Last entry at "+child3.optString("lastEntry", ""));

                JSONObject child4 = children.getJSONObject(3);
                llchild4.setVisibility(View.VISIBLE);
                child4_relative.setVisibility(View.VISIBLE);
                child_name4.setText(child4.optString("name", ""));
                child_lastentry4.setText("Last entry at "+child4.optString("lastEntry", ""));
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        child1_relative.setOnClickListener(new View.OnClickListener() {
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
                    RelativeLayout child1_data = (RelativeLayout) findViewById(R.id.child1_data);

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

        child2_relative.setOnClickListener(new View.OnClickListener() {
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
                    RelativeLayout child2_data = (RelativeLayout) findViewById(R.id.child2_data);

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

        child3_relative.setOnClickListener(new View.OnClickListener() {
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
                    RelativeLayout child3_data = (RelativeLayout) findViewById(R.id.child3_data);

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

        child4_relative.setOnClickListener(new View.OnClickListener() {
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
                    RelativeLayout child4_data = (RelativeLayout) findViewById(R.id.child4_data);

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

        ImageView settings = (ImageView) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                intent.putExtra("date", currentDate);
                startActivity(intent);
                finish();
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
            }
            public void onSwipeRight() {
                swipeRight();
            }
            public void onSwipeLeft() {
                swipeLeft();
            }
            public void onSwipeBottom() {
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

    private RelativeLayout getChildDetail(final String time, final String food, final String diaryId, final String amount, final int child) {
        View view = LayoutInflater.from(this).inflate(R.layout.child_header,null);

        Typeface latoRegular = Fonts.returnFont(this, Fonts.LATO_REGULAR);

        RelativeLayout child1_detail = (RelativeLayout)view.findViewById(R.id.child_item);

        TextView view1 = (TextView)view.findViewById(R.id.txt1);
        TextView view2 = (TextView)view.findViewById(R.id.txt2);
        TextView view3 = (TextView)view.findViewById(R.id.txt3);
        TextView view4 = (TextView)view.findViewById(R.id.txt4);

        view1.setTypeface(latoRegular);
        view2.setTypeface(latoRegular);
        view3.setTypeface(latoRegular);
        view4.setTypeface(latoRegular);

        view4.setVisibility(View.GONE);

        Integer length = food.length();
        String food30chars;
        if (length > 20)
        {
            String more = "...";
            food30chars = food.substring(0, 20) + more;
        }
        else
        {
            food30chars = food;
        }
        view1.setText(time);
        view2.setText(food30chars);
        view3.setText(amount);
        view4.setText(diaryId);

        ImageView edit = (ImageView) view.findViewById(R.id.editImageView);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductToChildrenActivity.class);
                Bundle extras = new Bundle();
                extras.putString("date", time);
                extras.putString("productName", food);
                extras.putString("productId", "");
                extras.putString("amount", amount);
                extras.putString("diaryId", diaryId);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        ImageView delete = (ImageView) view.findViewById(R.id.deleteImageView);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete item");
                builder.setMessage("Are you sure you want to delete this item? The portion and time of consume will be deleted from the daily log of your child.")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle data = new Bundle();
                                data.putInt("child", child);
                                new AsyncApiCall(DELETE_PRODUCT, data, MainActivity.this, false).execute(WebApi.deleteProductFromChild(diaryId));
                            }

                        })
                        .setNegativeButton("CANCEL", null)
                        .show();
            }
        });

        return child1_detail;
    }

    @Override
    public void onTaskCompleted(int thread, String result) {
        JSONObject jsonResult = WebInterface.validateJSON(MainActivity.this, result);

        if (jsonResult != null) {
            if (thread == LOAD_CHILD_DATA1) {
                RelativeLayout child1_data = (RelativeLayout) findViewById(R.id.child1_data);
                child1_data.removeAllViews();
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout child1_detail;

                JSONArray childData = jsonResult.optJSONArray("childData");

                for(int i = 0; i < childData.length(); i++) {
                    try {
                        JSONObject oneFood = childData.getJSONObject(i);
                        String time = oneFood.optString("time", "");
                        String food = oneFood.optString("food", "");
                        String diaryId = oneFood.optString("diaryId", "");
                        String amount = oneFood.optString("amount", "");
                        child1_detail = getChildDetail(time, food, diaryId, amount, 1);
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
                RelativeLayout child2_data = (RelativeLayout) findViewById(R.id.child2_data);
                child2_data.removeAllViews();
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout child2_detail;

                JSONArray childData = jsonResult.optJSONArray("childData");

                for(int i = 0; i < childData.length(); i++) {
                    try {
                        JSONObject oneFood = childData.getJSONObject(i);
                        String time = oneFood.optString("time", "");
                        String food = oneFood.optString("food", "");
                        String diaryId = oneFood.optString("diaryId", "");
                        String amount = oneFood.optString("amount", "");
                        child2_detail = getChildDetail(time, food, diaryId, amount, 2);
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
                RelativeLayout child3_data = (RelativeLayout) findViewById(R.id.child3_data);
                child3_data.removeAllViews();
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout child3_detail;

                JSONArray childData = jsonResult.optJSONArray("childData");

                for(int i = 0; i < childData.length(); i++) {
                    try {
                        JSONObject oneFood = childData.getJSONObject(i);
                        String time = oneFood.optString("time", "");
                        String food = oneFood.optString("food", "");
                        String diaryId = oneFood.optString("diaryId", "");
                        String amount = oneFood.optString("amount", "");
                        child3_detail = getChildDetail(time, food, diaryId, amount, 3);
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
                RelativeLayout child4_data = (RelativeLayout) findViewById(R.id.child4_data);
                child4_data.removeAllViews();
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout child4_detail;

                JSONArray childData = jsonResult.optJSONArray("childData");

                for(int i = 0; i < childData.length(); i++) {
                    try {
                        JSONObject oneFood = childData.getJSONObject(i);
                        String time = oneFood.optString("time", "");
                        String food = oneFood.optString("food", "");
                        String diaryId = oneFood.optString("diaryId", "");
                        String amount = oneFood.optString("amount", "");
                        child4_detail = getChildDetail(time, food, diaryId, amount, 4);
                        child4_data.addView(child4_detail, p);
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                CHILD4_HAS_DATA = 1;
            }
        }
    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {
        // TODO Auto-generated method stub
        JSONObject jsonResult = WebInterface.validateJSON(MainActivity.this, result);

        if (jsonResult != null) {
            if (thread == DELETE_PRODUCT) {
                String success = jsonResult.optString("success", "0");
                if (success.equals("1")) {
                    int child = vars.getInt("child");
                    JSONArray children = LocalStorage.getChildren();
                    try {
                        if (child == 1) {
                            JSONObject child1 = children.getJSONObject(0);
                            new AsyncApiCall(LOAD_CHILD_DATA1, MainActivity.this, true, "Loading...").execute(WebApi.getChildInfo(LocalStorage.getDayForInfo(), child1.optInt("id", -1)));
                        } else if (child == 2) {
                            JSONObject child2 = children.getJSONObject(1);
                            new AsyncApiCall(LOAD_CHILD_DATA2, MainActivity.this, true, "Loading...").execute(WebApi.getChildInfo(LocalStorage.getDayForInfo(), child2.optInt("id", -1)));
                        } else if (child == 3) {
                            JSONObject child3 = children.getJSONObject(2);
                            new AsyncApiCall(LOAD_CHILD_DATA3, MainActivity.this, true, "Loading...").execute(WebApi.getChildInfo(LocalStorage.getDayForInfo(), child3.optInt("id", -1)));
                        } else if (child == 4) {
                            JSONObject child4 = children.getJSONObject(3);
                            new AsyncApiCall(LOAD_CHILD_DATA4, MainActivity.this, true, "Loading...").execute(WebApi.getChildInfo(LocalStorage.getDayForInfo(), child4.optInt("id", -1)));
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "The product has been deleted.", Toast.LENGTH_LONG).show();
                } else if (success.equals("0")) {
                    Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }
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