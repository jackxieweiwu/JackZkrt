//package com.jack.jackzkrt.widget;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.jack.frame.util.show.T;
//import com.jack.jackzkrt.JackApplication;
//import com.jack.jackzkrt.R;
//import com.jack.jackzkrt.bean.rxbean.UITask;
//
//import dji.common.camera.SettingsDefinitions;
//import dji.common.error.DJIError;
//import dji.common.util.CommonCallbacks;
//import utils.rxutil.RxjavaUtil;
//import zkrt.ui.ModuleVerificationUtil;
//import zkrt.ui.base.BaseFrameLayout;
//import zkrt.ui.c.a.UiCAB;
//import zkrt.ui.d.UiDA;
//
///**
// * Created by jack_xie on 2017/7/26.
// *
// * @des ${TODO}
// */
//
//public class ZoomControlsWidget extends BaseFrameLayout implements View.OnClickListener,View.OnTouchListener {
//    private UiZoomControl widgetAppearances;
//    private Button mZoomMaxWidght;
//    private Button mZoomMinWidght;
//    private Button mZoomRecoveryWidght;
//    private TextView mZoomTxtWidght;
//    private int numberTap=1;
//    private int maxCameraTap;
//    private int minCameraTap;
//    private int focalLength;//间隔
//
//
//    public ZoomControlsWidget(Context var1) {
//        this(var1, (AttributeSet)null, 0);
//    }
//
//    public ZoomControlsWidget(Context var1, AttributeSet var2) {
//        this(var1, var2, 0);
//    }
//
//    public ZoomControlsWidget(Context var1, AttributeSet var2, int var3) {
//        super(var1, var2, var3);
//        UiDA.b();
//    }
//
//    public void initView(Context var1, AttributeSet var2, int var3) {
//        super.initView(var1,var2,var3);
//        this.setClickable(true);
//        mZoomMaxWidght = (Button) this.findViewById(R.id.widget_zoom_max);
//        mZoomMinWidght = (Button) this.findViewById(R.id.widget_zoom_min);
//        mZoomRecoveryWidght = (Button) this.findViewById(R.id.widget_zoom_recovery);
//        mZoomTxtWidght = (TextView) this.findViewById(R.id.txt_multipe);
//        mZoomMaxWidght.setOnClickListener(this);
//        mZoomMinWidght.setOnClickListener(this);
//        mZoomRecoveryWidght.setOnClickListener(this);
//
//        mZoomMaxWidght.setOnTouchListener(this);
//        mZoomMinWidght.setOnTouchListener(this);
//        mZoomRecoveryWidght.setOnTouchListener(this);
//
//        if(ModuleVerificationUtil.isCameraModuleAvailable()){
//            JackApplication.getAircraftInstance().getCamera().getOpticalZoomFactor(new CommonCallbacks.CompletionCallbackWith<Float>() {
//                        @Override
//                        public void onSuccess(final Float aFloat) {
//                            RxjavaUtil.doInUIThread(new UITask<Object>() {
//                                @Override
//                                public void doInUIThread() {
//                                    mZoomTxtWidght.setText(aFloat+"x");
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onFailure(DJIError djiError) {
//
//                        }
//                    });
//
//            //max
//            JackApplication.getAircraftInstance().getCamera().getOpticalZoomSpec(new CommonCallbacks.CompletionCallbackWith<SettingsDefinitions.OpticalZoomSpec>() {
//                @Override
//                public void onSuccess(SettingsDefinitions.OpticalZoomSpec opticalZoomSpec) {
//                    maxCameraTap = opticalZoomSpec.getMaxFocalLength();
//                    minCameraTap = opticalZoomSpec.getMinFocalLength();
//                    focalLength = opticalZoomSpec.getFocalLengthStep();
//                }
//
//                @Override
//                public void onFailure(DJIError djiError) {
//
//                }
//            });
//        }
//    }
//
//    private void tos(final String name){
//        RxjavaUtil.doInUIThread(new UITask<Object>() {
//            @Override
//            public void doInUIThread() {
//                T.show(context,name);
//            }
//        });
//    }
//
//    @Override
//    protected UiCAB getWidgetAppearances() {
//        if(this.widgetAppearances == null) {
//            this.widgetAppearances = new UiZoomControl();
//        }
//        return this.widgetAppearances;
//    }
//
//    @Override
//    public void onClick(View v) {
//        /*switch (v.getId()){
//            case R.id.widget_zoom_max:
//                if(numberTap<maxCameraTap)numberTap++;
//                setCameraTapZoom(0);
//                break;
//            case R.id.widget_zoom_min:
//                if(numberTap>minCameraTap)numberTap--;
//                setCameraTapZoom(1);
//                break;
//            case R.id.widget_zoom_recovery:
//                numberTap = 1;
//                setCameraTapZoom(2);
//                break;
//        }*/
//    }
//    private int num;
//    private void setCameraTapZoom(int number){
//        num = number;
//        if(ModuleVerificationUtil.isCameraModuleAvailable()){
//            if(number == 0) {
//                JackApplication.getAircraftInstance().getCamera().startContinuousOpticalZoom(SettingsDefinitions.ZoomDirection.ZOOM_IN, SettingsDefinitions.ZoomSpeed.NORMAL,
//                        new CommonCallbacks.CompletionCallback() {
//                            @Override
//                            public void onResult(DJIError djiError) {
//                                getCameraTxt(djiError);
//                            }
//                        });
//            }
//            if(number == 1) {
//                JackApplication.getAircraftInstance().getCamera().startContinuousOpticalZoom(SettingsDefinitions.ZoomDirection.ZOOM_OUT, SettingsDefinitions.ZoomSpeed.NORMAL,
//                        new CommonCallbacks.CompletionCallback() {
//                            @Override
//                            public void onResult(DJIError djiError) {
//                                getCameraTxt(djiError);
//                            }
//                        });
//            }
//
//            if(number == 2) {
//                JackApplication.getAircraftInstance().getCamera().startContinuousOpticalZoom(SettingsDefinitions.ZoomDirection.ZOOM_OUT, SettingsDefinitions.ZoomSpeed.NORMAL,
//                        new CommonCallbacks.CompletionCallback() {
//                            @Override
//                            public void onResult(DJIError djiError) {
//                                getCameraTxt(djiError);
//                            }
//                        });
//            }
//        }
//    }
//
//    private void getCameraTxt(DJIError djiError){
//        if(djiError == null){
//            if(ModuleVerificationUtil.isCameraModuleAvailable()) {
//                JackApplication.getAircraftInstance().getCamera().getOpticalZoomFactor(new CommonCallbacks.CompletionCallbackWith<Float>() {
//                    @Override
//                    public void onSuccess(final Float aFloat) {
//                        RxjavaUtil.doInUIThread(new UITask<Object>() {
//                            @Override
//                            public void doInUIThread() {
//                                mZoomTxtWidght.setText(aFloat + "x");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(DJIError djiError) {
//
//                    }
//                });
//            }
//        }
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_MOVE:
////                switch (v.getId()){
////                    case R.id.widget_zoom_max:
////                        setCameraTapZoom(0);
////                        break;
////                    case R.id.widget_zoom_min:
////                        setCameraTapZoom(1);
////                        break;
////                    case R.id.widget_zoom_recovery:
////                        setCameraTapZoom(2);
////                        break;
////                }
//                break;
//            case MotionEvent.ACTION_DOWN:
//                switch (v.getId()){
//                    case R.id.widget_zoom_max:
//                        setCameraTapZoom(0);
//                        break;
//                    case R.id.widget_zoom_min:
//                        setCameraTapZoom(1);
//                        break;
//                    case R.id.widget_zoom_recovery:
//                        setCameraTapZoom(2);
//                        break;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                if(num != 2) {
//                    if (ModuleVerificationUtil.isCameraModuleAvailable()) {
//                        JackApplication.getAircraftInstance().getCamera().stopContinuousOpticalZoom(new CommonCallbacks.CompletionCallback() {
//                            @Override
//                            public void onResult(DJIError djiError) {
//
//                            }
//                        });
//                    }
//                }
//                break;
//        }
//        return false;
//    }
//}
