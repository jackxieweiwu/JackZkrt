package com.jack.jackzkrt.fragment;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jackspinnerlibrary.JackSpinner;
import com.github.mikephil.charting.charts.LineChart;
import com.jack.frame.core.AbsFragment;
import com.jack.frame.http.HttpUtil;
import com.jack.frame.util.CalendarUtils;
import com.jack.frame.util.SharePreUtil;
import com.jack.frame.util.byteUtil.ByteUtils;
import com.jack.frame.util.byteUtil.Utils;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.JackApplication;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.adapter.MoudleAdapter;
import com.jack.jackzkrt.bean.GasTime;
import com.jack.jackzkrt.bean.MavlinkDjiBean;
import com.jack.jackzkrt.bean.Moudle;
import com.jack.jackzkrt.bean.rxbean.CommonRxTask;
import com.jack.jackzkrt.bean.rxbean.IOTask;
import com.jack.jackzkrt.databinding.FragmentMountBinding;
import com.jack.jackzkrt.dialog.AvoidTemDialog;
import com.jack.jackzkrt.dialog.GasDialog;
import com.jack.jackzkrt.dialog.ObstacleAvoidanceDialog;
import com.jack.jackzkrt.widget.ExpandableGridView;
import com.jack.jackzkrt.widget.JoystickView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.Bind;
import butterknife.OnClick;
import dji.common.error.DJIError;
import dji.sdk.flightcontroller.FlightController;
import utils.ChartUtil;
import utils.KqwSpeechCompound;
import utils.rxutil.RxjavaUtil;
import zkrt.ui.ModuleVerificationUtil;
import zkrtdrone.zkrt.com.bean.exelBean;
import zkrtdrone.zkrt.com.maplib.info.Constanst;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MEDIA_PROJECTION_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.jack.frame.PublicStackNUm.droneAlt;
import static com.jack.frame.PublicStackNUm.droneloLat;
import static com.jack.frame.PublicStackNUm.droneloLng;
import static zkrtdrone.zkrt.com.maplib.info.mapApplication.handler;
import static zkrtdrone.zkrt.com.maplib.info.mapApplication.bool;

/**
 * Created by jack_xie on 17-4-27.
 */

public class MountFragment extends AbsFragment<FragmentMountBinding>
        implements CompoundButton.OnCheckedChangeListener, View.OnTouchListener, View.OnClickListener, JackSpinner.OnItemSelectedListener {
    @Bind(R.id.horiz_moudle_seekbar)
    ListView horiz_moudle_seekbar;
    @Bind(R.id.frame_mount)
    public FrameLayout frame_mount;
    @Bind(R.id.mount_clear)
    public ImageView mount_clear;
    @Bind(R.id.bar_char_linear)
    LinearLayout bar_char_linear;
    @Bind(R.id.grid_view)
    ExpandableGridView grid_view;
    @Bind(R.id.altitudeValue)
    TextView altitudeValue;
    @Bind(R.id.txt_front)
    TextView txt_front;
    @Bind(R.id.txt_back)
    TextView txt_back;
    @Bind(R.id.txt_left)
    TextView txt_left;
    @Bind(R.id.txt_right)
    TextView txt_right;
    @Bind(R.id.mount_cut)
    ImageView mount_cut;

    //虚拟摇杆控制
    @Bind(R.id.joystick_mount)
    JoystickView joystick_mount;
    @Bind(R.id.img_joystick_mount_close)
    ImageView img_joystick_mount_close;
    @Bind(R.id.relative_joystick)
    RelativeLayout relative_joystick;
    @Bind(R.id.seekbar_mount)
    SeekBar seekbar_mount;
    @Bind(R.id.img_camera_video_mount)
    ImageButton img_camera_video_mount;
    @Bind(R.id.switch_mount_photo_video)
    Switch switch_mount_photo_video;

    //双光相机控制
    @Bind(R.id.switch_txt_qh)
    Switch switch_txt_qh;  //可见光与红外
    @Bind(R.id.relative_joystick_dualsg)
    RelativeLayout relative_joystick_dualsg;
    @Bind(R.id.switch_txt_camera_video)
    Switch switch_txt_camera_video;  //拍照与录像
    @Bind(R.id.txt_dualsg_photo)
    TextView txt_dualsg_photo;  //拍照txt
    @Bind(R.id.txt_dualsg_video)
    TextView txt_dualsg_video;  //录像txt
    @Bind(R.id.img_camera_video_dualsg)
    ImageButton img_camera_video_dualsg;
    @Bind(R.id.button_color_camera_red)
    Button button_color_camera_red;  //红外颜色
    @Bind(R.id.img_joystick_sg_close)
    ImageView img_joystick_sg_close;


    @Bind(R.id.spinner_ch5)
    JackSpinner spinner_ch5;//夜视
    @Bind(R.id.btn_ch7)
    Button btn_ch7;//校准
    @Bind(R.id.spinner_ch9)
    JackSpinner spinner_ch9;//透雾
    @Bind(R.id.spinner_ch10)
    JackSpinner spinner_ch10;//电子稳像
    @Bind(R.id.diurnal_luminous)
    Button diurnal_luminous;//日夜光切换
    @Bind(R.id.zoom_seek)
    SeekBar seek_zoom;//变焦
    @Bind(R.id.tv_seek)
    TextView tv_seek;
    @Bind(R.id.stop_zoom)
    Button stop_zoom;//停止变焦

    /*@Bind(R.id.name_co) TextView nameCo;
    @Bind(R.id.name_co2) TextView nameCo2;
    @Bind(R.id.name_SO2) TextView nameSo2;*/
    @Bind(R.id.linechart_co)
    LineChart linechart_co;
    @Bind(R.id.linechart_co2)
    LineChart linechart_co2;
    @Bind(R.id.linechart_so2)
    LineChart linechart_so2;
    @Bind(R.id.linechart_h2s)
    LineChart linechart_h2s;
    @Bind(R.id.linechart_nh3)
    LineChart linechart_nh3;
    @Bind(R.id.linechart_voc)
    LineChart linechart_voc;
    @Bind(R.id.linechart_ch4)
    LineChart linechart_ch4;
    @Bind(R.id.linechart_cl2)
    LineChart linechart_cl2;


    //以下八个为当前气体当前数值
    @Bind(R.id.co_current_value)
    TextView tv_co_current_value;
    @Bind(R.id.co2_current_value)
    TextView tv_co2_current_value;
    @Bind(R.id.so2_current_value)
    TextView tv_so2_current_value;
    @Bind(R.id.h2s_current_value)
    TextView tv_h2s_current_value;
    @Bind(R.id.nh3_current_value)
    TextView tv_nh3_current_value;
    @Bind(R.id.voc_current_value)
    TextView tv_voc_current_value;
    @Bind(R.id.ch4_current_value)
    TextView tv_ch4_current_value;
    @Bind(R.id.cl2_current_value)
    TextView tv_cl2_current_value;


    @Bind(R.id.gas_time)
    TextView gas_time;


    //当前时间
//    @Bind(R.id.co_current_time)TextView tv_co_current_time;
//    @Bind(R.id.co2_current_time)TextView tv_co2_current_time;
//    @Bind(R.id.so2_current_time)TextView tv_so2_current_time;
//    @Bind(R.id.h2s_current_time)TextView tv_h2s_current_time;
//    @Bind(R.id.nh3_current_time)TextView tv_nh3_current_time;
//    @Bind(R.id.voc_current_time)TextView tv_voc_current_time;
//    @Bind(R.id.ch4_current_time)TextView tv_ch4_current_time;
//    @Bind(R.id.cl2_current_time)TextView tv_cl2_current_time;

    public RotateAnimation animation, animation1;
    private ImageView imgOpen;
    private ImageView camera_centerpoints;
    private MoudleAdapter moudleAdapter;

    private List<Moudle> list = new ArrayList<>();
    //    private List<Gases> gasesList = new ArrayList<>();
    public MavlinkDjiBean mavlinkDjiBean = null;
    private TextView tishi;
    private Switch cast_one, cast_two, cast_three, switch_seachlight, switch_threed;
    private Button switch_threed_camera, switch_power_threed;
    private String str1, mStatus1;
    private int mGasCL2, mGasSO2, mGasVOC, mGasCH4, mGasCO, mGasH2S, mGasNH3, mGasCO2, tmpMax, tmpMin;
    private int mGetBarrierDistance, mBarrierFront, mBarrierBack, mBarrierLeft, mBarrierRight, mGetStatus;
    private float mGetSpeed;
    private boolean isJettisonin = false;
    public int hg = 1500;
    public int fy = 1500;
    public int zoomNum = 1500;
    public int tacke = 0;
    public int rec = 0;
    public int iso = 0;
    private int maxNum = 0;
    private int minNum = 0;
    private int djiCallback = -1;
    boolean boolIsGas = false;
    private KqwSpeechCompound mKqwSpeechCompound;
    @Bind(R.id.gaslistview_layout)
    ScrollView gaslistview_layout;
    //private LineChartGasesAdapter gasesAdapter;
    //建立八个hashMap  和一个list
    //private Map<String,List<GasTime>> mapgas;
    private List<GasTime> listco, listh2s, listNh3, listco2, listcl2, listso2, listvoc, listch4;
    private AvoidTemDialog avoidTemDialog = null;
    private ObstacleAvoidanceDialog obstacleAvoidanceDialog = null;
    String time;
    Byte num3;


    //截屏参数
    MediaProjection mediaProjection;
    static MediaProjectionManager projectionManager;
    VirtualDisplay virtualDisplay;
    int mResultCode;
    Intent mData;
    ImageReader imageReader;
    int width;
    static int height;
    static int dpi;
    String imageName;
    Bitmap bitmap;
    @Bind(R.id.image)
    ImageView imageView;
    static DisplayMetrics metric;
    private static final int SCREEN_SHOT = 0;

    //录像
    private static final int RECORDER_CODE = 1;
    MediaCodec mediaCodec;
    private MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
    MediaMuxer mediaMuxer;
    Surface surface;
    public AtomicBoolean mQuit = new AtomicBoolean(false);
    private int videoTrackIndex = -1;
    private boolean muxerStarted = false;
    String videopath;
    public boolean isvideo = true;


    HttpUtil mHttpUtil;
    String ANDROID_ID;

    @Override
    protected void init(Bundle savedInstanceState) {
        handler = new Handler();
        //handler.post(progress);
        new HandleMavlinkData().setContext(getContext(), tishi);
        mHttpUtil = HttpUtil.getInstance(getContext());
        ANDROID_ID = Settings.System.getString(getContext().getContentResolver(), Settings.System.ANDROID_ID);

        //初始化毒气模块
        gaslistview_layout.setVisibility(VISIBLE);
        ChartUtil.initChart(linechart_co);
        ChartUtil.initChart(linechart_co2);
        ChartUtil.initChart(linechart_so2);
        ChartUtil.initChart(linechart_h2s);
        ChartUtil.initChart(linechart_nh3);
        ChartUtil.initChart(linechart_voc);
        ChartUtil.initChart(linechart_ch4);
        ChartUtil.initChart(linechart_cl2);

//        mapgas = new HashMap<>();
        listco = new ArrayList<GasTime>();
        listh2s = new ArrayList<GasTime>();
        listNh3 = new ArrayList<GasTime>();
        listco2 = new ArrayList<GasTime>();
        listcl2 = new ArrayList<GasTime>();
        listso2 = new ArrayList<GasTime>();
        listvoc = new ArrayList<GasTime>();
        listch4 = new ArrayList<GasTime>();

        seekbar_mount.setProgress(50);
        Constanst.redVisible = true;
        //获取基本的数据
        String[] moudleName = mActivity.getResources().getStringArray(R.array.moudle_item);
        TypedArray moudleImage = mActivity.getResources().obtainTypedArray(R.array.moudle_img);
        joystick_mount.setOnJostickMovedListener(lJoystick);
        joystick_mount.setVisibility(GONE);
        // 初始化语音合成对象|
        mKqwSpeechCompound = new KqwSpeechCompound(mActivity);
        int i = 0;
        for (String name : moudleName) {
            Moudle moudle = new Moudle();
            moudle.setName(name);
            moudle.setDrawableID(moudleImage.getResourceId(i, 0));  //moudleImage.getDrawable(i)
            i++;
            moudle.setStatus(false);
            list.add(moudle);
        }

        if (ModuleVerificationUtil.isFlightControllerAvailable()) {
            if (JackApplication.getAircraftInstance().getFlightController().isOnboardSDKDeviceAvailable()) {
                JackApplication.getAircraftInstance().getFlightController().setOnboardSDKDeviceDataCallback(new FlightController.OnboardSDKDeviceDataCallback() {
                    @Override
                    public void onReceive(final byte[] bytes) {
                        RxjavaUtil.executeRxTask(new CommonRxTask<Object>() {
                            @Override
                            public void doInIOThread() {
                                time = CalendarUtils.getDateTie();
                                mavlinkDjiBean = null;
                                mavlinkDjiBean = ByteUtils.toObject(bytes, MavlinkDjiBean.class);
                                if (Byte.toString(mavlinkDjiBean.getUavIDFour()).equals("102")) {
                                    //避障数据
                                    mBarrierFront = ((mavlinkDjiBean.getStatus2() & 0xFF) << 8) + (mavlinkDjiBean.getTemperaturE1Two() & 0xFF);
                                    mBarrierBack = ((mavlinkDjiBean.getTemperaturE2_LowTwo() & 0xFF) << 8) + (mavlinkDjiBean.getTemperaturE2_LowOne() & 0xFF);
                                    mBarrierLeft = ((mavlinkDjiBean.getTemperaturE2_HighTwo() & 0xFF) << 8) + (mavlinkDjiBean.getTemperaturE2_HighOne() & 0xFF);
                                    mBarrierRight = ((mavlinkDjiBean.getTemperaturE2Two() & 0xFF) << 8) + (mavlinkDjiBean.getTemperaturE2One() & 0xFF);

                                    //获取避障使能状态
                                    mGetStatus = mavlinkDjiBean.getGas_Status() & 0xFF;
                                    //获取避障生效距离 两位
                                    mGetBarrierDistance = ((mavlinkDjiBean.getGas_Value1_One() & 0xFF) << 8) + (mavlinkDjiBean.getGas_Num1() & 0xFF);
                                    //避障速度
                                    mGetSpeed = (((mavlinkDjiBean.getGas_Num2() & 0xFF) << 8) + (mavlinkDjiBean.getGas_Value1_Two() & 0xFF));
                                    mGetSpeed = mGetSpeed / 10;

                                    Byte num5 = mavlinkDjiBean.getGas_Num3();

                                    if (boolIsGas && num5 != 0) {
                                        //if (mapgas.size() >=4) {  //gasesList.size() == 8
                                        mGasCL2 = (((mavlinkDjiBean.getGas_Value3_Two() & 0xFF) << 8) + (mavlinkDjiBean.getGas_Value3_One() & 0xFF)) / 10;
                                        mGasSO2 = (((mavlinkDjiBean.getGas_Value4_Two() & 0xFF) << 8) + (mavlinkDjiBean.getGas_Value4_One() & 0xFF)) / 10;
                                        mGasVOC = (((mavlinkDjiBean.getDevice_Status_Three() & 0xFF) << 8) + (mavlinkDjiBean.getDevice_Status_Two() & 0xFF)) / 10;
                                        mGasCH4 = (((mavlinkDjiBean.getSet_feedback_Three() & 0xFF) << 8) + (mavlinkDjiBean.getSet_feedback_Two() & 0xFF)) * 100;

                                        if (listvoc.size() >= 30) {
                                            listvoc.clear();
                                            listso2.clear();
                                            listch4.clear();
                                            listcl2.clear();
                                        }

                                        listso2.add(new GasTime(time, mGasSO2));
                                        listvoc.add(new GasTime(time, mGasVOC));
                                        listch4.add(new GasTime(time, mGasCH4));
                                        listcl2.add(new GasTime(time, mGasCL2));
                                    }
                                    //}
                                } else {
                                    //模块是否在线
                                    HexOneMode(Utils.toBinary(mavlinkDjiBean.getDevice_Status_Three())
                                            + Utils.toBinary(mavlinkDjiBean.getDevice_Status_Two())
                                            + Utils.toBinary(mavlinkDjiBean.getDevice_Status_One()));

                                    //获取抛投的反馈事件
                                    str1 = Utils.toBinary(mavlinkDjiBean.getSet_feedback_One());
                                    //获取温度值的反馈事件
                                    mStatus1 = Utils.toBinary(mavlinkDjiBean.getStatus1());
                                    //温度 TEMPERATURE1
                                    //getBinding().setStrTempTwo(((mavlinkDjiBean.getTemperaturE1Two() << 8) + (mavlinkDjiBean.getTemperaturE1One())) / 10f + "");

                                    getBinding().setStrTempTwo("" + (short) ((mavlinkDjiBean.getTemperaturE1Two() << 8) | (mavlinkDjiBean.getTemperaturE1One() & 0xff)) / 10f);

                                    //initData(mavlinkDjiBean.getTemperaturE1Two()+"   "+mavlinkDjiBean.getTemperaturE1One());

                                    //温度上限
                                    tmpMax = (((mavlinkDjiBean.getTemperaturE2_HighTwo() & 0xFF) << 8) + (mavlinkDjiBean.getTemperaturE2_HighOne() & 0xFF)) / 10;
                                    //温度下限
                                    tmpMin = ((int) (((mavlinkDjiBean.getTemperaturE2_LowTwo() << 8) | (mavlinkDjiBean.getTemperaturE2_LowOne())))) / 10;

                                    //毒气
                                    //hexListViewGases(Utils.toBinary(mavlinkDjiBean.getGas_Status()));
                                    //if (mapgas.size() <4) {  //gasesList.size() >= 4
                                    //Byte num1 = mavlinkDjiBean.getGas_Num1();
                                    //Byte num2 = mavlinkDjiBean.getGas_Num2();
                                    num3 = mavlinkDjiBean.getGas_Num3();
                                    final Byte num4 = mavlinkDjiBean.getGas_Num4();

                                    if (boolIsGas) {
                                        mGasCO = ((mavlinkDjiBean.getGas_Value1_Two() & 0xFF) << 8) + (mavlinkDjiBean.getGas_Value1_One() & 0xFF);
                                        mGasH2S = ((mavlinkDjiBean.getGas_Value2_Two() & 0xFF) << 8) + (mavlinkDjiBean.getGas_Value2_One() & 0xFF) / 100;
                                        mGasNH3 = ((mavlinkDjiBean.getGas_Value3_Two() & 0xFF) << 8) + (mavlinkDjiBean.getGas_Value3_One() & 0xFF);
                                        mGasCO2 = ((mavlinkDjiBean.getGas_Value4_Two() & 0xFF) << 8) + (mavlinkDjiBean.getGas_Value4_One() & 0xFF);
                                        if (listco.size() >= 30) {
                                            listco.clear();
                                            listh2s.clear();
                                            listNh3.clear();
                                            listco2.clear();
                                            listch4.clear();
                                        }

                                        listco.add(new GasTime(time, mGasCO));
                                        listh2s.add(new GasTime(time, mGasH2S));
                                        if (num3 == 3) {
                                            listNh3.add(new GasTime(time, mGasNH3));
                                        } else if (num3 == 4) {
                                            mGasCO2 = ((mavlinkDjiBean.getGas_Value3_Two() & 0xFF) << 8) + (mavlinkDjiBean.getGas_Value3_One() & 0xFF);
                                            listco2.add(new GasTime(time, mGasCO2));
                                        }
                                        //gasesList.get(2).setGasNum(mGasNH3);   //NH3   CO2
                                        if (num4 == 4) {
                                            listco2.add(new GasTime(time, mGasCO2));
                                        } else if (num4 == 8) {
                                            mGasCH4 = (((mavlinkDjiBean.getGas_Value4_Two() & 0xFF) << 8) + (mavlinkDjiBean.getGas_Value4_One() & 0xFF)) * 100;
                                            listch4.add(new GasTime(time, mGasCH4));
                                        }
                                    }
                                }
                            }

                            @Override
                            public void doInUIThread() {
                                moudleAdapter.setData(list);
                                moudleAdapter.notifyDataSetChanged();
                                if (boolIsGas) {
                                    ChartUtil.setData(linechart_so2, listso2);
                                    ChartUtil.setData(linechart_voc, listvoc);
                                    ChartUtil.setData(linechart_ch4, listch4);
                                    ChartUtil.setData(linechart_cl2, listcl2);

                                    ChartUtil.setData(linechart_co, listco);
                                    ChartUtil.setData(linechart_h2s, listh2s);
                                    ChartUtil.setData(linechart_nh3, listNh3);
                                    ChartUtil.setData(linechart_co2, listco2);

                                    tv_voc_current_value.setText(mGasVOC + "");
                                    tv_so2_current_value.setText(mGasSO2 + "");
                                    tv_ch4_current_value.setText(mGasCH4 + "");
                                    tv_cl2_current_value.setText(mGasCL2 + "");

                                    tv_co_current_value.setText(mGasCO + "");
                                    tv_h2s_current_value.setText(mGasH2S + "");
                                    if (num3 == 3) tv_nh3_current_value.setText(mGasNH3 + "");
                                    tv_co2_current_value.setText(mGasCO2 + "");

                                    gas_time.setText(time);

                                    gaslistview_layout.setVisibility(VISIBLE);
                                } else {
                                    gaslistview_layout.setVisibility(GONE);
                                    listso2.clear();
                                    listvoc.clear();
                                    listch4.clear();
                                    listcl2.clear();
                                    listco.clear();
                                    listh2s.clear();
                                    listNh3.clear();
                                    listco2.clear();
                                }

                                if (bool) {
                                    exelBean e = new exelBean();
                                    e.setLat(droneloLat);
                                    e.setLog(droneloLng);
                                    e.setAlt(droneAlt);
                                    e.setGasValueOne(mGasCO);
                                    e.setGasValueTwo(mGasH2S);
                                    e.setGasValueThree(mGasNH3);
                                    e.setGasValueFour(mGasCO2);
                                    e.setGasValueFive(mGasCL2);
                                    e.setGasValueSix(mGasSO2);
                                    e.setGasValueSeven(mGasVOC);
                                    e.setGasValueEight(mGasCH4);
                                    e.setTime(CalendarUtils.getTimeDate());
                                    e.setTime2(CalendarUtils.getTimeDate2());
                                    e.save();
                                }
                                if (djiCallback == 0) {
                                    if (ptbool) {
                                        tishi.setText("111");
                                    } else {
                                        tishi.setText("222");
                                    }
                                    if (ptbool) refreshJettisoninStaste(str1);
                                    if (zsbool) refreshIrradiation(str1);
                                    if (swbool) refreshThreeCamera(str1);
                                }
                                //气体报警
                                refreshgas();
                                refreshTemperature();
                                refreshBarrier();
                            }
                        });
                    }
                });
            }
        }
        setImgRotateAnimation();
        //毒气显示值
        //gasesAdapter = new GasesAdapter(mActivity, gasesList, R.layout.item_seekbar);
//        gasesAdapter = new LineChartGasesAdapter(mActivity,mapgas,getAbsActivity());
//        horiz_moudle_seekbar.setAdapter(gasesAdapter);
        horiz_moudle_seekbar.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //点击进入gridView
        moudleAdapter = new MoudleAdapter(mActivity, list, R.layout.item_moudle);
        grid_view.setAdapter(moudleAdapter);
        grid_view.setSelector(new ColorDrawable(Color.TRANSPARENT));
        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Moudle moudle = list.get(position);
                moudle.setStatus(true);
                View childView;
                if (moudle.getName().equals("避温") && moudle.getStatus()) {
                    if (avoidTemDialog == null)
                        avoidTemDialog = new AvoidTemDialog(mActivity, mavlinkDjiBean);
                    avoidTemDialog.setMessage(tmpMax, tmpMin);
                    avoidTemDialog.show();
                }
                if (moudle.getName().equals("探照灯") && moudle.getStatus()) {
                    childView = getView(R.layout.moudle_searchlight);
                    grid_view.expandGridViewAtView(view, childView, 2, 0);
                    switch_seachlight = (Switch) childView.findViewById(R.id.switch_seachlight);
                    switch_seachlight.setOnCheckedChangeListener(MountFragment.this);
                }
                if (moudle.getName().equals("抛投") && moudle.getStatus()) {
                    isJettisonin = true;
                    childView = getView(R.layout.moudle_jettisonin);
                    grid_view.expandGridViewAtView(view, childView, 2, 0);
                    cast_one = (Switch) childView.findViewById(R.id.cast_one);
                    cast_two = (Switch) childView.findViewById(R.id.cast_two);
                    cast_three = (Switch) childView.findViewById(R.id.cast_three);
                    cast_one.setOnCheckedChangeListener(MountFragment.this);
                    cast_two.setOnCheckedChangeListener(MountFragment.this);
                    cast_three.setOnCheckedChangeListener(MountFragment.this);
                    //setCameraViewGone();
                    camera_centerpoints.setVisibility(grid_view.getVisibility() == VISIBLE ? VISIBLE : GONE);
                }
                if (moudle.getName().equals("避障") && moudle.getStatus()) {
                    if (obstacleAvoidanceDialog == null)
                        obstacleAvoidanceDialog = new ObstacleAvoidanceDialog(mActivity, mavlinkDjiBean);
                    obstacleAvoidanceDialog.setMessage(mGetStatus, mGetSpeed, mGetBarrierDistance);
                    obstacleAvoidanceDialog.show();
                }
                if (moudle.getName().equals("有毒气体") && moudle.getStatus()) {
                    Toast.makeText(getContext(), "有毒气体", Toast.LENGTH_SHORT).show();
                    GasDialog gasDialog = new GasDialog(mActivity);
                    gasDialog.show();
                }
                if (moudle.getName().equals("相机") && moudle.getStatus()) {
                    relative_joystick.setVisibility(VISIBLE);
                    grid_view.setVisibility(GONE);
                    mount_cut.setVisibility(GONE);
                }

                if (moudle.getName().equals("三维成像") && moudle.getStatus()) {
                    childView = getView(R.layout.moudle_threed);
                    grid_view.expandGridViewAtView(view, childView, 2, 0);
                    switch_threed = (Switch) childView.findViewById(R.id.switch_threed);
                    switch_threed_camera = (Button) childView.findViewById(R.id.switch_threed_camera);
                    switch_power_threed = (Button) childView.findViewById(R.id.switch_power_threed);
                    switch_threed.setOnCheckedChangeListener(MountFragment.this);
                    switch_threed_camera.setOnClickListener(MountFragment.this);
                    switch_power_threed.setOnClickListener(MountFragment.this);
                }
                if (moudle.getName().equals("双光红外") && moudle.getStatus()) {
                    relative_joystick_dualsg.setVisibility(VISIBLE);
                    grid_view.setVisibility(GONE);
                    mount_cut.setVisibility(GONE);
                }
                if (grid_view.getImageViewUp() != null) {
                    grid_view.getImageViewUp().setOnTouchListener(MountFragment.this);
                    grid_view.getImageViewDown().setOnTouchListener(MountFragment.this);
                }
            }
        });

        //关闭单光
        img_joystick_mount_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relative_joystick.setVisibility(GONE);
                grid_view.setVisibility(VISIBLE);
                mount_cut.setVisibility(VISIBLE);
            }
        });

        //关闭双光界面
        img_joystick_sg_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_joystick_dualsg.setVisibility(GONE);
                grid_view.setVisibility(VISIBLE);
                mount_cut.setVisibility(VISIBLE);
            }
        });

        //相机变焦
        seekbar_mount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
                final int numerSeekbar = seekBar.getProgress();
                if (numerSeekbar > 50) {  // 放大
                    maxNum++;
                } else if (numerSeekbar < 50) { // 缩小
                    minNum++;
                }
                HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "camera", Constanst.redVisible, "one", false, "" + 0,
                        "" + cloud, false, "" + maxNum, "" + minNum, hg, fy, tacke, rec, null, iso, calibration, fog, 1, 0);
                /*mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tishi.setText(numerSeekbar+"");
                    }
                });*/
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int numerSeekbar = seekBar.getProgress();
                if (numerSeekbar > 50 || numerSeekbar < 50) seekbar_mount.setProgress(50);
                HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "camera", Constanst.redVisible, "one", false, "" + 0,
                        "" + cloud, false, "" + maxNum, "" + minNum, hg, fy, tacke, rec, null, iso, calibration, 0, 1, 0);
            }
        });

        //切换拍照 录像
        switch_mount_photo_video.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Constanst.boolVideo) {
                    switch_mount_photo_video.setChecked(Constanst.boolVideo);
                    return;
                }
                img_camera_video_mount.setBackgroundResource(isChecked ? R.drawable.camera_controll_recordvideo_nor : R.drawable.btn_cam_capture_released);
                Constanst.boolCamera = isChecked;
            }
        });

        // 单光 拍照 录像
        img_camera_video_mount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCameraDan();
            }
        });

        //双关 拍照 录像
        img_camera_video_dualsg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

//                if(Constanst.redVisible){//红外模式
//                    Log.i("rongjiajie","红外模式");
//                    HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "many_light", Constanst.redVisible, ""+zoomNum, false, ""+0, ""+cloud,
//                            false, "", ""+diurnal_luminous_flag, hg, fy, tacke, rec++, new HandleMavlinkData.setDroneCallBack() {
//                                @Override
//                                public void backBoolean(DJIError djiError) {
//                                    Constanst.redVideo = djiError == null?true:false;  //判定红外是否录像中
//                                    mActivity.runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            img_camera_video_dualsg.setBackgroundResource(Constanst.redVideo?
//                                                    R.drawable.camera_controll_stopvideo_nor:R.drawable.camera_controll_recordvideo_nor);
//                                        }
//                                    });
//                                }
//                            }, iso,calibration,fog,1,0);
//                }else{  //可见光模式
                if (Constanst.visibleVideo) {  //可见光处于录像模式
//                        HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "many_light", Constanst.redVisible, ""+zoomNum, false, ""+0, ""+cloud,
//                                false, "", ""+diurnal_luminous_flag, hg, fy, tacke, rec++, new HandleMavlinkData.setDroneCallBack() {
//                                    @Override
//                                    public void backBoolean(DJIError djiError) {
//                                        Constanst.video = djiError == null?true:false;  //判定红外是否录像中
//                                        mActivity.runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                img_camera_video_dualsg.setBackgroundResource(Constanst.video?
//                                                        R.drawable.camera_controll_stopvideo_nor:R.drawable.camera_controll_recordvideo_nor);
//                                            }
//                                        });
//
//                                    }
//                                }, iso,calibration,fog,1,0);

                    if (isvideo) {
                        takeVideo();
                        isvideo = false;
                    } else {
                        mQuit.set(true);
                        Toast.makeText(getContext(), "Recorder stop", Toast.LENGTH_SHORT).show();
                        isvideo = true;
                    }


                } else {//可见光处于拍照模式
//                        HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "many_light", Constanst.redVisible, ""+zoomNum, false, ""+0, ""+cloud,
//                                false, "", ""+diurnal_luminous_flag, hg, fy, tacke++, rec, null, iso,calibration,fog,1,0);


                    takePhoto();
                }
                //}
            }
        });

        //双光(拍照 录像)
        switch_txt_camera_video.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean isChecked) {
                Constanst.visibleVideo = isChecked; //true 录像模式
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img_camera_video_dualsg.setBackgroundResource(isChecked ? R.drawable.camera_controll_recordvideo_nor : R.drawable.btn_cam_capture_released);
                    }
                });
            }
        });

        //双光(可见光 红外)
        switch_txt_qh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean isChecked) {
                Constanst.redVisible = isChecked;  //true 红外
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt_dualsg_photo.setVisibility(isChecked ? GONE : VISIBLE);
                        txt_dualsg_video.setVisibility(isChecked ? GONE : VISIBLE);
                        switch_txt_camera_video.setVisibility(isChecked ? GONE : VISIBLE);
                        img_camera_video_dualsg.setBackgroundResource(isChecked ? R.drawable.camera_controll_recordvideo_nor : R.drawable.btn_cam_capture_released);
                        if (!isChecked) {
                            img_camera_video_dualsg.setBackgroundResource(Constanst.visibleVideo ? R.drawable.camera_controll_recordvideo_nor : R.drawable.btn_cam_capture_released);
                        }
                        button_color_camera_red.setVisibility(isChecked ? VISIBLE : GONE);
                    }
                });
            }
        });


        if (spinner_ch5 != null) spinner_ch5.setItems("无增益", "高增益", "黑白夜视");
        if (spinner_ch9 != null) spinner_ch9.setItems("透雾关", "透雾开");
        if (spinner_ch10 != null) spinner_ch10.setItems("电子稳像关", "电子稳像开");

        spinner_ch5.isPressed();

        spinner_ch9.isPressed();
        spinner_ch10.isPressed();

        spinner_ch5.setOnItemSelectedListener(this);
        btn_ch7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//修改第7个参数
                calibration++;
                startHandmavlinkDate("many_light");
            }
        });


        diurnal_luminous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diurnal_luminous_flag++;
                startHandmavlinkDate("many_light");
            }
        });


        spinner_ch9.setOnItemSelectedListener(this);
        spinner_ch10.setOnItemSelectedListener(this);


        seek_zoom.setProgress(500);
        tv_seek.setText("光焦1500");
        //设置变焦
        seek_zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //进度条结束后调用
            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

                final int number = seekBar.getProgress() + seekbar;
                tv_seek.setText("光焦" + number);

                zoomNum = number;
                tishi.setText(zoomNum + "");
                startHandmavlinkDate("many_light");
            }
        });


        stop_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_seek.setText("光焦" + 1500);

                seek_zoom.setProgress(500);
                zoomNum = 1500;
                startHandmavlinkDate("many_light");

//                HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "", Constanst.redVisible, "" + zoomNum, false, "" + 0, "" + cloud,
//                        false, "", "" + diurnal_luminous_flag, hg, fy, tacke, rec, new HandleMavlinkData.setDroneCallBack() {
//                            @Override
//                            public void backBoolean(final DJIError djiError) {
//                                mActivity.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if(djiError!=null){
//
//                                        }else{
//
//                                        }
//                                    }
//                                });
//
//                            }
//                        }, iso, calibration, fog, electronic, 0);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void takeVideo() {
        File f = null;
        File dataDir = null;
        String path = SharePreUtil.getString("videopath", mActivity, "videopath");
        if (path.equals("")) {
            dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
            f = new File(dataDir.getPath() + "/" + getContext().getPackageName() + "/video");
        } else {
            f = new File(path);
        }
        if (!f.exists()) {
            f.mkdir();
        }
        File videofile = new File(f.getPath() + "/" + System.currentTimeMillis() + ".mp4");
        videopath = videofile.getAbsolutePath();

        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = 720;
        height = 1280;
        dpi = 1;
        projectionManager = (MediaProjectionManager) getActivity().getSystemService(MEDIA_PROJECTION_SERVICE);
        startActivityForResult(projectionManager.createScreenCaptureIntent(), RECORDER_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void takePhoto() {
        metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;
        height = metric.heightPixels;
        dpi = metric.densityDpi;
        projectionManager = (MediaProjectionManager) getContext().getSystemService(MEDIA_PROJECTION_SERVICE);

        //向系统提交截屏的请求
        startActivityForResult(projectionManager.createScreenCaptureIntent(),
                SCREEN_SHOT);
    }

    public void startHandmavlinkDate(String type) {
        HandleMavlinkData.setMavlinkData(mavlinkDjiBean, type, Constanst.redVisible, "" + zoomNum, false, "" + 0, "" + cloud,
                false, "", "" + diurnal_luminous_flag, hg, fy, tacke, rec, null, iso, calibration, fog, electronic, 0);

    }


    private int seekbar = 1000;

    public void setCameraDan() {
        if (!Constanst.boolCamera) {
            tacke += 1;
            //HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "camera", false, "one", false, "", "",false,"","",hg,fy,tacke,rec);
        }
        if (Constanst.boolCamera) {
            if (Constanst.boolVideo) {
                //HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "camera", false, "one", false, "", "",false,"","",hg,fy,tacke,rec);
                Constanst.boolVideo = false; //停止录像
            } else {
                rec += 1;
                //HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "camera", false, "one", false, "", "",false,"","",hg,fy,tacke,rec);
                Constanst.boolVideo = true; //正在录像
            }
            //HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "camera", false, "one", false, "", "",false,"","",hg,fy,tacke,rec);
        }
        HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "camera", false, "one", false, "" + 0, "" + cloud,
                false, "" + 0, "" + diurnal_luminous_flag, hg, fy, tacke, rec, new HandleMavlinkData.setDroneCallBack() {
                    @Override
                    public void backBoolean(DJIError djiError) {
                        if (Constanst.boolCamera) { //录像模式
                            if (Constanst.boolVideo) {
                                Constanst.boolVideo = djiError == null ? true : false;
                            }
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img_camera_video_mount.setBackgroundResource(Constanst.boolVideo ?
                                            R.drawable.camera_controll_stopvideo_nor : R.drawable.camera_controll_recordvideo_nor);
                                }
                            });
                        } else {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    T.show(mActivity, "成功", 1);
                                }
                            });
                        }
                    }
                }, iso, calibration, fog, 1, 0);
    }

    //单光，双光控制
    JoystickView.JoystickMovedListener lJoystick = new JoystickView.JoystickMovedListener() {
        @Override
        public void OnMoved(int pan, int tilt) {
            if ((pan < 120) && (pan > 80))
                pan = 100;
            if ((tilt < 120) && (tilt > 80))
                tilt = 100;
            hg = pan * 5 + 1000;
            fy = tilt * 5 + 1000;
            /*mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tishi.setText(hg+"/"+fy);
                }
            });*/

            RxjavaUtil.doInIOThread(new IOTask<Object>() {
                @Override
                public void doInIOThread() throws Exception {
                    HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "camera", Constanst.redVisible, "one", false,
                            "" + 0, "" + cloud, false, "", "" + diurnal_luminous_flag, hg, fy, tacke, rec, null, iso, calibration, fog, 1, 0);
                }
            });
        }
    };

    public void setCameraViewGone() {  //1  0
        camera_centerpoints.setVisibility(camera_centerpoints.getVisibility() == GONE ? VISIBLE : GONE);
    }

    //避障反馈值
    private void refreshBarrier() {
        getBinding().setStrFount("前:" + mBarrierFront + "cm");
        getBinding().setStrBack("后:" + mBarrierBack + "cm");
        getBinding().setStrLeft("左:" + mBarrierLeft + "cm");
        getBinding().setStrRight("右:" + mBarrierRight + "cm");

        txt_left.setTextColor(mBarrierLeft <= mGetBarrierDistance ? Color.RED : Color.WHITE);
        txt_right.setTextColor(mBarrierRight <= mGetBarrierDistance ? Color.RED : Color.WHITE);
        txt_front.setTextColor(mBarrierFront <= mGetBarrierDistance ? Color.RED : Color.WHITE);
        txt_back.setTextColor(mBarrierBack <= mGetBarrierDistance ? Color.RED : Color.WHITE);
        StringBuilder stringBuilder = new StringBuilder();
        if (mBarrierLeft <= mGetBarrierDistance) {
            stringBuilder.append("左面");
        } else if (mBarrierRight <= mGetBarrierDistance) {
            stringBuilder.append("右面");
        } else if (mBarrierFront <= mGetBarrierDistance) {
            stringBuilder.append("前面");
        } else if (mBarrierBack <= mGetBarrierDistance) {
            stringBuilder.append("后面");
        }
        if (!stringBuilder.toString().isEmpty() && !stringBuilder.toString().equals("")) {
            setMessageHandlerLoop("小心" + stringBuilder.toString() + "的障碍物");
        }
    }

    //语音消息队列
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String strMsg = (String) msg.obj;
            mKqwSpeechCompound.speaking(strMsg);
        }
    };

    private void setMessageHandlerLoop(String name) {
        if (SharePreUtil.getString("boolSpeek", mActivity, "boolSpeek").equals("true")) {
            Message msg = Message.obtain();
            msg.obj = name;
            mHandler.sendMessage(msg);
        }
    }

    //温度反馈值
    private void refreshTemperature() {
        if ("11111101".equals(mStatus1)) {//FD
            setTestValuesClo("异常", Color.RED);
            setMessageHandlerLoop("温度值异常");
        } else if ("11111110".equals(mStatus1)) { //正常
            altitudeValue.setTextColor(Color.WHITE);
        } else if ("11111100".equals(mStatus1)) { //超过上限限度
            altitudeValue.setTextColor(Color.YELLOW);
            setMessageHandlerLoop("温度超过上限值");
        } else if ("11111011".equals(mStatus1)) { //超过下限限度
            altitudeValue.setTextColor(Color.YELLOW);
            setMessageHandlerLoop("温度超过下限值");
        }
    }

    private void setTestValuesClo(String name, int clo) {
        altitudeValue.setText(name);
        altitudeValue.setTextColor(clo);
    }

    //抛投
    private void refreshJettisoninStaste(String strs) {
        if (cast_one != null) {
            cast_one.setChecked(strs.substring(7, 8).equals("1") ? true : false);
            cast_two.setChecked(strs.substring(6, 7).equals("1") ? true : false);
            cast_three.setChecked(strs.substring(5, 6).equals("1") ? true : false);
        }
    }

    //照射模块
    private void refreshIrradiation(String str1) {
        if (switch_seachlight != null)
            switch_seachlight.setChecked(str1.substring(4, 5).equals("1") ? true : false);
    }

    //三维模块
    private void refreshThreeCamera(String str1) {
        if (switch_threed != null)
            switch_threed.setChecked(str1.substring(2, 3).equals("1") ? true : false);
    }

    //获取一个View
    private View getView(int resoureId) {
        return LayoutInflater.from(mActivity).inflate(resoureId, null);
    }

    public MavlinkDjiBean getMavlinkDjiBean() {
        return mavlinkDjiBean;
    }

    @OnClick(R.id.mount_clear)
    public void onClickMountClear(View v) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (frame_mount.getVisibility() == VISIBLE) {
                    mount_clear.startAnimation(animation);
                    frame_mount.startAnimation(startAnimViewGone());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            frame_mount.setVisibility(GONE);
                            if (imgOpen != null)
                                imgOpen.setVisibility(VISIBLE);
                        }
                    }, 1000);
                } else {
                    mount_clear.startAnimation(animation1);
                    frame_mount.startAnimation(startAnimViewShow());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            frame_mount.setVisibility(VISIBLE);
                            if (imgOpen != null)
                                imgOpen.setVisibility(GONE);
                        }
                    }, 1000);
                }
            }
        });
    }

    @OnClick(R.id.mount_cut)
    public void onClickMoubtOut(View v) {//
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bar_char_linear.setVisibility(bar_char_linear.getVisibility() == View.INVISIBLE ? VISIBLE : View.INVISIBLE);
                grid_view.setVisibility(bar_char_linear.getVisibility() == View.INVISIBLE ? VISIBLE : View.INVISIBLE);
            }
        });
        /*int w = 0,h = 0;
        if(bar_char_linear.getVisibility() == View.INVISIBLE){w = 750;h=550;}else{w = 700;h = 500;}
        FrameLayout.LayoutParams lay = new FrameLayout.LayoutParams(Utils.dp2px(getContext(), w), Utils.dp2px(getContext(), h));
        frame_mount.setLayoutParams(lay);*/
    }

    private void setImgRotateAnimation() {
        animation = new RotateAnimation(0, 180, 50f, 50f);
        animation.setDuration(1000);//设置动画持续时间
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态

        animation1 = new RotateAnimation(180, 0, 50f, 50f);
        animation1.setDuration(1000);//设置动画持续时间
        animation1.setFillAfter(true);//动画执行完后是否停留在执行完的状态
    }

    //左上部显示动画
    public TranslateAnimation startAnimViewShow() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(1000);
        return mHiddenAction;
    }

    //左上部隐藏动画
    public TranslateAnimation startAnimViewGone() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(1000);
        return mHiddenAction;
    }

    @Override
    protected void onDelayLoad() {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mount;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
    }

    public void setImgOpen(ImageView imgOpen, TextView txt_tishi, ImageView imageView) {
        this.imgOpen = imgOpen;
        this.tishi = txt_tishi;
        this.camera_centerpoints = imageView;
        new HandleMavlinkData().setContext(getContext(), tishi);
    }

    //设置气体状态
//    private void hexListViewGases(String gasess) {
//        gasesList.clear();
//        for (int i = 0; i < gasess.length(); i++) {
//            final String gasesMoudle = gasess.substring((8 - (i + 1)), (8 - i));
//            switch (8 - i) {
//                case 8:
//                    if (gasesMoudle.equals("1")) {
//                        gasesList.add(new Gases("CO", 0, 100));
//                    }
//                    break;
//                case 7:
//                    if (gasesMoudle.equals("1")) {
//                        gasesList.add(new Gases("H2S", 0, 50));
//                    }
//                    break;
//                case 6:
//                    if (gasesMoudle.equals("1")) {
//                        gasesList.add(new Gases("NH3", 0, 100));
//                    }
//                    break;
//                case 5:
//                    if (gasesMoudle.equals("1")) {
//                        gasesList.add(new Gases("CO2", 0, 20000));
//                    }
//                    break;
//                case 4:
//                    if (gasesMoudle.equals("1")) {
//                        gasesList.add(new Gases("CL2", 0, 20));
//                    }
//                    break;
//                case 3:
//                    if (gasesMoudle.equals("1")) {
//                        gasesList.add(new Gases("SO2", 0, 20));
//                    }
//                    break;
//                case 2:
//                    if (gasesMoudle.equals("1")) {
//                        gasesList.add(new Gases("VOC", 0, 200));
//                    }
//                    break;
//                case 1:
//                    if (gasesMoudle.equals("1")) {
//                        gasesList.add(new Gases("CH4", 0, 50000));
//                    }
//                    break;
//            }
//        }
//    }


    @OnClick(R.id.button_color_camera_holder)
    public void maxCameraHolder(View v) {//云台模式
        cloud++;

        HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "many_light", Constanst.redVisible, "" + zoomNum, false, "" + 0, "" + cloud,
                false, "", "" + diurnal_luminous_flag, hg, fy, tacke, rec, new HandleMavlinkData.setDroneCallBack() {
                    @Override
                    public void backBoolean(DJIError djiError) {
                        if (djiError != null) {

                        } else {

                        }

                    }
                }, iso, calibration, fog, electronic, 0);
    }

    @OnClick(R.id.button_color_camera_iso)
    public void maxCameraIsoImage(View v) {//镜像翻转
        iso++;

        startHandmavlinkDate("many_light");
    }

    @OnClick(R.id.button_color_camera_red)
    public void maxCameraRed(View v) {//红外颜色

    }

    @OnClick(R.id.img_joystick_sg_close)
    public void maxCameraClose(View v) {//红外颜色

    }

    private boolean ptbool = false;
    private boolean zsbool = false;
    private boolean swbool = false;

    //设置模块状态
    private void HexOneMode(final String name) {
        String flagone = "";
        String flagtwo = "";
        String otherflag = "";
        String modeflag = "";//检测到模块的

        for (int i = 0; i < name.length(); ++i) {
            //(name.indexOf("1",i))
            int drawableId;
            boolean bool = false;
            String numMoudle = name.substring((24 - (i + 1)), (24 - i));
            switch (24 - i) {

                case 22:  //备用
                    break;
                case 21:  //相机模块
//                    tishi.setText("毒气模块有"+numMoudle);
//                    Log.i("rongjiajie","毒气模块有"+numMoudle);
                    if (numMoudle.equals("1")) {
                        drawableId = R.mipmap.pic_radar_online;
                        bool = true;
                        Constanst.cameraType = "相机";
                        otherflag = otherflag + "相机/";
                    } else {
                        drawableId = R.mipmap.pic_radar_unline;
                        bool = false;
                        Constanst.cameraType = "";
                    }
                    list.get(11).setDrawableID(drawableId);
                    list.get(11).setStatus(bool);
                    break;
                case 20:  //毒气模块
                    if (numMoudle.equals("1")) {
                        drawableId = R.mipmap.pic_gas_online;
                        boolIsGas = bool = true;

                        otherflag = otherflag + "有毒气体/";
                    } else {
                        drawableId = R.mipmap.pic_gas_unline;
                        boolIsGas = bool = false;
                    }
                    list.get(3).setDrawableID(drawableId);
                    list.get(3).setStatus(bool);
                    break;
                case 19:  //抛投模块
                    if (numMoudle.equals("1")) {
                        drawableId = R.mipmap.pic_toss_online;
                        ptbool = bool = true;


                        otherflag = otherflag + "抛投/";
                    } else {
                        drawableId = R.mipmap.pic_toss_unline;
                        ptbool = bool = false;
                        // camera_centerpoints.setVisibility(GONE);

                    }
                    list.get(2).setDrawableID(drawableId);
                    list.get(2).setStatus(bool);
                    break;
                case 18:  //备用
                    break;
                case 17:  //降落伞
                    if (numMoudle.equals("1")) {
                        drawableId = R.mipmap.pic_parachute_online;
                        bool = true;

                        otherflag = otherflag + "降落伞/";
                    } else {
                        drawableId = R.mipmap.pic_parachute_unline;
                        bool = false;

                    }
                    list.get(4).setDrawableID(drawableId);
                    list.get(4).setStatus(bool);
                    break;
                case 16:  //照射模块
                    if (numMoudle.equals("1")) {
                        drawableId = R.mipmap.pic_searchlight_online;
                        zsbool = bool = true;

                        otherflag = otherflag + "探照灯/";
                    } else {
                        drawableId = R.mipmap.pic_searchlight_unline;
                        zsbool = bool = false;

                    }
                    list.get(10).setDrawableID(drawableId);
                    list.get(10).setStatus(bool);
                    break;
                case 15:  //喊话模块
                    if (numMoudle.equals("1")) {
                        drawableId = R.mipmap.pic_horn_online;
                        bool = true;

                        otherflag = otherflag + "喊话/";
                    } else {
                        drawableId = R.mipmap.pic_horn_unline;
                        bool = false;

                    }
                    list.get(1).setDrawableID(drawableId);
                    list.get(1).setStatus(bool);
                    break;
                case 14:  //电池模块
                    if (numMoudle.equals("1")) {
                        drawableId = R.mipmap.pic_battery_onling;
                        bool = true;

                        otherflag = otherflag + "电池/";
                    } else {
                        drawableId = R.mipmap.pic_battery_unling;
                        bool = false;

                    }
                    list.get(0).setDrawableID(drawableId);
                    list.get(0).setStatus(bool);
                    break;
                case 13:  //3DModel
                    if (numMoudle.equals("1")) {
                        drawableId = R.mipmap.pic_3d_online;
                        swbool = bool = true;

                        otherflag = otherflag + "三维成像/";
                    } else {
                        drawableId = R.mipmap.pic_3d_unline;
                        swbool = bool = false;
                    }
                    list.get(8).setDrawableID(drawableId);
                    list.get(8).setStatus(bool);

                    break;
                case 12:  //双光/三光模块
                    if (numMoudle.equals("1")) {
                        drawableId = R.mipmap.pic_infrared_online;
                        bool = true;
                        Constanst.cameraType = "三光";


                        //截屏
                        DisplayMetrics metric = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
                        width = metric.widthPixels;
                        height = metric.heightPixels;
                        dpi = metric.densityDpi;
                        projectionManager = (MediaProjectionManager) getContext().getSystemService(MEDIA_PROJECTION_SERVICE);

                        otherflag = otherflag + "双光红外/";
                    } else {
                        drawableId = R.mipmap.pic_infrared_unline;
                        bool = false;
                        Constanst.cameraType = "";

                    }
                    list.get(7).setDrawableID(drawableId);
                    list.get(7).setStatus(bool);
                    break;


                case 24:  //温度模块
                    if (numMoudle.equals("1")) {
                        drawableId = R.mipmap.pic_temperature_online;
                        bool = true;
                        flagone = flagone + "避温/";
                    } else {
                        drawableId = R.mipmap.pic_temperature_unline;
                        bool = false;
                        flagone = flagone + "1/";
                    }
                    list.get(6).setDrawableID(drawableId);
                    list.get(6).setStatus(bool);
                    break;
                case 23:  //壁障模块
                    if (numMoudle.equals("1")) {
                        drawableId = R.mipmap.pic_tof_online;
                        bool = true;

                        flagtwo = flagtwo + "避障/";
                    } else {
                        drawableId = R.mipmap.pic_tof_unline;
                        bool = false;
                        flagtwo = flagtwo + "1/";
                    }
                    list.get(5).setDrawableID(drawableId);
                    list.get(5).setStatus(bool);
                    break;
            }
        }

        if (!flagone.equals("1/") && !flagtwo.equals("1/") && otherflag.length() == 0) {
            modeflag = flagone + flagtwo + "1/";
        } else if (flagone.equals("1/") && flagtwo.equals("1/") && otherflag.length() == 0) {
            modeflag = "1/1/1/";
        } else if (!flagone.equals("1/") && !flagtwo.equals("1/") && otherflag.length() > 0) {
            modeflag = flagone + flagtwo + otherflag;
        } else if (flagone.equals("1/") && flagtwo.equals("1/") && otherflag.length() > 0) {
            modeflag = otherflag + "1/1/";
        }


///v1/Moudle_Z/SetDroneMoudle/:droneid/:moudleone/:moudleTwo/:moudleThree
        final String url = Constanst.IP + "v1/Moudle_Z/SetDroneMoudle/" + ANDROID_ID + "/" + modeflag;

        if (!modeflag.equals("")) {
            modeflag = "";
            flagone = "";
            flagtwo = "";
            otherflag = "";
            mHttpUtil.post(url, null, new HttpUtil.AbsResponse() {
                @Override
                public void onResponse(final String data) {
                    super.onResponse(data);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });


                }

                @Override
                public void onError(Object error) {
                    super.onError(error);

                }
            });
        }


    }

    //抛投点击事件
    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        if (!buttonView.isPressed())
            return;
        djiCallback = -1;
        switch (buttonView.getId()) {
            case R.id.cast_one:
                HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "jettisonin", isChecked, "one", false, "" + 0, "" + cloud, false, "", "" + diurnal_luminous_flag, 0, 0, 0, 0, new HandleMavlinkData.setDroneCallBack() {
                    @Override
                    public void backBoolean(final DJIError djiError) {
                        setUpdateStates(cast_one, djiError, isChecked);
                    }
                }, iso, calibration, fog, electronic, 0);
                break;
            case R.id.cast_two:
                HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "jettisonin", isChecked, "two", false, "" + 0, "" + cloud, false, "", "" + diurnal_luminous_flag, 0, 0, 0, 0, new HandleMavlinkData.setDroneCallBack() {
                    @Override
                    public void backBoolean(final DJIError djiError) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (djiError == null) {
                                            cast_two.setChecked(isChecked);
                                            cast_one.setChecked(isChecked);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    djiCallback = 0;
                                                }
                                            }, 1500);
                                        } else {
                                            cast_one.setChecked(!isChecked);
                                            cast_two.setChecked(!isChecked);
                                        }
                                    }
                                });
                            }
                        });
                    }
                }, iso, calibration, fog, electronic, 0);
                break;
            case R.id.cast_three:
                HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "jettisonin", isChecked, "three", false, "" + 0, "" + cloud, false, "", "" + diurnal_luminous_flag, 0, 0, 0, 0, new HandleMavlinkData.setDroneCallBack() {
                    @Override
                    public void backBoolean(final DJIError djiError) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (djiError == null) {
                                            cast_two.setChecked(isChecked);
                                            cast_one.setChecked(isChecked);
                                            cast_three.setChecked(isChecked);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    djiCallback = 0;
                                                }
                                            }, 1500);
                                        } else {
                                            cast_one.setChecked(!isChecked);
                                            cast_two.setChecked(!isChecked);
                                            cast_three.setChecked(!isChecked);
                                        }
                                    }
                                });
                            }
                        });
                    }
                }, iso, calibration, fog, electronic, 0);
                break;
            case R.id.switch_seachlight:
                HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "irradiation", false, "", isChecked, "" + 0, "" + cloud, false, "", "" + diurnal_luminous_flag, 0, 0, 0, 0, new HandleMavlinkData.setDroneCallBack() {
                    @Override
                    public void backBoolean(final DJIError djiError) {
                        setUpdateStates(cast_one, djiError, isChecked);
                    }
                }, iso, calibration, fog, electronic, 0);
                break;
            case R.id.switch_threed:  //三维
                HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "three_dimensional", false, "", isChecked, "" + 0, "" + cloud, false, "", "" + diurnal_luminous_flag, 1, 0, 0, 0, new HandleMavlinkData.setDroneCallBack() {
                    @Override
                    public void backBoolean(final DJIError djiError) {
                        setUpdateStates(cast_one, djiError, isChecked);
                    }
                }, iso, calibration, fog, electronic, 0);
                break;
        }
    }

    private void setUpdateStates(final Switch cast_one, final DJIError djiError, final boolean isChecked) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (djiError == null) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    djiCallback = 0;
                                }
                            }, 1500);
                        } else {
                            cast_one.setChecked(!isChecked);
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isJettisonin) {
            isJettisonin = false;
            setCameraViewGone();
        }
        return false;
    }

    //三维拍照
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_power_threed:  //电源
                HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "three_dimensional", Constanst.redVisible, "", false, "" + 0, "" + cloud, false, "", "" + diurnal_luminous_flag, 2, 0, 0, 0, null, iso, calibration, fog, electronic, 0);
                break;

            case R.id.switch_threed_camera:  //摄像头
                HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "three_dimensional", Constanst.redVisible, "", false, "" + 0, "" + cloud, false, "", "" + diurnal_luminous_flag, 3, 0, 0, 0, null, iso, calibration, fog, electronic, 0);
                break;
        }
    }

    public int calibration = 0;//陀螺仪校准标识
    public int fog = 1;//透雾
    public int electronic = 1;//电子稳像
    public int diurnal_luminous_flag = 0;
    public int cloud = 0;

    @Override
    public void onItemSelected(JackSpinner view, int position, long id, Object item) {
        switch (view.getId()) {
            case R.id.spinner_ch5://夜视

                break;


//            case spinner_ch7://陀螺仪校准
//                if(position==0){//低档   修改第17个参数
//                    calibration=0;
//                    HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "many_light", Constanst.redVisible, "one", false, "", "",
//                            false, "", "", hg, fy, tacke, rec, null, iso,calibration,fog,electronic,1);
//                }else if(position==1){//高档
//                    calibration=1;
//                    HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "many_light", Constanst.redVisible, "one", false, "", "",
//                            false, "", "", hg, fy, tacke, rec, null, iso,calibration,fog,electronic,1);
//                }
//                break;


            case R.id.spinner_ch9://透雾
                if (position == 0) {//透雾关   修改第18个参数
                    fog = 1;
                    HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "many_light", Constanst.redVisible, "" + zoomNum, false, "" + 0, "" + cloud,
                            false, "", "" + diurnal_luminous_flag, hg, fy, tacke, rec, null, iso, calibration, fog, electronic, 0);
                } else if (position == 1) {//透雾开
                    fog = 2;
                    HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "many_light", Constanst.redVisible, "" + zoomNum, false, "" + 0, "" + cloud,
                            false, "", "" + diurnal_luminous_flag, hg, fy, tacke, rec, null, iso, calibration, fog, electronic, 0);
                }
                break;


            case R.id.spinner_ch10://电子稳像

                if (position == 0) {//电子稳像关   修改第18个参数
                    electronic = 1;
                    HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "many_light", Constanst.redVisible, "" + zoomNum, false, "" + 0, "" + cloud,
                            false, "", "" + diurnal_luminous_flag, hg, fy, tacke, rec, null, iso, calibration, fog, electronic, 0);
                } else if (position == 1) {//电子稳像开
                    electronic = 2;
                    HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "many_light", Constanst.redVisible, "" + zoomNum, false, "" + 0, "" + cloud,
                            false, "", "" + diurnal_luminous_flag, hg, fy, tacke, rec, null, iso, calibration, fog, electronic, 0);
                }
                break;
        }
    }


    /**
     * 气体语音报警
     */
    StringBuilder str = new StringBuilder();

    public void refreshgas() {


        int gasco;
        if (SharePreUtil.getString("Gams", mActivity, "StrCO").equals("")) {
            gasco = -1;
        } else {
            gasco = Integer.valueOf(SharePreUtil.getString("Gams", mActivity, "StrCO"));
        }
        if (mGasCO > gasco) {//一氧化碳 CO
            str.append("一氧化碳");
            str.append(",");
            tv_co_current_value.setTextColor(Color.RED);
        } else {
            tv_co_current_value.setTextColor(Color.BLACK);
        }


        int gash2s;
        if (SharePreUtil.getString("Gams", mActivity, "StrH2S").equals("")) {
            gash2s = -1;
        } else {
            gash2s = Integer.valueOf(SharePreUtil.getString("Gams", mActivity, "StrH2S"));
        }
        if (mGasH2S > gash2s) {//硫化氢 H2S
            str.append("硫化氢");
            str.append(",");
            tv_h2s_current_value.setTextColor(Color.RED);
        } else {
            tv_h2s_current_value.setTextColor(Color.BLACK);
        }


        int gasnh3;
        if (SharePreUtil.getString("Gams", mActivity, "StrNH3").equals("")) {
            gasnh3 = -1;
        } else {
            gasnh3 = Integer.valueOf(SharePreUtil.getString("Gams", mActivity, "StrNH3"));
        }
        if (mGasNH3 > gasnh3) {//氨气 NH3
            str.append("氨气");
            str.append(",");
            tv_nh3_current_value.setTextColor(Color.RED);
        } else {
            tv_nh3_current_value.setTextColor(Color.BLACK);
        }


        int gasco2;
        if (SharePreUtil.getString("Gams", mActivity, "StrCO2").equals("")) {
            gasco2 = -1;
        } else {
            gasco2 = Integer.valueOf(SharePreUtil.getString("Gams", mActivity, "StrCO2"));
        }
        if (mGasCO2 > gasco2) {//二氧化碳 CO2
            str.append("二氧化碳");
            str.append(",");
            tv_co2_current_value.setTextColor(Color.RED);
        } else {
            tv_co2_current_value.setTextColor(Color.BLACK);
        }

        int gascl2;
        if (SharePreUtil.getString("Gams", mActivity, "StrCL2").equals("")) {
            gascl2 = -1;
        } else {
            gascl2 = Integer.valueOf(SharePreUtil.getString("Gams", mActivity, "StrCL2"));
        }
        if (mGasCL2 > gascl2) {//氯气 CL2
            str.append("氯气");
            str.append(",");
            tv_cl2_current_value.setTextColor(Color.RED);
        } else {
            tv_cl2_current_value.setTextColor(Color.BLACK);
        }

        int gasso2;
        if (SharePreUtil.getString("Gams", mActivity, "StrSO2").equals("")) {
            gasso2 = -1;
        } else {
            gasso2 = Integer.valueOf(SharePreUtil.getString("Gams", mActivity, "StrSO2"));
        }
        if (mGasSO2 > gasso2) {//二氧化硫 so2
            str.append("二氧化硫");
            str.append(",");
            tv_so2_current_value.setTextColor(Color.RED);
        } else {
            tv_so2_current_value.setTextColor(Color.BLACK);
        }

        int gasvoc;
        if (SharePreUtil.getString("Gams", mActivity, "StrVOC").equals("")) {
            gasvoc = -1;
        } else {
            gasvoc = Integer.valueOf(SharePreUtil.getString("Gams", mActivity, "StrVOC"));
        }
        if (mGasVOC > gasvoc) {//挥发性有机化合物 voc
            str.append("挥发性有机化合物");
            str.append(",");
            tv_voc_current_value.setTextColor(Color.RED);
        } else {
            tv_voc_current_value.setTextColor(Color.BLACK);
        }


        int gasvh4;
        if (SharePreUtil.getString("Gams", mActivity, "StrCH4").equals("")) {
            gasvh4 = -1;
        } else {
            gasvh4 = Integer.valueOf(SharePreUtil.getString("Gams", mActivity, "StrCH4"));
        }
        if (mGasCH4 > gasvh4) {//甲烷 ch4
            str.append("甲烷");
            str.append(",");
            tv_ch4_current_value.setTextColor(Color.RED);
        } else {
            tv_ch4_current_value.setTextColor(Color.BLACK);
        }


        if (str.length() > 2) {
            str.append("有异常");
            setMessageHandlerLoop(str.toString());
            str.setLength(0);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCREEN_SHOT) {
            if (resultCode == RESULT_OK) {
                mResultCode = resultCode;
                mData = data;
                setUpMediaProjection();
                setUpVirtualDisplay();
                startCapture();
            }
        }


        if (requestCode == RECORDER_CODE) {
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            new Thread() {
                @Override
                public void run() {
                    try {
                        try {
                            prepareEncoder();
                            mediaMuxer = new MediaMuxer(videopath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        virtualDisplay = mediaProjection.createVirtualDisplay(TAG + "-display",
                                width, height, dpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                                surface, null, null);
                        recordVirtualDisplay();
                    } finally {
                        release();
                    }
                }
            }.start();

            Toast.makeText(getContext(), "Recorder is running...", Toast.LENGTH_SHORT).show();
            //getActivity().moveTaskToBack(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpMediaProjection() {
        mediaProjection = projectionManager.getMediaProjection(mResultCode, mData);
    }


    private void startCapture() {
        SystemClock.sleep(1000);
        imageName = System.currentTimeMillis() + ".png";
        Image image = imageReader.acquireNextImage();
        if (image == null) {
            Log.e(TAG, "image is null.");
            return;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();

        if (bitmap != null) {
            //imageView.setImageBitmap(bitmap);
            try {
                imageView.setImageBitmap(bitmap);
                saveFile(bitmap, imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpVirtualDisplay() {
        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 1);
        mediaProjection.createVirtualDisplay("ScreenShout",
                width, height, dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.getSurface(), null, null);
    }


    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String fileName) throws IOException {
        String path = SharePreUtil.getString("picpath", mActivity, "picpath");
        File f = null;
        if (path.equals("")) {
            File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
            f = new File(dataDir.getPath() + "/" + getContext().getPackageName() + "/photo");
        } else {

            f = new File(path);
        }

        if (!f.exists()) {
            f.mkdir();
        }
        File myCaptureFile = new File(f.getPath() + "/" + fileName);

        Toast.makeText(getContext(), "文件保存路径" + myCaptureFile.getPath(), Toast.LENGTH_LONG).show();

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }


    private void prepareEncoder() throws IOException {
        MediaFormat format = MediaFormat.createVideoFormat("video/avc", width, height);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 6000000);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);

        mediaCodec = MediaCodec.createEncoderByType("video/avc");
        mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        surface = mediaCodec.createInputSurface();
        mediaCodec.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void recordVirtualDisplay() {
        while (!mQuit.get()) {
            int index = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
            if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                resetOutputFormat();
            } else if (index >= 0) {
                encodeToVideoTrack(index);
                mediaCodec.releaseOutputBuffer(index, false);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void release() {
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
        }
        if (virtualDisplay != null) {
            virtualDisplay.release();
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
        }
        if (mediaMuxer != null) {
            mediaMuxer.release();
            mediaMuxer = null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void encodeToVideoTrack(int index) {
        ByteBuffer encodedData = mediaCodec.getOutputBuffer(index);

        if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
            bufferInfo.size = 0;
        }
        if (bufferInfo.size == 0) {
            encodedData = null;
        }
        if (encodedData != null) {
            encodedData.position(bufferInfo.offset);
            encodedData.limit(bufferInfo.offset + bufferInfo.size);
            mediaMuxer.writeSampleData(videoTrackIndex, encodedData, bufferInfo);
        }
    }


    private void resetOutputFormat() {
        MediaFormat newFormat = mediaCodec.getOutputFormat();
        videoTrackIndex = mediaMuxer.addTrack(newFormat);
        mediaMuxer.start();
        muxerStarted = true;
    }
//
//    Runnable progress = new Runnable() {
//        @Override
//        public void run() {
//            handler.postDelayed(progress,1000);
//            startHandmavlinkDate("many_light");
//        }
//    };


    private void initData(String str) {
        String filePath = "/sdcard/Test/";
        String fileName = "log.txt";

        writeTxtToFile(str, filePath, fileName);
    }


    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }


    // 生成文件
    public File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }

    }
}