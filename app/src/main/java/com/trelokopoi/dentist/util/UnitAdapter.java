package com.trelokopoi.dentist.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trelokopoi.dentist.R;

public class UnitAdapter extends ArrayAdapter<String> {
    private String[] items;
    private Context mContext;
    private int id;

    public UnitAdapter(Context context, int resource, int textViewResourceId, String[] list) {
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

        TextView text = (TextView) mView.findViewById(R.id.unit_item);

        Typeface latoRegular = Fonts.returnFont(mContext, Fonts.LATO_REGULAR);
        text.setTypeface(latoRegular);
        text.setText(items[position]);

        return mView;
    }
}
