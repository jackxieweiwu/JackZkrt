package com.jack.jackzkrt.fragment.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;


import com.example.jackspinnerlibrary.JackSpinner;
import com.jack.frame.core.AbsFragment;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.JackApplication;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.databinding.SettingHolderFragmentBinding;

import butterknife.Bind;
import butterknife.OnClick;
import dji.common.error.DJIError;
import dji.common.gimbal.Axis;
import dji.common.gimbal.MovementSettingsProfile;
import dji.common.util.CommonCallbacks;
import zkrt.ui.ModuleVerificationUtil;


/**
 * Created by jack_xie on 17-5-5.
 * 云台
 */

public class HolderSettingFragment extends AbsFragment<SettingHolderFragmentBinding> {
    @Bind(R.id.spinner_holder_setting)JackSpinner spinner_holder_setting;
    @Bind(R.id.seekbar_bolder_one) SeekBar seekbar_bolder_one;
    @Bind(R.id.seekbar_bolder_two) SeekBar seekbar_bolder_two;
    @Bind(R.id.seekbar_bolder_three) SeekBar seekbar_bolder_three;
    @Bind(R.id.switch_ios_holder) Switch switch_ios_holder;
    //private String[] NameHolder = {"自定义1", "自定义2", "快速", "中", "慢"};

    public static HolderSettingFragment newInstance(){
        return new HolderSettingFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (spinner_holder_setting != null) {
            spinner_holder_setting.setItems("自定义1", "自定义2", "快速", "中", "慢");
        }

        switch_ios_holder.isPressed();
        if (ModuleVerificationUtil.isGimbalModuleAvailable()) {

            //设置云台俯仰轴
            seekbar_bolder_one.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int progress = seekBar.getProgress();
                    if (ModuleVerificationUtil.isGimbalModuleAvailable()) {
                        JackApplication.getAircraftInstance().getGimbal().setControllerDeadband(Axis.YAW, progress, new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {
                                T.show(mActivity, djiError == null ? "设置云台俯仰轴成功" : djiError.toString(),1);
                            }
                        });
                    }
                }
            });

            //设置云台的偏航值
            seekbar_bolder_two.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    final int progress = seekBar.getProgress();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getBinding().setStrSeekBarTwoTxt(progress + "");
                        }
                    });
                    if (ModuleVerificationUtil.isGimbalModuleAvailable()) {
                        JackApplication.getAircraftInstance().getGimbal().setControllerSpeedCoefficient(Axis.YAW, progress, new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {
                                T.show(mActivity, djiError == null ? "设置云台的偏航值" : djiError.toString(),1);
                            }
                        });
                    }
                }
            });

            //设置云台的俯仰平滑
            seekbar_bolder_three.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    final int progress = seekBar.getProgress();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getBinding().setStrSeekBarThreeTxt(progress + "");
                        }
                    });
                    if (ModuleVerificationUtil.isGimbalModuleAvailable()) {
                        JackApplication.getAircraftInstance().getGimbal().setControllerSmoothingFactor(Axis.YAW, progress, new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {
                                T.show(mActivity, djiError == null ? "设置云台的俯仰平滑" : djiError.toString(),1);
                            }
                        });
                    }
                }
            });

            //设置云台配置文件
            spinner_holder_setting.setOnItemSelectedListener(new JackSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(JackSpinner view, final int position, long id, Object item) {
                    if (ModuleVerificationUtil.isGimbalModuleAvailable()) {
                        MovementSettingsProfile djiGimbalAdvancedSettingsProfile = null;
                        if (position == 0) {
                            djiGimbalAdvancedSettingsProfile = MovementSettingsProfile.CUSTOM_1;
                        } else if (position == 1) {
                            djiGimbalAdvancedSettingsProfile = MovementSettingsProfile.CUSTOM_2;
                        } else if (position == 1) {
                            djiGimbalAdvancedSettingsProfile = MovementSettingsProfile.FAST;
                        } else if (position == 1) {
                            djiGimbalAdvancedSettingsProfile = MovementSettingsProfile.MEDIUM;
                        } else if (position == 1) {
                            djiGimbalAdvancedSettingsProfile = MovementSettingsProfile.SLOW;
                        }

                        JackApplication.getAircraftInstance().getGimbal().setMovementSettingsProfile(djiGimbalAdvancedSettingsProfile, new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {
                                if (djiError == null) {
                                    spinner_holder_setting.setSelectedIndex(position);
                                } else {
                                    T.show(mActivity, djiError.toString(),1);
                                }
                            }
                        });
                    }
                }
            });

            //Switch
            switch_ios_holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                        JackApplication.getAircraftInstance().getGimbal().setSmoothTrackEnabled(Axis.YAW, isChecked, new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {
                                getBinding().setBoolFollowGimbal(isChecked);
                            }
                        });
                }
            });

        }
    }

    //重置云台
    @OnClick(R.id.txt_holder_resetGimbal)
    public void resetGimbal(View v) {
        if (ModuleVerificationUtil.isGimbalModuleAvailable()) {
            JackApplication.getAircraftInstance().getGimbal().reset(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    T.show(mActivity, djiError == null ? "重置云台成功" : djiError.toString(),1);
                }
            });
        }
    }


    //恢复出厂设置
    @OnClick(R.id.txt_holder_reset)
    public void holderReset(View v) {
        if (ModuleVerificationUtil.isGimbalModuleAvailable()) {
            JackApplication.getAircraftInstance().getGimbal().restoreFactorySettings(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    T.show(mActivity, djiError == null ? "恢复出厂设置成功" : djiError.toString(),1);
                }
            });
        }
    }


    //云台校准
    @OnClick(R.id.txt_holder_correct)
    public void holderCorrect(View v) {
        if (ModuleVerificationUtil.isGimbalModuleAvailable()) {
            JackApplication.getAircraftInstance().getGimbal().startCalibration(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    T.show(mActivity, djiError == null ? "云台校准成功" : djiError.toString(),1);
                }
            });
        }
    }


    @Override
    protected void onDelayLoad() {
        if (ModuleVerificationUtil.isGimbalModuleAvailable()&&ModuleVerificationUtil.isHandHeld()) {

            //获得云台移动设置
            JackApplication.getAircraftInstance().getGimbal().getMovementSettingsProfile(new CommonCallbacks.CompletionCallbackWith<MovementSettingsProfile>() {
                @Override
                public void onSuccess(final MovementSettingsProfile movementSettingsProfile) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinner_holder_setting.setSelectedIndex(movementSettingsProfile.value());
                        }
                    });

                }

                @Override
                public void onFailure(final DJIError djiError) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            T.show(mActivity, djiError.toString(), 1000);
                        }
                    });
                }
            });


            //获取云台俯仰轴
            JackApplication.getAircraftInstance().getGimbal().getControllerDeadband(Axis.PITCH, new CommonCallbacks.CompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(final Integer integer) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            T.show(mActivity, "seekbar_bolder_one:" + integer, 1000);
                            seekbar_bolder_one.setProgress(integer);
                            getBinding().setStrSeekBarOneTxt(integer + "");
                        }
                    });
                }

                @Override
                public void onFailure(final DJIError djiError) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (djiError != null) {
                                T.show(mActivity, "one:" + djiError.toString(), 1000);
                            }
                        }
                    });
                }
            });

            //获取云台的偏航值
            JackApplication.getAircraftInstance().getGimbal().getControllerSpeedCoefficient(Axis.YAW, new CommonCallbacks.CompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(final Integer integer) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seekbar_bolder_two.setProgress(integer);
                            getBinding().setStrSeekBarTwoTxt(integer + "");
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

//             获取云台的俯仰平滑
            JackApplication.getAircraftInstance().getGimbal().getControllerSmoothingFactor(Axis.YAW, new CommonCallbacks.CompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(final Integer integer) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seekbar_bolder_three.setProgress(integer);
                            getBinding().setStrSeekBarThreeTxt(integer + "");
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });


            //获取设置是否平滑的bool值
            JackApplication.getAircraftInstance().getGimbal().getSmoothTrackEnabled(Axis.YAW, new CommonCallbacks.CompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    getBinding().setBoolFollowGimbal(aBoolean);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });


        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.setting_holder_fragment;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
