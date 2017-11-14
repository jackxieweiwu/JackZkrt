package com.jack.jackzkrt.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.arialyy.absadapter.common.AbsHolder;
import com.arialyy.absadapter.listview.AbsLvAdapter;
import com.jack.jackzkrt.R;

import java.util.List;

import butterknife.Bind;

/**
 * Created by jack on 17-11-14.
 */

public class SettingIconAdapter extends AbsLvAdapter<Integer,SettingIconAdapter.SettingIconHolder> {
    public SettingIconAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.list_item_image;
    }

    @Override
    public void bindData(int position, SettingIconHolder holder, Integer item) {
        holder.image_setting_icon.setBackgroundResource(item);
    }

    @Override
    public SettingIconHolder getViewHolder(View convertView) {
        return new SettingIconHolder(convertView);
    }

    /**
     * 创建相应的Holder
     */
    public class SettingIconHolder extends AbsHolder {
        @Bind(R.id.image_setting_icon) ImageView image_setting_icon;

        public SettingIconHolder(View itemView) {
            super(itemView);
        }
    }
}
