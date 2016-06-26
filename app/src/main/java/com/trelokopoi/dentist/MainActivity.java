package com.trelokopoi.dentist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.trelokopoi.dentist.util.L;
import com.trelokopoi.dentist.util.LocalStorage;
import com.trelokopoi.dentist.util.OnSwipeTouchListener;
import com.trelokopoi.dentist.util.Tools;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    String currentDate;
    TextView dateTxtView;
    String [] children = {"Panikas", "Kostakis", "Thomakos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context ctx = this;

        RelativeLayout activity_main = (RelativeLayout) findViewById(R.id.activity_main);

        dateTxtView = (TextView) findViewById(R.id.date);
        currentDate = LocalStorage.getDayForInfo();
        dateTxtView.setText(currentDate);

        TextView child_name1 = (TextView) findViewById(R.id.child_name1);
        TextView child_name2 = (TextView) findViewById(R.id.child_name2);
        TextView child_name3 = (TextView) findViewById(R.id.child_name3);
        TextView child_name4 = (TextView) findViewById(R.id.child_name4);

        Integer i = children.length;
        switch (i) {
            case 1:
                child_name1.setVisibility(View.VISIBLE);
                child_name1.setText(children[0]);
                break;
            case 2:
                child_name1.setVisibility(View.VISIBLE);
                child_name1.setText(children[0]);

                child_name2.setVisibility(View.VISIBLE);
                child_name2.setText(children[1]);
                break;
            case 3:
                child_name1.setVisibility(View.VISIBLE);
                child_name1.setText(children[0]);

                child_name2.setVisibility(View.VISIBLE);
                child_name2.setText(children[1]);

                child_name3.setVisibility(View.VISIBLE);
                child_name3.setText(children[2]);
                break;
            case 4:
                child_name1.setVisibility(View.VISIBLE);
                child_name1.setText(children[0]);

                child_name2.setVisibility(View.VISIBLE);
                child_name2.setText(children[1]);

                child_name3.setVisibility(View.VISIBLE);
                child_name3.setText(children[2]);

                child_name4.setVisibility(View.VISIBLE);
                child_name4.setText(children[3]);
                break;
        }

        child_name1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout myRoot = (LinearLayout) findViewById(R.id.child1);
                LinearLayout a = new LinearLayout(ctx);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                a.setOrientation(LinearLayout.VERTICAL);
                TextView view1 = new TextView(ctx);
                TextView view2 = new TextView(ctx);
                TextView view3 = new TextView(ctx);
                view1.setLayoutParams(p);
                view2.setLayoutParams(p);
                view3.setLayoutParams(p);
                view1.setBackgroundColor(0xff66ff66);
                view2.setBackgroundColor(0xff66ff66);
                view3.setBackgroundColor(0xff66ff66);
                view1.setText("Coca Cola");
                view2.setText("150ml");
                view3.setText("10:52");
                a.addView(view1);
                a.addView(view2);
                a.addView(view3);
                myRoot.addView(a, p);
            }
        });

        ImageView previous = (ImageView) findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRight();
            }
        });

        ImageView next = (ImageView) findViewById(R.id.next);
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
                startActivity(intent);
            }
        });

        activity_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        activity_main.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                swipeRight();
            }
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                swipeLeft();
            }
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
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
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private void swipeRight() {
        Tools.startNewActivityLeft(MainActivity.this, MainActivity.class);
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
    }

    private void swipeLeft() {
        Tools.startNewActivityLeft(MainActivity.this, MainActivity.class);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = (Date) df.parse(currentDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            String dateToSave = df.format(cal.getTime());
            LocalStorage.setDayForInfo(dateToSave);
        } catch (ParseException e) {
            L.debug(e.toString());
        }
    }
}

