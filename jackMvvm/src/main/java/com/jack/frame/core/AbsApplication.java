package com.jack.frame.core;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.jack.frame.R;
import com.jack.frame.util.show.T;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.products.Aircraft;
import dji.sdk.products.HandHeld;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Created by root on 17-11-9.
 */

public class AbsApplication extends Application {
    public static final String TAG = AbsApplication.class.getName();

    private static BaseProduct product;
    private static Bus bus = new Bus(ThreadEnforcer.ANY);
    private static AbsApplication instance;

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
        //mManager.openCrashHandler();
        //mManager.openCrashHandler("http://192.168.1.132/server","params");
    }

    public static synchronized BaseProduct getProductInstance() {
        if (null == product) {
            product = DJISDKManager.getInstance().getProduct();
        }
        return product;
    }

    public static boolean isAircraftConnected() {
        return getProductInstance() != null && getProductInstance() instanceof Aircraft;
    }

    public static boolean isHandHeldConnected() {
        return getProductInstance() != null && getProductInstance() instanceof HandHeld;
    }

    public static synchronized Aircraft getAircraftInstance() {
        if (!isAircraftConnected()) return null;
        return (Aircraft) getProductInstance();
    }

    public static AbsApplication getInstance() {
        return instance;
    }

    public static Bus getEventBus() {
        return bus;
    }

    public static DJISDKManager.SDKManagerCallback mDJISDKManagerCallback = new DJISDKManager.SDKManagerCallback() {

        @Override
        public void onRegister(DJIError error) {
            if (error == DJISDKError.REGISTRATION_SUCCESS) {
                DJISDKManager.getInstance().startConnectionToProduct();
            } else {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        T.show(getInstance(), R.string.sdk_registration_message,1);
                    }
                });
            }
            Log.v(TAG, error.getDescription());
        }

        @Override
        public void onProductChange(BaseProduct oldProduct, BaseProduct newProduct) {

            Log.d(TAG, String.format("onProductChanged oldProduct:%s, newProduct:%s", oldProduct, newProduct));
            product = newProduct;
            if (product != null) {
                product.setBaseProductListener(mDJIBaseProductListener);
            }
            notifyStatusChange();
        }

        private BaseProduct.BaseProductListener mDJIBaseProductListener = new BaseProduct.BaseProductListener() {

            @Override
            public void onComponentChange(BaseProduct.ComponentKey key,
                                          BaseComponent oldComponent,
                                          BaseComponent newComponent) {

                if (newComponent != null) {
                    newComponent.setComponentListener(mDJIComponentListener);
                }
                Log.d(TAG,
                        String.format("onComponentChange key:%s, oldComponent:%s, newComponent:%s",
                                key,
                                oldComponent,
                                newComponent));

                notifyStatusChange();
            }

            @Override
            public void onConnectivityChange(boolean isConnected) {

                Log.d(TAG, "onProductConnectivityChanged: " + isConnected);

                notifyStatusChange();
            }
        };

        private BaseComponent.ComponentListener mDJIComponentListener = new BaseComponent.ComponentListener() {

            @Override
            public void onConnectivityChange(boolean isConnected) {
                Log.d(TAG, "onComponentConnectivityChanged: " + isConnected);
                notifyStatusChange();
            }
        };

        private void notifyStatusChange() {
            bus.post(new ConnectivityChangeEvent());
        }
    };

    public static class ConnectivityChangeEvent {
    }
}
