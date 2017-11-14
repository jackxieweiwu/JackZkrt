package com.jack.jackzkrt.fragment.setting;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.jackspinnerlibrary.JackSpinner;
import com.jack.frame.core.AbsFragment;
import com.jack.jackzkrt.JackApplication;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.databinding.SettingRemoteFragmentBinding;

import butterknife.Bind;
import butterknife.OnClick;
import dji.common.error.DJIError;
import dji.common.remotecontroller.AircraftMappingStyle;
import dji.common.remotecontroller.ChargeMobileMode;
import dji.common.remotecontroller.ChargeRemaining;
import dji.common.remotecontroller.CustomButtonTags;
import dji.common.remotecontroller.GimbalAxis;
import dji.common.util.CommonCallbacks;
import zkrt.ui.ModuleVerificationUtil;

/**
 * Created by jack_xie on 17-5-9.
 * 遥控器
 */

public class RemoteSettingFragment extends AbsFragment<SettingRemoteFragmentBinding> {
    @Bind(R.id.edit_remote_name) EditText edit_remote_name;
    @Bind(R.id.edit_remote_pwd) EditText edit_remote_pwd;
    @Bind(R.id.spinner_ios_phone)JackSpinner spinner_ios_phone;
    @Bind(R.id.spinner_ios_pyr) JackSpinner spinner_ios_pyr;
    @Bind(R.id.spinner_rocker_mode) JackSpinner spinner_rocker_mode;  //遥控器模式
    @Bind(R.id.img_rocker_mode) ImageView img_rocker_mode;  //遥控器模式
    @Bind(R.id.spinner_custom_c1) JackSpinner spinner_custom_c1;
    @Bind(R.id.spinner_custom_c2) JackSpinner spinner_custom_c2;

    public static RemoteSettingFragment newInstance(){
        return new RemoteSettingFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        spinner_ios_phone.setItems("不充电", "一直充", "智能充");
        spinner_ios_pyr.setItems("俯仰", "横滚", "偏航");
//        spinner_rocker_mode.setItems("风格1", "风格2", "风格3");
        spinner_rocker_mode.setItems("日本手", "美国手", "中国手");
        //String[] strings = {"相机高级设置","相机朝向归中","切换云台模式","地图/FPV切换","清除航线","电池页面","云台方向 上下/左右","中央重点测光"};
        //List<String> arrays = Arrays.asList(strings);
        spinner_custom_c1.setItems("相机高级设置","相机朝向归中","切换云台模式","地图/FPV切换","清除航线","电池页面","云台方向 上下/左右","中央重点测光");
        spinner_custom_c2.setItems("相机高级设置","相机朝向归中","切换云台模式","地图/FPV切换","清除航线","电池页面","云台方向 上下/左右","中央重点测光");
        spinner_custom_c1.isPressed();
        spinner_custom_c2.isPressed();
        spinner_rocker_mode.isPressed();
        spinner_ios_phone.isPressed();
        spinner_ios_pyr.isPressed();

        if (ModuleVerificationUtil.isRemoteControllerAvailable()) {
            //设置摇杆风格
            spinner_rocker_mode.setOnItemSelectedListener(new JackSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(JackSpinner view, final int position, long id, Object item) {
                    JackApplication.getAircraftInstance().getRemoteController().setAircraftMappingStyle(AircraftMappingStyle.find(position + 1), new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if (djiError == null){
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (position == 2)
                                            img_rocker_mode.setImageResource(R.mipmap.setting_ui_rc_china);
                                        else if (position == 1)
                                            img_rocker_mode.setImageResource(R.mipmap.setting_ui_rc_usa);
                                        else if (position == 0)
                                            img_rocker_mode.setImageResource(R.mipmap.setting_ui_rc_japan);

                                    }
                                });
                            }

                        }
                    });
                }

            });

            //设置左上角轮子的作用
            spinner_ios_pyr.setOnItemSelectedListener(new JackSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(JackSpinner view, int position, long id, Object item) {
                    JackApplication.getAircraftInstance().getRemoteController().setLeftWheelGimbalControlAxis(GimbalAxis.find(position), new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {

                        }
                    });
                }

            });

            //设置给移动设备充电
            spinner_ios_phone.setOnItemSelectedListener(new JackSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(JackSpinner view, int position, long id, Object item) {
                    JackApplication.getAircraftInstance().getRemoteController().setChargeMobileMode(ChargeMobileMode.find(position), new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {

                        }
                    });
                }
            });

            //设置遥控器的名称
            edit_remote_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String remote = getBinding().getStrRemoteName().toString() + "";
                            if (!remote.isEmpty() && !remote.equals("")) {
                                JackApplication.getAircraftInstance().getRemoteController().setName(remote, new CommonCallbacks.CompletionCallback() {
                                    @Override
                                    public void onResult(DJIError djiError) {

                                    }
                                });
                            }
                        }
                    }, 3000);
                }
            });

            //设置遥控器的密码
            edit_remote_pwd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String remotePwd = getBinding().getStrRemotePwd().toString().trim();
                            if (!remotePwd.isEmpty() && !remotePwd.equals("")) {
                                JackApplication.getAircraftInstance().getRemoteController().setPassword(remotePwd, new CommonCallbacks.CompletionCallback() {
                                    @Override
                                    public void onResult(DJIError djiError) {

                                    }
                                });
                            }
                        }
                    }, 3000);
                }
            });
        }
    }


    @OnClick(R.id.txt_remote_frequency)
    public void ParringRemote(View v) {
        //进入配对模式
        /*DialogPairimg dialog = new DialogPairimg();
        dialog.show(JackApplication.fragmentManager, "ip_dialog");*/
    }

    @Override
    protected int setLayoutId() {
        return R.layout.setting_remote_fragment;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

    @Override
    protected void onDelayLoad() {
        if (ModuleVerificationUtil.isRemoteControllerAvailable()) {
            if (JackApplication.getAircraftInstance().getRemoteController().isConnected()) {

                // 获取摇杆模式
                JackApplication.getAircraftInstance().getRemoteController().getAircraftMappingStyle(new CommonCallbacks.CompletionCallbackWith<AircraftMappingStyle>() {
                    @Override
                    public void onSuccess(AircraftMappingStyle aircraftMappingStyle) {
                        final int numb = aircraftMappingStyle.value();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spinner_rocker_mode.setSelectedIndex(numb - 1);
                                if (numb == 3)
                                    img_rocker_mode.setImageResource(R.mipmap.setting_ui_rc_china);
                                else if (numb == 2)
                                    img_rocker_mode.setImageResource(R.mipmap.setting_ui_rc_usa);
                                else if (numb == 1)
                                    img_rocker_mode.setImageResource(R.mipmap.setting_ui_rc_japan);
                                else if (numb == 255)
                                    img_rocker_mode.setImageResource(R.mipmap.setting_ui_rc_custom);
                            }
                        });
                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }
                });


                //获取遥控器的名称
                JackApplication.getAircraftInstance().getRemoteController().getName(new CommonCallbacks.CompletionCallbackWith<String>() {
                    @Override
                    public void onSuccess(String s) {
                        getBinding().setStrRemoteName(s);
                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }
                });

                //获取遥控器的固件版本
                JackApplication.getAircraftInstance().getRemoteController().getFirmwareVersion(new CommonCallbacks.CompletionCallbackWith<String>() {
                    @Override
                    public void onSuccess(String s) {
                        getBinding().setStrRemoteVersion(s);
                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }
                });

                //获取遥控器的密码
                JackApplication.getAircraftInstance().getRemoteController().getPassword(new CommonCallbacks.CompletionCallbackWith<String>() {
                    @Override
                    public void onSuccess(String s) {
                        getBinding().setStrRemotePwd(s);
                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }
                });

                //获取遥控器电量
                JackApplication.getAircraftInstance().getRemoteController().setChargeRemainingCallback(new ChargeRemaining.Callback() {
                    @Override
                    public void onUpdate(@NonNull ChargeRemaining chargeRemaining) {
                        getBinding().setStrRemoteBattery(chargeRemaining.getRemainingChargeInPercent() + "%");
                    }
                });

                //获取是否给移动设备充电
                JackApplication.getAircraftInstance().getRemoteController().getChargeMobileMode(new CommonCallbacks.CompletionCallbackWith<ChargeMobileMode>() {
                    @Override
                    public void onSuccess(ChargeMobileMode chargeMobileMode) {
                        int num = chargeMobileMode.value();
                        spinner_ios_phone.setSelectedIndex(num);
                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }
                });

                //获取左上角驳轮的作用
                JackApplication.getAircraftInstance().getRemoteController().getLeftWheelGimbalControlAxis(new CommonCallbacks.CompletionCallbackWith<GimbalAxis>() {
                    @Override
                    public void onSuccess(final GimbalAxis gimbalAxis) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spinner_ios_pyr.setSelectedIndex(gimbalAxis.value());
                            }
                        });
                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }
                });


                //获取自定义ButtonC1,C2
                JackApplication.getAircraftInstance().getRemoteController().getCustomButtonTags(new CommonCallbacks.CompletionCallbackWith<CustomButtonTags>() {
                    @Override
                    public void onSuccess(CustomButtonTags customButtonTags) {
                        final short c1ButtonTag = customButtonTags.getC1ButtonTag();
                        final short c2ButtonTag = customButtonTags.getC2ButtonTag();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                /*spinner_custom_c1.setSelectedIndex(c1ButtonTag);
                                spinner_custom_c2.setSelectedIndex(c2ButtonTag);*/
                            }
                        });
                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }
                });

            }
        }
    }
}
