package com.jack.jackzkrt.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jack.frame.core.AbsFragment;
import com.jack.frame.util.AndroidUtils;
import com.jack.frame.util.SharePreUtil;
import com.jack.frame.util.rxutil.GeneralUtils;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.JackApplication;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.databinding.FragmentStartBinding;
import com.jack.jackzkrt.dialog.DialogPairimg;
import com.jack.jackzkrt.dialog.RegisterDialog;
import com.jack.jackzkrt.interf.RegisterCallBackIntercace;
import com.jack.jackzkrt.launcher.LauncherView;
import com.squareup.otto.Subscribe;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.Bind;
import butterknife.OnClick;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.keysdk.callback.ActionCallback;
import dji.sdk.base.BaseProduct;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.thirdparty.eventbus.EventBus;
import zkrt.ui.widget.compass.CompassCalibratingWorkFlow;
import zkrtdrone.zkrt.com.maplib.info.until.Utils;

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
    @Bind(R.id.tv_register_code)
    TextView tv_register_code;
    @Bind(R.id.tv_version_code)
    TextView tv_version_code;
    @Bind(R.id.linear_register_info)
    LinearLayout linear_register_info;

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
//                        EventBus.getDefault().post(1);
                    }
                });

                launcherview.start();
            }
        }, 500);

    }

    private void showRegisterInfo() {
        //显示注册信息
        linear_register_info.setVisibility(View.VISIBLE);
        String detachment = SharePreUtil.getString("detachment", mActivity, "detachment");//支队
        String overall = SharePreUtil.getString("overall", mActivity, "overall");//总队
        String big = SharePreUtil.getString("bigArr", mActivity, "bigArr");//大队
        String centre = SharePreUtil.getString("centreArr", mActivity, "centreArr");//中队

        String registerInfo = detachment + "/" + overall + "/" + detachment + "/" + big + "/" + centre;
        tv_show_register_info.setText(registerInfo);
        tv_register_code.setText("ID码: " + Settings.System.getString(getContext().getContentResolver(), Settings.System.ANDROID_ID));
        tv_version_code.setText("版本号:" + AndroidUtils.getAPPVersionCode(getActivity()) + "");
    }

    @OnClick(R.id.ftb_start)
    public void onStartBtnClick(View v) {
        if (GeneralUtils.isFastDoubleClick())
            return;

        if (startBtnNum == 1) {

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

        } else if (startBtnNum == 2) {

            if (GeneralUtils.isFastDoubleClick())
                return;
            //进入配对模式
            DialogPairimg dialog = new DialogPairimg();
            dialog.show(getChildFragmentManager(), "ip_dialog");

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
            ftb_start.setVisibility(View.GONE);
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
