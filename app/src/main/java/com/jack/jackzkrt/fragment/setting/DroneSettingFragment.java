package com.jack.jackzkrt.fragment.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;


import com.example.jackspinnerlibrary.JackSpinner;
import com.jack.frame.core.AbsFragment;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.JackApplication;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.databinding.SettingDroneFragmentBinding;
import com.jack.jackzkrt.dialog.SettingEditTextDialog;

import butterknife.Bind;
import butterknife.OnClick;
import dji.common.error.DJIError;
import dji.common.flightcontroller.ConnectionFailSafeBehavior;
import dji.common.flightcontroller.FlightOrientationMode;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CommonCallbacks;
import zkrt.ui.ModuleVerificationUtil;
import zkrtdrone.zkrt.com.interf.CallBackExitText;
import zkrtdrone.zkrt.com.maplib.info.Constanst;

import static com.jack.frame.PublicStackNUm.droneloLat;
import static com.jack.frame.PublicStackNUm.droneloLng;
import static java.lang.Double.NaN;


/**
 * Created by jack_xie on 17-5-5.
 * Drone
 */

public class DroneSettingFragment extends AbsFragment<SettingDroneFragmentBinding> {
    @Bind(R.id.distance_ios_led) Switch distance_ios_led;
    @Bind(R.id.distance_ios_fly) Switch distance_ios_fly;
    @Bind(R.id.fly_ios) Switch fly_ios;
    @Bind(R.id.edit_setting_drone_gohome_hight) TextView edit_setting_drone_gohome_hight;
    @Bind(R.id.edit_setting_fly_max_hight) TextView edit_setting_fly_max_hight;
    @Bind(R.id.edit_setting_fly_radius_distance) TextView edit_setting_fly_radius_distance;
    @Bind(R.id.spinner_outof) JackSpinner spinner_outof;
    @Bind(R.id.spinner_heading)JackSpinner spinner_heading;
    @Bind(R.id.rela_dis_number) RelativeLayout rela_dis_number;
    @Bind(R.id.txt_setting_fly_radius_distance) TextView txt_setting_fly_radius_distance;
    @Bind(R.id.txt_setting_fly_max_hight) TextView txt_setting_fly_max_hight;
    private SettingEditTextDialog settingEditTextDialog;

    public static DroneSettingFragment newInstance(){
        return new DroneSettingFragment();
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        fly_ios.isPressed();
        settingEditTextDialog = new SettingEditTextDialog(mActivity);
        if (ModuleVerificationUtil.isFlightControllerAvailable()) {
            //设置是否处于新手模式
            fly_ios.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //高度限制在30m，距离限制在50m
//                        setMaxHightDrone30();
                        setMaxHightDrone(30);
                        setMaxRadiusDrone(50);
                        distance_ios_fly.setChecked(true);
                        distance_ios_fly.setClickable(false);
                        edit_setting_fly_radius_distance.setVisibility(View.INVISIBLE);
                        txt_setting_fly_radius_distance.setVisibility(View.VISIBLE);
                        txt_setting_fly_max_hight.setVisibility(View.VISIBLE);
                        edit_setting_fly_max_hight.setVisibility(View.INVISIBLE);
                        setGoHomeHeight(20);
                    } else {
                        setMaxHightDrone(20);
                        setMaxRadiusDrone(500);
                        edit_setting_fly_radius_distance.setVisibility(View.VISIBLE);
                        txt_setting_fly_radius_distance.setVisibility(View.GONE);
                        txt_setting_fly_max_hight.setVisibility(View.GONE);
                        edit_setting_fly_max_hight.setVisibility(View.VISIBLE);
                        distance_ios_fly.setChecked(false);
                        distance_ios_fly.setClickable(true);
                    }
                }
            });

            //设置失控行为
            spinner_outof.setOnItemSelectedListener(new JackSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(JackSpinner view, int position, long id, Object item) {
                    JackApplication.getAircraftInstance().getFlightController().setConnectionFailSafeBehavior(ConnectionFailSafeBehavior.find(position), new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                        }
                    });
                }
            });

            //设置飞行器方向锁
            spinner_heading.setOnItemSelectedListener(new JackSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(JackSpinner view, int position, long id, Object item) {
                    int number = -1;
                    if (position == 0)
                        number = 255;
                    else
                        number = position;
                    JackApplication.getAircraftInstance().getFlightController().setFlightOrientationMode(FlightOrientationMode.find(number), new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {

                        }
                    });
                }
            });

            //设置LED是否闪烁
            distance_ios_led.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    JackApplication.getAircraftInstance().getFlightController().setLEDsEnabled(isChecked, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {

                        }
                    });
                }
            });

            //设置距离限制
            distance_ios_fly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        JackApplication.getAircraftInstance().getFlightController().setMaxFlightRadiusLimitationEnabled(isChecked, new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(final DJIError djiError) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rela_dis_number.setVisibility(View.VISIBLE);
                                        if (djiError == null)
                                            getDroneRadius();
                                    }
                                });
                            }
                        });
                    } else {
                        JackApplication.getAircraftInstance().getFlightController().setMaxFlightRadiusLimitationEnabled(isChecked, new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(final DJIError djiError) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rela_dis_number.setVisibility(View.GONE);
                                        if (djiError == null)
                                            getDroneRadius();
                                    }
                                });
                            }
                        });
                    }
                }
            });


            //设置反航高度
            edit_setting_drone_gohome_hight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(settingEditTextDialog !=null) settingEditTextDialog.dismiss();
                    settingEditTextDialog.setTextName("返航高度",20,500,"M", new CallBackExitText() {
                        @Override
                        public void exitName(final int name) {
                            float maxdroneh = Float.parseFloat(edit_setting_fly_max_hight.getText().toString().trim());
                            if(name>maxdroneh){T.show(mActivity,"返航高度必须低于飞行高度",1); return;}
                            setGoHomeHeight(name);
                        }
                    },3);
                    settingEditTextDialog.show();
                }
            });

            //设置飞行高度限制
            edit_setting_fly_max_hight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(settingEditTextDialog !=null) settingEditTextDialog.dismiss();
                    settingEditTextDialog.setTextName("设置飞行高度",20,500,"M", new CallBackExitText() {
                        @Override
                        public void exitName(final int name) {
                            setMaxHightDrone(name);
                        }
                    },3);
                    settingEditTextDialog.show();
                }
            });

            //设定飞机的最大飞行半径限制
            edit_setting_fly_radius_distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(settingEditTextDialog !=null) settingEditTextDialog.dismiss();
                    settingEditTextDialog.setTextName("设置飞行半径",15,8000,"M", new CallBackExitText() {
                        @Override
                        public void exitName(final int name) {
                            getBinding().setStrDroneRadiusBooleanNumber(name + "");
                            setMaxRadiusDrone(name);
                        }
                    },4);
                    settingEditTextDialog.show();
                }
            });
        }
    }

    //将返航高度设置回20
    private void setGoHomeHeight(final int gohomenNum) {
        JackApplication.getAircraftInstance().getFlightController().setGoHomeHeightInMeters(gohomenNum, new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if (djiError == null) {
                    getBinding().setStrGohomeHiget(gohomenNum + "");
                    Constanst.goHome = gohomenNum;
                }
            }
        });
    }

    //设置飞行高度限制
    private void setMaxHightDrone(final int f) {
        JackApplication.getAircraftInstance().getFlightController().setMaxFlightHeight(f, new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if (djiError == null){
                    getBinding().setStrHightDrone(f + "");
                    Constanst.fMaxFlightHeight = f;
                }
            }
        });
    }


    //设置最大的飞行半径
    private void setMaxRadiusDrone(final int f) {
        JackApplication.getAircraftInstance().getFlightController().setMaxFlightRadius(f, new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if (djiError == null){
                    getBinding().setStrDroneRadiusBooleanNumber(f + "");
                    Constanst.iMaxFlightRadius = f;
                }
            }
        });
    }

    //定义反航点（操作者当前的位置）
    @OnClick(R.id.img_gohome_setting_peplo)
    public void onClickGoHomePeploGPS(View v) {
        if (ModuleVerificationUtil.isFlightControllerAvailable()) {
            if(droneloLat==NaN){
                T.show(mActivity,"坐标获取失败",1);
                return;
            }
            JackApplication.getAircraftInstance().getFlightController().setHomeLocation(new LocationCoordinate2D(droneloLat, droneloLng), new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                }
            });
        }
    }

    //定义反航点（飞行器当前的位置为反航点）
    @OnClick(R.id.img_gohome_setting_drone)
    public void onClickGOHomeDroneGPS(View v) {
        if (ModuleVerificationUtil.isFlightControllerAvailable()) {
            JackApplication.getAircraftInstance().getFlightController().setHomeLocationUsingAircraftCurrentLocation(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {

                }
            });
        }
    }

    @Override
    protected void onDelayLoad() {
        if (ModuleVerificationUtil.isFlightControllerAvailable()) {
            //初始化失控行为的adapter
            if (spinner_outof != null)spinner_outof.setItems("悬停", "降落", "返航");
            if (spinner_heading != null)spinner_heading.setItems("机头锁", "航向锁", "返航锁");
            spinner_outof.isPressed();
            spinner_heading.isPressed();

            //LED
            JackApplication.getAircraftInstance().getFlightController().getLEDsEnabled(new CommonCallbacks.CompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(final Boolean aBoolean) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            distance_ios_led.setChecked(aBoolean);
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //得到反航高度
            JackApplication.getAircraftInstance().getFlightController().getGoHomeHeightInMeters(new CommonCallbacks.CompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    getBinding().setStrGohomeHiget(integer + "");
                    Constanst.goHome = integer;
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获得飞机的最大飞行高度限制。
            JackApplication.getAircraftInstance().getFlightController().getMaxFlightHeight(new CommonCallbacks.CompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    getBinding().setStrHightDrone(integer + "");
                    Constanst.fMaxFlightHeight = integer;
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获得飞机的最大飞行半径限制。
            getDroneRadius();

            //获得是否开启限制飞行半径
            JackApplication.getAircraftInstance().getFlightController().getMaxFlightRadiusLimitationEnabled(new CommonCallbacks.CompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(final Boolean aBoolean) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (aBoolean)getDroneRadius();
                            distance_ios_fly.setChecked(aBoolean);
                            rela_dis_number.setVisibility(aBoolean == true ? View.VISIBLE : View.GONE);
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {
                }
            });
        }
    }

    //获取最大的飞行半径限制
    private void getDroneRadius() {
        JackApplication.getAircraftInstance().getFlightController().getMaxFlightRadius(new CommonCallbacks.CompletionCallbackWith<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                getBinding().setStrDroneRadiusBooleanNumber(integer + "");
                Constanst.iMaxFlightRadius = integer;
            }

            @Override
            public void onFailure(DJIError djiError) {

            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.setting_drone_fragment;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
    }

}
