package com.jack.jackzkrt.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jack.frame.core.AbsFragment;
import com.jack.frame.util.SharePreUtil;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.JackApplication;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.databinding.FragmentStartBinding;
import com.jack.jackzkrt.dialog.RegisterDialog;
import com.jack.jackzkrt.interf.RegisterCallBackIntercace;
import com.jack.jackzkrt.launcher.LauncherView;
import com.squareup.otto.Subscribe;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.Bind;
import butterknife.OnClick;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.sdk.base.BaseProduct;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.thirdparty.eventbus.EventBus;

import static com.jack.frame.core.AbsApplication.mActivity;

/**
 * Created by jack on 2017/11/10.
 */

public class StartFragment extends AbsFragment<FragmentStartBinding> {
    @Bind(R.id.launcherview)
    LauncherView launcherview;
    @Bind(R.id.ftb_start)
    Button ftb_start;
    @Bind(R.id.tv_show_register_info)
    TextView tv_show_register_info;

    private int startBtnNum;
    private BaseProduct mProduct;
    private AtomicBoolean isRegistrationInProgress = new AtomicBoolean(false);

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        JackApplication.getEventBus().register(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                launcherview.setStartEndListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (!SharePreUtil.getString("isRegister", getActivity(), "isRegister").equals("true")) {
                            RegisterDialog registerDialog = new RegisterDialog(getActivity(), new RegisterCallBackIntercace() {
                                @Override
                                public void getRegisterResult(boolean bool) {
                                    if (bool) {
                                        showRegisterInfo();
                                    }
                                }
                            });
                            registerDialog.show();
                        } else {
                            showRegisterInfo();
                        }
                    }
                });

                launcherview.start();
            }
        }, 500);
    }

    private void showRegisterInfo() {
        //显示注册信息
        tv_show_register_info.setVisibility(View.VISIBLE);
        tv_show_register_info.setText(SharePreUtil.getString("detachment", mActivity, "detachment") + " " +
                SharePreUtil.getString("overall", mActivity, "overall") + " " +
                SharePreUtil.getString("bigArr", mActivity, "bigArr") + " " +
                SharePreUtil.getString("centreArr", mActivity, "centreArr"));
    }

    @OnClick(R.id.ftb_start)
    public void onStartBtnClick(View v) {
//        if(startBtnNum==1){
//        }else if(startBtnNum == 2){
        //CompassCalibratingWorkFlow.startCalibration((ActionCallback)null);
        // }
        //TODO 判断stack里面为1，如果为1，则不需要进入以下代码
//        if (GeneralUtils.isFastDoubleClick())
//            return;


        if (!SharePreUtil.getString("isRegister", getActivity(), "isRegister").equals("true")) {
            RegisterDialog registerDialog = new RegisterDialog(getActivity(), new RegisterCallBackIntercace() {
                @Override
                public void getRegisterResult(boolean bool) {
                    ftb_start.setEnabled(true);
                    if (bool) {
                        //显示注册信息
                        showRegisterInfo();
                    }
                }
            });
            registerDialog.show();
        } else {
            ftb_start.setEnabled(false);
            EventBus.getDefault().post(1);
        }

    }

    private AnimatorListenerAdapter mAnimatorListenerAdapter;

    @Override
    protected void onRequestPermissions(int requestCode, String[] permissions, int[] grantResults) {
        startSDKRegistration();
    }

    private void startSDKRegistration() {
        if (isRegistrationInProgress.compareAndSet(false, true)) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    DJISDKManager.getInstance().registerApp(mActivity, registrationCallback);
                }
            });
        }
    }

    private DJISDKManager.SDKManagerCallback registrationCallback = new DJISDKManager.SDKManagerCallback() {
        @Override
        public void onRegister(DJIError error) {
            isRegistrationInProgress.set(false);
            if (error == DJISDKError.REGISTRATION_SUCCESS) {
                DJISDKManager.getInstance().startConnectionToProduct();
                T.show(mActivity, "SDK registration succeeded!", 1);
            } else {
                T.show(mActivity, "SDK registration failed, check network and retry!", 1);
            }
        }

        @Override
        public void onProductChange(BaseProduct djiBaseProduct, BaseProduct djiBaseProduct1) {

        }
    };

    @Subscribe
    public void onConnectivityChange(JackApplication.ConnectivityChangeEvent event) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshSDKRelativeUI();
            }
        });
    }

    private void refreshSDKRelativeUI() {
        mProduct = JackApplication.getProductInstance();
        if (mProduct != null) {
            ftb_start.setVisibility(View.VISIBLE);
            if (mProduct.isConnected()) {
                getBinding().ftbStart.setText("进入");
                startBtnNum = 1;
            } else {
                startBtnNum = 2;
                getBinding().ftbStart.setText("对频");
            }
            mProduct = null;
        } else {
            startBtnNum = 0;
            ftb_start.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JackApplication.getEventBus().unregister(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_start;
    }

    @Override
    protected void onDelayLoad() {

    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
