package com.trelokopoi.dentist.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.trelokopoi.dentist.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsAdapter extends ArrayAdapter<String> {
    private String[] items;
    private Context mContext;
    private int id;

    public SettingsAdapter(Context context, int resource, int textViewResourceId, String[] list) {
        super(context, resource, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.settings_item);

        Typeface latoBold = Fonts.returnFont(mContext, Fonts.LATO_BOLD);
        text.setTypeface(latoBold);
        text.setText(items[position]);

        return mView;
    }
}
