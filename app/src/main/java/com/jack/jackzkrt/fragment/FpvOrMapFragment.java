package com.jack.jackzkrt.fragment;

import android.os.Bundle;

import com.jack.frame.core.AbsFragment;
import com.jack.jackzkrt.R;

import zkrtdrone.zkrt.com.maplib.info.OnEditorInteraction;
import zkrtdrone.zkrt.com.maplib.info.mission.MissionItemProxy;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;

import static zkrtdrone.zkrt.com.maplib.info.mapApplication.setEditorListener;

/**
 * Created by jack on 2017/11/13.
 */

public class FpvOrMapFragment extends AbsFragment implements OnEditorInteraction{


    public static FpvOrMapFragment newInstance(){
        return new FpvOrMapFragment();
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        setEditorListener(this);
    }

    @Override
    protected void onDelayLoad() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fpv_or_map;
    }

    @Override
    public boolean onItemLongClick(MissionItemProxy item) {
        return false;
    }

    @Override
    public void onItemClick(MissionItemProxy item, boolean zoomToFit) {

    }

    @Override
    public void onMapClick(LatLong coord) {

    }

    @Override
    public void onListVisibilityChanged() {

    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
