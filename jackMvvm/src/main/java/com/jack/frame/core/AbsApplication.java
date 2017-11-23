package com.jack.frame.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;

/**
 * Created by root on 17-11-9.
 */

public class AbsApplication extends Application {
    public static final String TAG = AbsApplication.class.getName();
    private static AbsApplication instance;
    public static Activity mActivity;
    public static Handler handler;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AbsFrame mManager = AbsFrame.init(this);
        handler = new Handler(Looper.getMainLooper());
        initView();
        //mManager.openCrashHandler();
        //mManager.openCrashHandler("http://192.168.1.132/server","params");
    }

    protected void initView() {}

    public static AbsApplication getInstance() {
        return instance;
    }
}
