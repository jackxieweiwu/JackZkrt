package com.jack.frame.util;

import android.app.Activity;
import android.widget.ArrayAdapter;

/**
 * Created by jack-xie on 2017/8/15.
 *
 * @des ${TODO}
 */

public class AdapterHelper {
    public static ArrayAdapter getAdapter(Activity activity, String[] name){
        ArrayAdapter adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public static ArrayAdapter getAdapterInteger(Activity activity, Integer[] name){
        ArrayAdapter adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
