package com.example.jackspinnerlibrary;

import android.content.Context;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack_xie on 2017/8/16.
 *
 * @des ${TODO}
 */

final class JackSpinnerAdapterWrapper extends JackSpinnerBaseAdapter {
    private final ListAdapter listAdapter;

    public JackSpinnerAdapterWrapper(Context context, ListAdapter toWrap) {
        super(context);
        listAdapter = toWrap;
    }

    @Override public int getCount() {
        return listAdapter.getCount() - 1;
    }

    @Override public Object getItem(int position) {
        if (position >= getSelectedIndex()) {
            return listAdapter.getItem(position + 1);
        } else {
            return listAdapter.getItem(position);
        }
    }

    @Override public Object get(int position) {
        return listAdapter.getItem(position);
    }

    @Override public List<Object> getItems() {
        List<Object> items = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            items.add(getItem(i));
        }
        return items;
    }
}
