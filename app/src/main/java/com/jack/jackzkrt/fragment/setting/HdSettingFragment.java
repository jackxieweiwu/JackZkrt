package com.jack.jackzkrt.fragment.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.jackspinnerlibrary.JackSpinner;
import com.jack.frame.core.AbsFragment;
import com.jack.frame.util.AdapterHelper;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.JackApplication;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.adapter.ReoutStatusRecyleViewAdapter;
import com.jack.jackzkrt.databinding.SettingHdFragmentBinding;

import butterknife.Bind;
import dji.common.LightbridgeSecondaryVideoFormat;
import dji.common.VideoDataChannel;
import dji.common.airlink.ChannelInterference;
import dji.common.airlink.ChannelSelectionMode;
import dji.common.airlink.LightbridgeAntennaRSSI;
import dji.common.airlink.LightbridgeDataRate;
import dji.common.airlink.LightbridgeSecondaryVideoOutputPort;
import dji.common.airlink.LightbridgeTransmissionMode;
import dji.common.error.DJIError;
import dji.common.util.CommonCallbacks;
import dji.sdk.airlink.LightbridgeLink;
import zkrt.ui.ModuleVerificationUtil;
import zkrtdrone.zkrt.com.maplib.info.Constanst;


/**
 * Created by jack_xie on 17-5-5.
 * HD Camera
 */

public class HdSettingFragment extends AbsFragment<SettingHdFragmentBinding> implements View.OnClickListener, JackSpinner.OnItemSelectedListener {
    @Bind(R.id.txt_camera_hd_custom) TextView txt_camera_hd_custom;  //自定义
    @Bind(R.id.txt_camera_hd_motion) TextView txt_camera_hd_motion;  //自动选择
    @Bind(R.id.txt_camera_mode_delay) TextView txt_camera_mode_delay;  //高画质
    @Bind(R.id.txt_camera_mode_quality) TextView txt_camera_mode_quality;  //低延迟
    @Bind(R.id.txt_camera_hd_hdmi) TextView txt_camera_hd_hdmi;  //HDMI
    @Bind(R.id.txt_camera_sdi) TextView txt_camera_sdi;  //SDI
    @Bind(R.id.spinner_camera_hd) JackSpinner spinner_camera_hd;
    @Bind(R.id.spinner_setting_video_mode) JackSpinner spinner_setting_video_mode;  //视频输出模式
    @Bind(R.id.spinner_setting_app_mode) JackSpinner spinner_setting_app_mode;  //APP输出模式
    @Bind(R.id.spinner_camera_format) JackSpinner spinner_camera_format;  //视频输出格式
    @Bind(R.id.setting_hd_camera_seek) SeekBar setting_hd_camera_seek;  //图传码率
    @Bind(R.id.setting_bandwidth_seek) SeekBar setting_bandwidth_seek;  //带宽分配
    @Bind(R.id.switch_ios_camera_ext) Switch switch_ios_camera_ext;  //开启EXT端口
    @Bind(R.id.switch_ios_camera) Switch switch_ios_camera;  //HDMI/SDI视频输出
    @Bind(R.id.recy_list_route) RecyclerView recy_list_route;
    @Bind(R.id.rela_xinhao_one) RelativeLayout rela_xinhao_one;
    @Bind(R.id.rela_xinhao_two) RelativeLayout rela_xinhao_two;
    @Bind(R.id.ext_rela) RelativeLayout ext_rela;
    @Bind(R.id.linear_video_in) LinearLayout linear_video_in;
    final String[] list = {"4Mbps(3km)", "6Mbps(2km)", "8Mbps(1.5km)", "10Mbps(0.7km)"};
    private String[] hdmiSdi = {"720P_60FPS", "720P_50FPS",
            "1080I_60FPS", "1080I_50FPS", "1080P_60FPS", "1080P_50FPS", "1080P_30FPS", "1080P_24FPS", "1080P_25FPS", "720P_30FPS", "720P_25FPS", "720P_24FPS"};
    private String[] appInputOne ={"AV","HDMI"};
    private String[] appInputTwo ={"LB","EXT"};

    private ChannelInterference[] channelInterferences_relay;
    private ReoutStatusRecyleViewAdapter mliveRoomRecyclerViewAdapter;
    private int sumRoutatus = -1;

    public static HdSettingFragment newInstance(){
        return new HdSettingFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getBinding().setStrTransmissionModeSw(false);
        getBinding().setStrHdmiSdiVideo(false);
        spinner_camera_format.setAdapter(AdapterHelper.getAdapter(mActivity,hdmiSdi));
        spinner_camera_hd.isPressed();
        switch_ios_camera_ext.isPressed();
        setting_hd_camera_seek.isPressed();
        /*txt_camera_hd_hdmi.isPressed();
        txt_camera_sdi.isPressed();*/
        switch_ios_camera.isPressed();
        spinner_setting_video_mode.isPressed();
        spinner_camera_format.isPressed();
        spinner_setting_app_mode.isPressed();
        if(channelInterferences_relay == null) channelInterferences_relay = new ChannelInterference[8];
        mliveRoomRecyclerViewAdapter = new ReoutStatusRecyleViewAdapter(mActivity,channelInterferences_relay);
        recy_list_route.setAdapter(mliveRoomRecyclerViewAdapter);
        recy_list_route.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recy_list_route.setLayoutManager(linearLayoutManager);

        spinner_setting_app_mode.setOnItemSelectedListener(this);
        //设置信道是否自动
        txt_camera_hd_motion.setOnClickListener(HdSettingFragment.this);
        txt_camera_hd_custom.setOnClickListener(HdSettingFragment.this);
        txt_camera_mode_quality.setOnClickListener(HdSettingFragment.this);
        txt_camera_mode_delay.setOnClickListener(HdSettingFragment.this);
        txt_camera_sdi.setOnClickListener(HdSettingFragment.this);
        txt_camera_hd_hdmi.setOnClickListener(HdSettingFragment.this);
        if (ModuleVerificationUtil.isAirlinkAvailable()) {

            //设置下行通道
            spinner_camera_hd.setOnItemSelectedListener(new JackSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(JackSpinner view, final int position, long id, Object item) {
                    int number  = (int) spinner_camera_hd.getItems().get(position);
                    JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setChannelNumber(number, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            T.show(mActivity,"pos:"+position+",dji:"+djiError.toString(),1);
                        }
                    });
                }
            });

            //设置图传码率
            setting_hd_camera_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    getBinding().setStrCodeRate(list[progress]);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final int number = seekBar.getProgress() + 1;
                            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setDataRate(LightbridgeDataRate.find(number), new CommonCallbacks.CompletionCallback() {
                                @Override
                                public void onResult(DJIError djiError) {
                                    if(djiError == null){
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                getBinding().setStrCodeRate(list[seekBar.getProgress()]);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }, 3000);
                }
            });

            //设置带宽分配
            setting_bandwidth_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    final int number = seekBar.getProgress();
                    if (Constanst.hd_lb_av)
                        getBinding().setStrHdmiAv("LB:" + number + "% EXT:" + (100 - number) + "%");
                    else
                        getBinding().setStrHdmiAv("HDMI:" + number + "% AV:" + (100 - number) + "%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    final int number = seekBar.getProgress();
                    //final boolean bool = getBinding().getStrTransmissionModeSw();
                    ext_rela.setVisibility(number == 100 ? View.VISIBLE : View.GONE);
                    if (Constanst.hd_lb_av)
                        getBinding().setStrHdmiAv("LB:" + number + "% EXT:" + (100 - number) + "%");
                    else
                        getBinding().setStrHdmiAv("HDMI:" + number + "% AV:" + (100 - number) + "%");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (Constanst.hd_lb_av) {
                                JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setBandwidthAllocationForLBVideoInputPort(number / 100f, new CommonCallbacks.CompletionCallback() {
                                    @Override
                                    public void onResult(DJIError djiError) {
                                        if (djiError == null)
                                            getBandWithAllocation(Constanst.hd_lb_av);
                                    }
                                });
                            } else {
                                JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setBandwidthAllocationForHDMIVideoInputPort(number / 100f, new CommonCallbacks.CompletionCallback() {
                                    @Override
                                    public void onResult(DJIError djiError) {
                                        if (djiError == null)
                                            getBandWithAllocation(Constanst.hd_lb_av);
                                    }
                                });
                            }
                        }
                    }, 3000);
                }
            });

            //开启或者关闭EXT端口
            switch_ios_camera_ext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setEXTVideoInputPortEnabled(isChecked,
                            new CommonCallbacks.CompletionCallback() {
                                @Override
                                public void onResult(final DJIError djiError) {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (djiError == null) {
                                                getBandWithAllocation(isChecked);
                                                ext_rela.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                                                if (isChecked) { //LB  /ext
                                                    Constanst.hd_lb_av = true;
                                                    spinner_setting_app_mode.setAdapter(AdapterHelper.getAdapter(mActivity, appInputTwo));
                                                } else {  //av   /hdmi
                                                    Constanst.hd_lb_av = false;
                                                    spinner_setting_app_mode.setAdapter(AdapterHelper.getAdapter(mActivity, appInputOne));
                                                }

                                                if (isChecked) {
                                                    getCameraQuaily();
                                                }
                                            }else{
                                                switch_ios_camera_ext.setChecked(false);
                                                Constanst.hd_lb_av = false;
                                            }
                                        }
                                    });
                                }
                            });
                }
            });

            //是否开启HDMI/SDI端口
            switch_ios_camera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setSecondaryVideoOSDEnabled(isChecked, new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(final DJIError djiError) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        getBinding().setStrHdmiSdiVideo(djiError == null?true:false);
//                                        linear_video_in.setVisibility(djiError==null?View.VISIBLE:View.GONE);
                                        getBinding().setStrHdmiSdiVideo(true);
                                        linear_video_in.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                    } else {
                        JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setSecondaryVideoOSDEnabled(isChecked, new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(final DJIError djiError) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getBinding().setStrHdmiSdiVideo(false);
                                        linear_video_in.setVisibility(View.GONE);
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        /*if (GeneralUtils.isFastDoubleClick())
            return;*/
        if (ModuleVerificationUtil.isAirlinkAvailable() /*&& JackApplication.getAircraftInstance().getAirLink().isConnected()*/) {
            switch (v.getId()) {
                case R.id.txt_camera_hd_custom://自定义
                    JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setChannelSelectionMode(ChannelSelectionMode.MANUAL, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(final DJIError djiError) {
                            if (djiError == null) {
//                                mActivity.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        rela_xinhao_one.setVisibility(View.VISIBLE);
//                                        rela_xinhao_two.setVisibility(View.VISIBLE);
//                                    }
//                                });
                                getChannelSelect();

                            } else {
                                T.show(mActivity, djiError.toString(), 1);
                            }
                        }
                    });
                    break;

                case R.id.txt_camera_hd_motion://自动选择
                    JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setChannelSelectionMode(ChannelSelectionMode.AUTO, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(final DJIError djiError) {
                            if (djiError == null) {
//                                mActivity.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        rela_xinhao_one.setVisibility(View.GONE);
//                                        rela_xinhao_two.setVisibility(View.GONE);
//                                    }
//                                });
                                getChannelSelect();
                            } else {
                                T.show(mActivity, djiError.toString(), 1);
                            }
                        }
                    });
                    break;

                case R.id.txt_camera_mode_quality:  //低延迟
                    JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setTransmissionMode(LightbridgeTransmissionMode.LOW_LATENCY, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if (djiError == null) {
                                getCameraQuaily();
                            }
                        }
                    });
                    break;

                case R.id.txt_camera_mode_delay:  //高画质
                    JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setTransmissionMode(LightbridgeTransmissionMode.HIGH_QUALITY, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if (djiError == null) {
                                getCameraQuaily();
                            }
                        }
                    });
                    break;

                case R.id.txt_camera_hd_hdmi:
                    JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setSecondaryVideoOutputPort(LightbridgeSecondaryVideoOutputPort.HDMI, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if (djiError == null) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txt_camera_hd_hdmi.setTextColor(Color.GREEN);
                                        txt_camera_sdi.setTextColor(Color.WHITE);
                                    }
                                });

                            }
                        }
                    });
                    break;
                case R.id.txt_camera_sdi:
                    JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setSecondaryVideoOutputPort(LightbridgeSecondaryVideoOutputPort.SDI, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt_camera_sdi.setTextColor(Color.GREEN);
                                    txt_camera_hd_hdmi.setTextColor(Color.WHITE);
                                }
                            });

                        }
                    });
                    break;
            }
        }
    }

    //获取是否自动信道
    private void getChannelSelect() {
        JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getChannelSelectionMode(new CommonCallbacks.CompletionCallbackWith<ChannelSelectionMode>() {
            @Override
            public void onSuccess(ChannelSelectionMode channelSelectionMode) {
                final int airlinkNumber = channelSelectionMode.value();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (airlinkNumber == 0) {  //auto
                            rela_xinhao_one.setVisibility(View.GONE);
                            rela_xinhao_two.setVisibility(View.GONE);
                            txt_camera_hd_motion.setTextColor(Color.GREEN);
                            txt_camera_hd_custom.setTextColor(Color.WHITE);
                        } else if (airlinkNumber == 1) {
                            rela_xinhao_one.setVisibility(View.VISIBLE);
                            rela_xinhao_two.setVisibility(View.VISIBLE);
                            txt_camera_hd_custom.setTextColor(Color.GREEN);
                            txt_camera_hd_motion.setTextColor(Color.WHITE);
                        }
//                        else {
//                            txt_camera_hd_custom.setTextColor(Color.WHITE);
//                            txt_camera_hd_motion.setTextColor(Color.WHITE);
//                        }
                    }
                });
            }

            @Override
            public void onFailure(DJIError djiError) {

            }
        });
    }

    //获取是否是低延迟还是高画质
    private void getCameraQuaily() {
        JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getTransmissionMode(new CommonCallbacks.CompletionCallbackWith<LightbridgeTransmissionMode>() {
            @Override
            public void onSuccess(LightbridgeTransmissionMode lightbridgeTransmissionMode) {
                final int number = lightbridgeTransmissionMode.value();
                if (number == 255)
                    return;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (number == 0) {
                            txt_camera_mode_delay.setTextColor(Color.GREEN);
                            txt_camera_mode_quality.setTextColor(Color.WHITE);
                        } else if (number == 1) {
                            txt_camera_mode_quality.setTextColor(Color.GREEN);
                            txt_camera_mode_delay.setTextColor(Color.WHITE);
                        }
//                        else {
//                            txt_camera_mode_delay.setTextColor(Color.WHITE);
//                            txt_camera_mode_quality.setTextColor(Color.WHITE);
//                        }
                    }
                });
            }

            @Override
            public void onFailure(DJIError djiError) {

            }
        });
    }

    //获取带宽分配
    private void getBandWithAllocation(boolean bool) {
        if (bool) { // LB/Ext
            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getBandwidthAllocationForLBVideoInputPort(new CommonCallbacks.CompletionCallbackWith<Float>() {
                @Override
                public void onSuccess(final Float aFloat) {
                    final int fnum = (int) (aFloat * 100);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getBinding().setStrHdmiAv("LB:" + fnum + "% EXT:" + (100 - fnum) + "%");
                            setting_bandwidth_seek.setProgress(fnum);
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        } else { //AV/HDMI
            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getBandwidthAllocationForHDMIVideoInputPort(new CommonCallbacks.CompletionCallbackWith<Float>() {
                @Override
                public void onSuccess(Float aFloat) {
                    final int fnum = (int) (aFloat * 100);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getBinding().setStrHdmiAv("HDMI:" + fnum + "% AV:" + (100 - fnum) + "%");
                            setting_bandwidth_seek.setProgress(fnum);
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        }
    }

    //获取辅助视频输出格式与模式
    private void getAuxiliaryVideoMode(boolean bool) {
        if (bool) { //hdmi
            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getSecondaryVideoOutputFormatForPort(LightbridgeSecondaryVideoOutputPort.HDMI, new CommonCallbacks.CompletionCallbackWith<LightbridgeSecondaryVideoFormat>() {
                @Override
                public void onSuccess(LightbridgeSecondaryVideoFormat lightbridgeSecondaryVideoFormat) {
                    spinner_camera_format.setSelectedIndex(lightbridgeSecondaryVideoFormat.value());
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        } else { //SDI
            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getSecondaryVideoOutputFormatForPort(LightbridgeSecondaryVideoOutputPort.SDI, new CommonCallbacks.CompletionCallbackWith<LightbridgeSecondaryVideoFormat>() {
                @Override
                public void onSuccess(LightbridgeSecondaryVideoFormat lightbridgeSecondaryVideoFormat) {
                    spinner_camera_format.setSelectedIndex(lightbridgeSecondaryVideoFormat.value());
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        }
    }

    @Override
    protected void onDelayLoad() {
        if (ModuleVerificationUtil.isAirlinkAvailable()) {
            //检查设备是否开启了EXT端口
            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getEXTVideoInputPortEnabled(new CommonCallbacks.CompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(final Boolean aBoolean) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch_ios_camera_ext.setChecked(aBoolean);
                            if(aBoolean){ //LB  /ext
                                spinner_setting_app_mode.setAdapter(AdapterHelper.getAdapter(mActivity,appInputTwo));
                            }else{  //av   /hdmi
                                spinner_setting_app_mode.setAdapter(AdapterHelper.getAdapter(mActivity,appInputOne));
                            }
                            Constanst.hd_lb_av = aBoolean;
                        }
                    });
                }
                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取是否自动信道
            getChannelSelect();

            //获取所有的下行信道
            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getChannelRange(new CommonCallbacks.CompletionCallbackWith<Integer[]>() {
                @Override
                public void onSuccess(final Integer[] integers) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinner_camera_hd.setItems(integers);
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取图传码率
            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getDataRate(new CommonCallbacks.CompletionCallbackWith<LightbridgeDataRate>() {
                @Override
                public void onSuccess(LightbridgeDataRate lightbridgeDataRate) {
                    final int codeRateNum = lightbridgeDataRate.value();
                    if (codeRateNum == 255)
                        return;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getBinding().setStrCodeRate(list[codeRateNum - 1]);
                            setting_hd_camera_seek.setProgress(codeRateNum);
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取是否开启了EXT端口
            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getEXTVideoInputPortEnabled(new CommonCallbacks.CompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(final Boolean aBoolean) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getBinding().setStrTransmissionModeSw(aBoolean);
                            ext_rela.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                            getBandWithAllocation(aBoolean);//获取带宽分配
                        }
                    });
                    if (aBoolean)
                        getCameraQuaily();
                }

                @Override
                public void onFailure(DJIError djiError) {
                }
            });

            //检查辅助视频输出是否支持
            if (JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().isSecondaryVideoOutputSupported()) {
                //获取是否HDMI/SDI视频输出
                JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getSecondaryVideoOutputEnabled(new CommonCallbacks.CompletionCallbackWith<Boolean>() {
                    @Override
                    public void onSuccess(final Boolean aBoolean) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getBinding().setStrHdmiSdiVideo(aBoolean);
                                linear_video_in.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }
                });
            } else {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linear_video_in.setVisibility(View.GONE);
                    }
                });
            }

            //辅助视频输出的通道
            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getSecondaryVideoOutputPort(new CommonCallbacks.CompletionCallbackWith<LightbridgeSecondaryVideoOutputPort>() {
                @Override
                public void onSuccess(LightbridgeSecondaryVideoOutputPort lightbridgeSecondaryVideoOutputPort) {
                    final int number = lightbridgeSecondaryVideoOutputPort.value();
                    if (number == 255)
                        return;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (number == 0) { //HDMI
                                txt_camera_hd_hdmi.setTextColor(Color.GREEN);
                            } else if (number == 1) {  //SDI
                                txt_camera_sdi.setTextColor(Color.GREEN);
                            } else {
                                txt_camera_sdi.setTextColor(Color.WHITE);
                                txt_camera_hd_hdmi.setTextColor(Color.WHITE);
                            }
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取遥控器与天空端的RSSI
            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setAircraftAntennaRSSICallback(new LightbridgeLink.AntennaRSSICallback() {
                @Override
                public void onUpdate(LightbridgeAntennaRSSI lightbridgeAntennaRSSI) {
                    getBinding().setStrAntennaOne(lightbridgeAntennaRSSI.getAntenna1() + "dBm");
                    getBinding().setStrAntennatwo(lightbridgeAntennaRSSI.getAntenna2() + "dBm");

                    if (JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink() == null){
                        return;
                    }
                    //获取自定义的下行通道数
                    JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().getChannelNumber(new CommonCallbacks.CompletionCallbackWith<Integer>() {
                        @Override
                        public void onSuccess(final Integer integer) {
                            sumRoutatus = integer;
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(sumRoutatus-13>-1 && sumRoutatus-13<9)
                                    spinner_camera_hd.setSelectedIndex(sumRoutatus-13);
                                    else spinner_camera_hd.setSelectedIndex(sumRoutatus-12);
                                }
                            });

                        }
                        @Override
                        public void onFailure(DJIError djiError) {
                        }
                    });
                }
            });

            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setRemoteControllerAntennaRSSICallback(new LightbridgeLink.AntennaRSSICallback() {
                @Override
                public void onUpdate(LightbridgeAntennaRSSI lightbridgeAntennaRSSI) {
                    getBinding().setStrAntennaThree(lightbridgeAntennaRSSI.getAntenna1() + "dBm");
                    getBinding().setStrAntennaFour(lightbridgeAntennaRSSI.getAntenna2() + "dBm");
                }
            });


            //获取通道干扰量
            JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setChannelInterferenceCallback(new LightbridgeLink.ChannelInterferenceCallback() {
                @Override
                public void onResult(final ChannelInterference[] channelInterferences) {
                    channelInterferences_relay = channelInterferences;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mliveRoomRecyclerViewAdapter !=null && channelInterferences.length>0) {
                                mliveRoomRecyclerViewAdapter.initData(channelInterferences,sumRoutatus);
                                mliveRoomRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
        }

    }

    @Override
    public void onItemSelected(JackSpinner view, int position, long id, Object item) {
        if (Constanst.hd_lb_av){
            switch (position){ //LB  /ext
                case 0:
                    CustomSetVideoChannel(VideoDataChannel.HD_GIMBAL);
                    break;
                case 1:
                    CustomSetVideoChannel(VideoDataChannel.FPV_CAMERA);
                    break;
            }
        }else {
            switch (position){//av   /hdmi
                case 0:
                    CustomSetVideoChannel(VideoDataChannel.AV);
                    break;
                case 1:
                    CustomSetVideoChannel(VideoDataChannel.HDMI);
                    break;
            }
        }
    }

    private void CustomSetVideoChannel(VideoDataChannel vv){
        /*JackApplication.getAircraftInstance().getAirLink().getLightbridgeLink().setVideoDataChannel(vv, new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if (djiError == null){
                    T.show(mActivity,"videoChannel success",1);
                }else {
                    T.show(mActivity,"videoChannel fail"+djiError.toString(),1);
                }
            }
        });*/
    }


    @Override
    protected int setLayoutId() {
        return R.layout.setting_hd_fragment;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }


}
