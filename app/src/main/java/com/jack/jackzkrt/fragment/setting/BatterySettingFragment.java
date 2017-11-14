package com.jack.jackzkrt.fragment.setting;

import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;

import com.example.jackspinnerlibrary.JackSpinner;
import com.jack.frame.core.AbsFragment;
import com.jack.frame.util.MathUtil;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.JackApplication;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.databinding.SettingBatteryFragmentBinding;
import com.jack.jackzkrt.fragment.FpvOrMapFragment;

import butterknife.Bind;
import dji.common.battery.LowVoltageBehavior;
import dji.common.error.DJIError;
import dji.common.util.CommonCallbacks;
import zkrt.ui.ModuleVerificationUtil;

import static com.jack.frame.PublicStackNUm.batteryS;

/**
 * Created by jack_xie on 17-5-5.
 * Battery
 */

public class BatterySettingFragment extends AbsFragment<SettingBatteryFragmentBinding> implements JackSpinner.OnItemSelectedListener {
    @Bind(R.id.low_battery_spinner) JackSpinner low_battery_spinner;
    @Bind(R.id.kg_low_battery_spinner)JackSpinner kg_low_battery_spinner;
    @Bind(R.id.s_battery_spinner) JackSpinner s_battery_spinner;
    @Bind(R.id.battert_seekbar_one) SeekBar battert_seekbar_one;
    @Bind(R.id.battert_seekbar_two) SeekBar battert_seekbar_two;

    public static BatterySettingFragment newInstance(){
        return new BatterySettingFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (low_battery_spinner != null) low_battery_spinner.setItems("LED", "返航", "降落");
        if (kg_low_battery_spinner != null) kg_low_battery_spinner.setItems("LED", "返航", "降落");
        if (s_battery_spinner != null)s_battery_spinner.setItems("3S", "4S", "5S", "6S", "7S", "8S", "9S", "10S", "11S", "12S");
        low_battery_spinner.setOnItemSelectedListener(this);
        kg_low_battery_spinner.setOnItemSelectedListener(this);
        s_battery_spinner.setOnItemSelectedListener(this);
        battert_seekbar_one.isPressed();
        battert_seekbar_two.isPressed();
        low_battery_spinner.isPressed();
        kg_low_battery_spinner.isPressed();
        s_battery_spinner.isPressed();
        s_battery_spinner.setSelectedIndex(9);

        if(ModuleVerificationUtil.isAircraft()) {
            //设置低电压报警值
            battert_seekbar_one.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    final int batteryLow = seekBar.getProgress();
                    final int temp;
                    if (batteryLow==0){
                        temp = 3700;
                    }else {
                        temp = 3700 + batteryLow/13;
                    }
                    getBinding().setStrBatteryLow(MathUtil.round((temp > 4000 ? 4000 :temp) / 1000d * batteryS,2,4) + "");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    final int batteryLow = seekBar.getProgress();
                    final int temp;
                    if (batteryLow==0){
                        temp = 3700;
                    }else {
                        temp = 3700 + batteryLow/13;
                    }
                    getBinding().setStrBatteryLow(MathUtil.round((temp > 4000 ? 4000 :temp) / 1000d * batteryS,2,4) + "");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (ModuleVerificationUtil.isAircraft()) {// && JackApplication.getAircraftInstance().getBattery().isConnected()
                                JackApplication.getAircraftInstance().getBattery().setLevel1CellVoltageThreshold(temp, new CommonCallbacks.CompletionCallback() {
                                    @Override
                                    public void onResult(DJIError djiError) {
                                        if (djiError != null){
                                            T.show(mActivity,"batteryLow:"+djiError.toString(),1000);
                                        }
                                    }
                                });
                            }
                        }
                    }, 3000);
                }
            });
            //设置严重低电压报警值
            battert_seekbar_two.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    final int kgbatteryLow = seekBar.getProgress();
                    final int temp;
                    if (kgbatteryLow == 0){
                        temp = 3500;
                    }else {
                        temp = 3500 + kgbatteryLow / 18;
                    }
                    getBinding().setStrBatteryKgLow(MathUtil.round(temp / 1000d * batteryS,2,4) + "");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    final int kgbatteryLow = seekBar.getProgress();
                    final int temp;
                    if (kgbatteryLow == 0){
                        temp = 3500;
                    }else {
                        temp = 3500 + kgbatteryLow / 18;
                    }
                    getBinding().setStrBatteryKgLow(MathUtil.round(temp / 1000d * batteryS,2,4) + "");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (ModuleVerificationUtil.isAircraft() && JackApplication.getAircraftInstance().getBattery().isConnected()) {
                                JackApplication.getAircraftInstance().getBattery().setLevel2CellVoltageThreshold(temp, new CommonCallbacks.CompletionCallback() {
                                    @Override
                                    public void onResult(DJIError djiError) {
                                        if (djiError != null){
                                            T.show(mActivity,"kgbatteryLow:"+djiError.toString(),1000);
                                        }
                                    }
                                });
                            }
                        }
                    }, 3000);
                }
            });
        }
    }

    //设置电芯数
    @Override
    public void onItemSelected(JackSpinner view, int position, long id, Object item) {
        int number = -1;
        if (position == 3)
            number = 255;
        else
            number = position;
        switch (view.getId()) {
            case R.id.low_battery_spinner://低电压动作
                if (ModuleVerificationUtil.isAircraft() && JackApplication.getAircraftInstance().getBattery().isConnected()) {
                    JackApplication.getAircraftInstance().getBattery().setLevel1CellVoltageBehavior(LowVoltageBehavior.find(number), new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {

                        }
                    });
                }
                break;
            case R.id.kg_low_battery_spinner: //严重低电压动作
                if (ModuleVerificationUtil.isAircraft() && JackApplication.getAircraftInstance().getBattery().isConnected()) {
                    JackApplication.getAircraftInstance().getBattery().setLevel2CellVoltageBehavior(LowVoltageBehavior.find(number), new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {

                        }
                    });
                }
                break;

            case R.id.s_battery_spinner:
                batteryS = position + 3;
                if (ModuleVerificationUtil.isAircraft() && JackApplication.getAircraftInstance().getBattery().isConnected()) {
                    JackApplication.getAircraftInstance().getBattery().setNumberOfCells(batteryS, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError ==null){
                                onDelayLoad();
                            }
                        }
                    });
                }
                break;
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.setting_battery_fragment;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

    @Override
    protected void onDelayLoad() {
        if (ModuleVerificationUtil.isAircraft()) {
            //获取低电压的值
            if (JackApplication.getAircraftInstance().getBattery().isConnected()) {
                JackApplication.getAircraftInstance().getBattery().getLevel1CellVoltageThreshold(new CommonCallbacks.CompletionCallbackWith<Integer>() {
                    @Override
                    public void onSuccess(final Integer integer) {
                        final double leve1 = (integer)/1000d*batteryS;
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getBinding().setStrBatteryLow(MathUtil.round(leve1,2,4) + "");
                                battert_seekbar_one.setProgress(integer);
                            }
                        });
                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }
                });
            }

            //获取严重低电压的值
            if (JackApplication.getAircraftInstance().getBattery().isConnected()) {
                JackApplication.getAircraftInstance().getBattery().getLevel2CellVoltageThreshold(new CommonCallbacks.CompletionCallbackWith<Integer>() {
                    @Override
                    public void onSuccess(final Integer integer) {
                        final double leve2 = (integer/1000d)*batteryS;
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getBinding().setStrBatteryKgLow(MathUtil.round(leve2,2,4) + "");
                                battert_seekbar_two.setProgress(integer);
                            }
                        });
                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }
                });
            }

            //获取低电压动作
            if (JackApplication.getAircraftInstance().getBattery().isConnected()) {
                JackApplication.getAircraftInstance().getBattery().getLevel1CellVoltageBehavior(new CommonCallbacks.CompletionCallbackWith<LowVoltageBehavior>() {
                    @Override
                    public void onSuccess(final LowVoltageBehavior lowVoltageBehavior) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                low_battery_spinner.setSelectedIndex(lowVoltageBehavior.value());
                            }
                        });

                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }
                });
            }

            //获取严重低电压动作
            if (JackApplication.getAircraftInstance().getBattery().isConnected()) {
                JackApplication.getAircraftInstance().getBattery().getLevel2CellVoltageBehavior(new CommonCallbacks.CompletionCallbackWith<LowVoltageBehavior>() {
                    @Override
                    public void onSuccess(final LowVoltageBehavior lowVoltageBehavior) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                kg_low_battery_spinner.setSelectedIndex(lowVoltageBehavior.value());
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
