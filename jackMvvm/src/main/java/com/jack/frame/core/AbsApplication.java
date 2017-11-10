package com.jack.frame.core;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by root on 17-11-9.
 */

public class AbsApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AbsFrame mManager = AbsFrame.init(this);
        //mManager.openCrashHandler();
        //mManager.openCrashHandler("http://192.168.1.132/server","params");
    }
}
