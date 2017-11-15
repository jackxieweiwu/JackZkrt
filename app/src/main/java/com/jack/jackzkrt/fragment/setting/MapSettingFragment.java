package com.jack.jackzkrt.fragment.setting;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.jack.frame.core.AbsFragment;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.adapter.LocalMapAdapter;
import com.jack.jackzkrt.adapter.MapCityAdapter;
import com.jack.jackzkrt.databinding.SettingMapFragmentBinding;

import java.util.ArrayList;
import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by root on 17-5-5.
 */

public class MapSettingFragment extends AbsFragment<SettingMapFragmentBinding> implements MKOfflineMapListener{
    private MKOfflineMap mOffline = null;
    private static final String TAG = "OfflineDemo";

    @Bind(R.id.state) TextView stateView;
    @Bind(R.id.allcitylist) ListView allCityList;
    @Bind(R.id.localmaplist) ListView localMapListView;
    @Bind(R.id.citylist_layout) LinearLayout cl;
    @Bind(R.id.localmap_layout) LinearLayout lm;
    private int city_id;
    /**
     * 已下载的离线地图信息列表
     */
    private ArrayList<MKOLUpdateElement> localMapList = null;
    private LocalMapAdapter lAdapter = null;

    public static MapSettingFragment newInstance(){
        return new MapSettingFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        // 获取热闹城市列表
        ArrayList<MKOLSearchRecord> records1 = mOffline.getHotCityList();
        // 获取所有支持离线地图的城市
        ArrayList<String> allCities = new ArrayList<>();
        final ArrayList<String> allCityNames = new ArrayList<>();
        ArrayList<MKOLSearchRecord> records2 = mOffline.getOfflineCityList();
        if (records1 != null) {
            for (MKOLSearchRecord r : records2) {
                allCities.add(r.cityName + "(" + r.cityID + ")" + "   --" + this.formatDataSize(r.size));
                allCityNames.add(r.cityName);
            }
        }

        MapCityAdapter mapCityAdapter2 = new MapCityAdapter(mActivity,allCities);
        allCityList.setAdapter(mapCityAdapter2);
        allCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getBinding().setStrMessageMap(allCityNames.get(i));
                ArrayList<MKOLSearchRecord> records = mOffline.searchCity(allCityNames.get(i));
                if (records == null || records.size() != 1) {
                    return;
                }
                city_id = records.get(0).cityID;
                getBinding().setStrCityName(String.valueOf(records.get(0).cityID)+":"+allCityNames.get(i));
            }
        });
        lm.setVisibility(View.GONE);
        cl.setVisibility(View.VISIBLE);

        // 获取已下过的离线地图信息
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        //lAdapter = new LocalMapAdapter(mActivity,localMapList);
        lAdapter = new LocalMapAdapter();
        //lAdapter.setOffLineMap(mOffline);
        localMapListView.setAdapter(lAdapter);

    }

    public String formatDataSize(int size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

    @Override
    protected int setLayoutId() {
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        return R.layout.setting_map_fragment;
    }

    /**
     * 切换至城市列表
     *
     * @param view
     */
    @OnClick(R.id.clButton)
    public void clickCityListButton(View view) {
        lm.setVisibility(View.GONE);
        cl.setVisibility(View.VISIBLE);
    }

    /**
     * 切换至下载管理列表
     *
     * @param view
     */
    @OnClick(R.id.localButton)
    public void clickLocalMapListButton(View view) {
        lm.setVisibility(View.VISIBLE);
        cl.setVisibility(View.GONE);
    }


    /**
     * 开始下载
     *
     * @param view
     */
    @OnClick(R.id.start)
    public void start(View view) {
        mOffline.start(city_id);
        clickLocalMapListButton(null);
        T.show(mActivity, "开始下载离线地图. cityid: " + city_id,1);
        updateView();
    }

    /**
     * 暂停下载
     *
     * @param view
     */
    @OnClick(R.id.stop)
    public void stop(View view) {
        mOffline.pause(city_id);
        T.show(mActivity, "暂停下载离线地图. cityid: " + city_id,1);
        updateView();
    }

    /**
     * 删除离线地图
     *
     * @param view
     */
    @OnClick(R.id.del_map)
    public void remove(View view) {
        mOffline.remove(city_id);
        T.show(mActivity, "删除离线地图. cityid: " + city_id,1);
        updateView();
    }

    /**
     * 更新状态显示
     */
    public void updateView() {
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        lAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                final MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
                    stateView.setText(String.format("%s : %d%%", update.cityName,update.ratio));
                    updateView();
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
            default:
                break;
        }
    }


    @Override
    public void onPause() {
        MKOLUpdateElement temp = mOffline.getUpdateInfo(city_id);
        if (temp != null && temp.status == MKOLUpdateElement.DOWNLOADING) {
            mOffline.pause(city_id);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mOffline.destroy();
        super.onDestroy();
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

    @Override
    protected void onDelayLoad() {

    }

    /**
     * 离线地图管理列表适配器
     */
    public class LocalMapAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return localMapList.size();
        }

        @Override
        public Object getItem(int index) {
            return localMapList.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View view, ViewGroup arg2) {
            MKOLUpdateElement e = (MKOLUpdateElement) getItem(index);
            view = View.inflate(mActivity,
                    R.layout.offline_localmap_list, null);
            initViewItem(view, e);
            return view;
        }

        void initViewItem(View view, final MKOLUpdateElement e) {
            Button remove = (Button) view.findViewById(R.id.remove);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView update = (TextView) view.findViewById(R.id.update);
            TextView ratio = (TextView) view.findViewById(R.id.ratio);
            ratio.setText(e.ratio + "%");
            title.setText(e.cityName);
            if (e.update) {
                update.setText("可更新");
            } else {
                update.setText("最新");
            }
            if (e.ratio != 100) {
                //display.setEnabled(false);
            } else {
                //display.setEnabled(true);
            }
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mOffline.remove(e.cityID);
                    updateView();
                }
            });
        }

    }
}
