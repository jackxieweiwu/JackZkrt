package com.jack.jackzkrt.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.jack.frame.core.AbsFragment;
import com.jack.frame.util.SharePreUtil;
import com.jack.jackzkrt.JackApplication;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.databinding.FragmentMapBinding;

import butterknife.Bind;
import butterknife.OnClick;
import zkrtdrone.zkrt.com.maplib.info.GestureMapFragment;
import zkrtdrone.zkrt.com.maplib.info.mission.MissionProxy;

/**
 * Created by jll on 17-11-20.
 * 原项目 控制地图基本操作 阉割版
 */

public class MapMountFragment extends AbsFragment<FragmentMapBinding> implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.map_type_radiogroup) RadioGroup map_type_radiogroup;
    @Bind(R.id.location_map_radiogroup) RadioGroup location_map_radiogroup;
    @Bind(R.id.relayout_map) RelativeLayout relayout_map;

    private GestureMapFragment gestureMapFragment;
    public static Handler handler;
    private MissionProxy missionProxy;

    @Override
    protected void init(Bundle savedInstanceState) {
        handler = new Handler(Looper.getMainLooper());
        location_map_radiogroup.setOnCheckedChangeListener(this);
    }


    @Override
    protected void onDelayLoad() {
    }

    @OnClick(R.id.img_mapzomm_min)
    public void mapZoomMin(View v) {
        /*if (GeneralUtils.isFastDoubleClick())
            return;*/

        String maptype= SharePreUtil.getString("maptype",mActivity,"maptyppe");
        if(maptype.equals("baidumap")){
            gestureMapFragment.getMapFragment().setBaidumapZoom(MapStatusUpdateFactory.zoomOut());
        }else if(maptype.equals("gaodemap")){
//            gestureMapFragment.getMapFragment().setGaodemapZoom(CameraUpdateFactory.zoomOut());
        }else{
            gestureMapFragment.getMapFragment().setBaidumapZoom(MapStatusUpdateFactory.zoomOut());
        }
    }

    @OnClick(R.id.img_maptype)
    public void maptype(View v) {
        /*if (GeneralUtils.isFastDoubleClick())
            return;*/
        if (map_type_radiogroup.getVisibility() == View.VISIBLE) {
            map_type_radiogroup.startAnimation(startAnimViewGone());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    map_type_radiogroup.setVisibility(View.GONE);
                }
            }, 200);
        } else {
            map_type_radiogroup.startAnimation(startAnimViewShow());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    map_type_radiogroup.setVisibility(View.VISIBLE);
                }
            }, 200);
        }
    }
    @OnClick(R.id.img_location)
    public void mapLocation(View v) {
        /*if (GeneralUtils.isFastDoubleClick())
            return;*/
        if (location_map_radiogroup.getVisibility() == View.VISIBLE) {
            location_map_radiogroup.startAnimation(startAnimViewGone());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    location_map_radiogroup.setVisibility(View.GONE);
                }
            }, 200);
        } else {
            location_map_radiogroup.startAnimation(startAnimViewShow());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    location_map_radiogroup.setVisibility(View.VISIBLE);
                }
            }, 200);
        }
    }
    @OnClick(R.id.img_mapzomm_max)
    public void mapZoomMax(View v) {
        /*if (GeneralUtils.isFastDoubleClick())
            return;*/
        String maptype= SharePreUtil.getString("maptype",mActivity,"maptyppe");
        if(maptype.equals("baidumap")){
            gestureMapFragment.getMapFragment().setBaidumapZoom(MapStatusUpdateFactory.zoomIn());
        }else if(maptype.equals("gaodemap")){
//            gestureMapFragment.getMapFragment().setGaodemapZoom(CameraUpdateFactory.zoomIn());
        }else{
            gestureMapFragment.getMapFragment().setBaidumapZoom(MapStatusUpdateFactory.zoomIn());
        }

    }

    @OnClick(R.id.img_mappoy_clear)
    public void mapMarkerClear(View v) {
        /*if (GeneralUtils.isFastDoubleClick())
            return;*/
        missionProxy = JackApplication.getMissionProxy();
        try {
            missionProxy.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
        gestureMapFragment.getMapFragment().clearMapMarker();
    }

    //显示动画
    private TranslateAnimation startAnimViewShow() {
        int[] location = new int[2];
        map_type_radiogroup.getLocationOnScreen(location);
        //TranslateAnimation mHiddenAction = new TranslateAnimation(location[0], 0.0f,location[1], 0.0f);
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    //隐藏动画
    private TranslateAnimation startAnimViewGone() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        String maptype= SharePreUtil.getString("maptype",mActivity,"maptyppe");
        switch (checkedId) {
            case R.id.radio_btn_common:  //普通
                if(maptype.equals("baidumap")){
                    gestureMapFragment.getMapFragment().goTomapType(BaiduMap.MAP_TYPE_NORMAL);
                }else if(maptype.equals("gaodemap")){
//                    gestureMapFragment.getMapFragment().goTomapType(AMap.MAP_TYPE_NORMAL);
                }else{
                    gestureMapFragment.getMapFragment().goTomapType(BaiduMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.radio_btn_satellite:  //卫星
                if(maptype.equals("baidumap")){
                    gestureMapFragment.getMapFragment().goTomapType(BaiduMap.MAP_TYPE_SATELLITE);
                }else if(maptype.equals("gaodemap")){
//                    gestureMapFragment.getMapFragment().goTomapType(AMap.MAP_TYPE_SATELLITE);
                }else{
                    gestureMapFragment.getMapFragment().goTomapType(BaiduMap.MAP_TYPE_SATELLITE);
                }
                break;
            case R.id.radio_btn_mylocation:  //自我
                gestureMapFragment.getMapFragment().goToMyLocation();
                break;
            case R.id.radio_btn_dronelocation:  //飞机
                gestureMapFragment.getMapFragment().goToDroneLocation();
                break;
        }
    }

    public void GestureMapFragment(GestureMapFragment gestureMapFragments) {
        this.gestureMapFragment = gestureMapFragments;
    }
    public void setCheckGoneViesbile(boolean bool) {
        if (bool) {
            relayout_map.setVisibility(View.VISIBLE);
        } else {
            relayout_map.setVisibility(View.GONE);
        }
    }
}
