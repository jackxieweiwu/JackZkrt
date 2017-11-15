package com.jack.jackzkrt;

import android.os.Bundle;
import com.jack.frame.core.AbsActivity;
import com.jack.frame.core.AbsApplication;
import com.jack.jackzkrt.fragment.MainFragment;
import com.jack.jackzkrt.fragment.StartFragment;

import dji.thirdparty.eventbus.EventBus;


public class MainActivity extends AbsActivity {

    private StartFragment startFragment;
    private MainFragment mainFragment;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.init(savedInstanceState);

        if (savedInstanceState != null) {
            startFragment = (StartFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "StartFragment");
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "mainFragment");
        } else {
            startFragment = StartFragment.newInstance();
            mainFragment = MainFragment.newInstance();
        }

        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout_start, startFragment)
                .commit();
    }

    public void onEventMainThread(Integer type){
        if(type == 1){//open main fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_start, mainFragment)
                    .commit();
        }else{  //open Compass
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void dataCallback(int result, Object data) {

    }
}
