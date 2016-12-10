package com.kieslog.util;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kieslog.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RecentProductAdapter extends BaseAdapter {
    private final ArrayList mData;

    public RecentProductAdapter(HashMap<String, String> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public HashMap.Entry<String, String> getItem(int position) {
        return (HashMap.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_list_item, parent, false);
        } else {
            result = convertView;
        }

        HashMap.Entry<String, String> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(R.id.id)).setText(item.getKey());
        ((TextView) result.findViewById(R.id.product)).setText(item.getValue());

        Typeface latoRegular = Fonts.returnFont(parent.getContext(), Fonts.LATO_REGULAR);
        ((TextView) result.findViewById(R.id.product)).setTypeface(latoRegular);

        return result;
    }
}
