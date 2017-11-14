package com.jack.jackzkrt.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arialyy.absadapter.common.AbsHolder;
import com.arialyy.absadapter.listview.AbsLvAdapter;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.jack.jackzkrt.R;
import java.util.List;

import butterknife.Bind;

/**
 * Created by jack on 17-11-14.
 */

public class LocalMapAdapter extends AbsLvAdapter<MKOLUpdateElement,LocalMapAdapter.LocalMapHolder> {
    private MKOfflineMap mOffline = null;

    public LocalMapAdapter(Context context, List data) {
        super(context, data);
    }

    public void setOffLineMap(MKOfflineMap mkOfflineMap){
        this.mOffline =mkOfflineMap;
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.offline_localmap_list;
    }

    @Override
    public void bindData(int position, final LocalMapHolder holder, final MKOLUpdateElement e) {
        holder.ratio.setText(e.ratio + "%");
        holder.title.setText(e.cityName);
        if (e.update) {
            holder.update.setText("可更新");
        } else {
            holder.update.setText("最新");
        }
        if (e.ratio != 100) {
            //display.setEnabled(false);
        } else {
            //display.setEnabled(true);
        }
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mOffline.remove(e.cityID);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public LocalMapHolder getViewHolder(View convertView) {
        return new LocalMapHolder(convertView);
    }

    /**
     * 创建相应的Holder
     */
    public class LocalMapHolder extends AbsHolder {
        @Bind(R.id.remove)  Button remove;
        @Bind(R.id.title)  TextView title;
        @Bind(R.id.update) TextView update;
        @Bind(R.id.ratio)  TextView ratio;

        public LocalMapHolder(View itemView) {
            super(itemView);
        }
    }
}
