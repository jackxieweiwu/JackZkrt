package zkrt.ui.widget.controls;

import android.content.Context;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.ActionCallback;
import dji.log.DJILog;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.schedulers.Schedulers;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseEView;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBF;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDE;
import zkrt.ui.d.UiDH;
import zkrt.ui.d.UiDJ;
import zkrt.ui.internal.a.Inter_A;
import zkrt.ui.internal.a.Inter_G;

/**
 * Created by jack_xie on 17-6-1.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraCaptureWidget extends UiBaseEView {
    private static final String TAG = "CameraCaptureWidget";
    private static final int DEFAULT_NULL_RES = 0;
    private static final int SHOOTING_PHOTO_SOUND = 0;
    private static final int START_RECORDING_SOUND = 1;
    private static final int STOP_RECORDING_SOUND = 2;
    private SettingsDefinitions.CameraMode cameraMode;
    private SettingsDefinitions.ShootPhotoMode shootPhotoMode;
    private SettingsDefinitions.PhotoAEBCount aebParam;
    private SettingsDefinitions.PhotoBurstCount photoBurstCount;
    private SettingsDefinitions.PhotoTimeIntervalSettings photoIntervalParam;
    private boolean isShootingIntervalPhoto;
    private boolean isShootingPhotoEnabled;
    private Integer currentBackgroundResId;
    private Integer currentForegroundBackResId;
    private Integer currentForegroundTopResId;
    private String recordingTime;
    private boolean isRecording;
    private Animation savingAnim;
    private UiDH captureSound;
    private boolean isShootingPhoto;
    private TextView textviewVideoTime;
    protected ImageView imageForegroundTop;
    private UiCAC widgetAppearances;
    private DJIKey cameraModeKey;
    private DJIKey shootPhotoModeKey;
    private DJIKey photoAEBParamKey;
    private DJIKey photoBurstCountKey;
    private DJIKey photoIntervalParamKey;
    private DJIKey isShootingPhotoEnabledKey;
    private DJIKey isShootingIntervalPhotoKey;
    private DJIKey isShootingPhotoKey;
    private DJIKey isShootingShallowFocusKey;
    private DJIKey isShootingPanoramaKey;
    private DJIKey isRecordingKey;
    private DJIKey recordingTimeKey;
    private DJIKey startRecordVideoKey;
    private DJIKey stopRecordVideoKey;
    private DJIKey stopShootPhotoKey;
    private DJIKey startShootPhotoKey;
    private DJIKey rawBurstCountKey;
    private DJIKey panoramModeKey;
    private SettingsDefinitions.PhotoBurstCount rawBurstCount;
    private SettingsDefinitions.PhotoPanoramaMode panoramMode;
    private boolean isShootingShallowFocusPhoto;
    private boolean isShootingPanoramaPhoto;
    private AtomicBoolean isAnimationRunning;

    public CameraCaptureWidget(Context var1) {
        this(var1, (AttributeSet)null, 0);
    }

    public CameraCaptureWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public CameraCaptureWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.isAnimationRunning = new AtomicBoolean(false);
        UiDA.b();
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.imageBackground = (ImageButton)this.findViewById(R.id.image_button_background);
        this.imageForeground = (ImageView)this.findViewById(R.id.image_button_foreground);
        this.imageForegroundTop = (ImageView)this.findViewById(R.id.image_button_foreground_top);
        this.textviewVideoTime = (TextView)this.findViewById(R.id.textview_camera_controll_videotime);
        this.currentForegroundTopResId = Integer.valueOf(0);
        this.imageBackground.setOnClickListener(this);
        this.imageForeground.setOnClickListener(this);
        this.savingAnim = AnimationUtils.loadAnimation(var1, R.anim.storing_rotate);
        this.savingAnim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation var1) {
                CameraCaptureWidget.this.isAnimationRunning.set(true);
            }

            public void onAnimationRepeat(Animation var1) {
            }

            public void onAnimationEnd(Animation var1) {
                CameraCaptureWidget.this.isAnimationRunning.set(false);
            }
        });
        this.captureSound = new UiDH(this.getContext());
    }

    protected UiCAC getWidgetAppearances() {
        if (widgetAppearances == null) {
            widgetAppearances = new UiCBF();
        }
        return widgetAppearances;
    }

    public void initKey() {
        this.startRecordVideoKey = CameraKey.create("StartRecordVideo");
        this.stopRecordVideoKey = CameraKey.create("StopRecordVideo");
        this.stopShootPhotoKey = CameraKey.create("StopShootPhoto");
        this.startShootPhotoKey = CameraKey.create("StartShootPhoto");
        this.cameraModeKey = CameraKey.create("Mode");
        this.addDependentKey(this.cameraModeKey);
        this.shootPhotoModeKey = CameraKey.create("ShootPhotoMode");
        this.addDependentKey(this.shootPhotoModeKey);
        this.photoAEBParamKey = CameraKey.create("PhotoAEBCount");
        this.addDependentKey(this.photoAEBParamKey);
        this.photoBurstCountKey = CameraKey.create("PhotoBurstCount");
        this.addDependentKey(this.photoBurstCountKey);
        this.photoIntervalParamKey = CameraKey.create("PhotoTimeIntervalSettings");
        this.addDependentKey(this.photoIntervalParamKey);
        this.isShootingPhotoEnabledKey = CameraKey.create("IsShootPhotoEnabled");
        this.addDependentKey(this.isShootingPhotoEnabledKey);
        this.isShootingIntervalPhotoKey = CameraKey.create("IsShootingIntervalPhoto");
        this.addDependentKey(this.isShootingIntervalPhotoKey);
        this.isShootingPhotoKey = CameraKey.create("IsShootingPhoto");
        this.addDependentKey(this.isShootingPhotoKey);
        this.isRecordingKey = CameraKey.create("IsRecording");
        this.addDependentKey(this.isRecordingKey);
        this.recordingTimeKey = CameraKey.create("CurrentVideoRecordingTimeInSeconds");
        this.addDependentKey(this.recordingTimeKey);
        this.rawBurstCountKey = CameraKey.create("PhotoRAWBurstCount");
        this.addDependentKey(this.rawBurstCountKey);
        this.panoramModeKey = CameraKey.create("PhotoPanoramaMode");
        this.addDependentKey(this.panoramModeKey);
        this.isShootingShallowFocusKey = CameraKey.create("IsShootingShallowFocusPhoto");
        this.addDependentKey(this.isShootingShallowFocusKey);
        this.isShootingPanoramaKey = CameraKey.create("IsShootingPanoramaPhoto");
        this.addDependentKey(this.isShootingPanoramaKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.cameraModeKey)) {
            this.cameraMode = (SettingsDefinitions.CameraMode)var1;
        } else if(var2.equals(this.shootPhotoModeKey)) {
            this.shootPhotoMode = (SettingsDefinitions.ShootPhotoMode)((SettingsDefinitions.ShootPhotoMode)var1);
        } else if(var2.equals(this.photoAEBParamKey)) {
            this.aebParam = (SettingsDefinitions.PhotoAEBCount)((SettingsDefinitions.PhotoAEBCount)var1);
        } else if(var2.equals(this.photoBurstCountKey)) {
            this.photoBurstCount = (SettingsDefinitions.PhotoBurstCount)((SettingsDefinitions.PhotoBurstCount)var1);
        } else if(var2.equals(this.rawBurstCountKey)) {
            this.rawBurstCount = (SettingsDefinitions.PhotoBurstCount)((SettingsDefinitions.PhotoBurstCount)var1);
        } else if(var2.equals(this.panoramModeKey)) {
            this.panoramMode = (SettingsDefinitions.PhotoPanoramaMode)((SettingsDefinitions.PhotoPanoramaMode)var1);
        } else if(var2.equals(this.photoIntervalParamKey)) {
            this.photoIntervalParam = (SettingsDefinitions.PhotoTimeIntervalSettings)((SettingsDefinitions.PhotoTimeIntervalSettings)var1);
        } else if(var2.equals(this.isShootingIntervalPhotoKey)) {
            this.isShootingIntervalPhoto = ((Boolean)((Boolean)var1)).booleanValue();
        } else if(var2.equals(this.isShootingPhotoKey)) {
            this.isShootingPhoto = ((Boolean)((Boolean)var1)).booleanValue();
            if(this.isShootingPhoto) {
                this.playSound(0);
            }
        } else if(var2.equals(this.isShootingPhotoEnabledKey)) {
            this.isShootingPhotoEnabled = ((Boolean)((Boolean)var1)).booleanValue();
        } else if(var2.equals(this.isRecordingKey)) {
            this.isRecording = ((Boolean)((Boolean)var1)).booleanValue();
            if(this.isRecording) {
                this.playSound(1);
            } else {
                this.playSound(2);
            }
        } else if(var2.equals(this.recordingTimeKey)) {
            this.recordingTime = this.formatVideoTime((Integer)var1);
        } else if(var2.equals(this.isShootingShallowFocusKey)) {
            this.isShootingShallowFocusPhoto = ((Boolean)var1).booleanValue();
            if(this.isShootingShallowFocusPhoto) {
                this.playSound(0);
            }
        } else if(var2.equals(this.isShootingPanoramaKey)) {
            this.isShootingPanoramaPhoto = ((Boolean)var1).booleanValue();
        }

        this.setCaptureBackground(this.cameraMode);
        this.setForegroundRes(this.shootPhotoMode);
    }

    private boolean isShootingAndStoringPhoto() {
        return this.isShootingPhoto || this.isShootingPanoramaPhoto || this.isShootingShallowFocusPhoto;
    }

    public void updateWidget(DJIKey var1) {
        if(var1.equals(this.cameraModeKey)) {
            this.onCameraModeChange(this.cameraMode);
        } else if(var1.equals(this.isRecordingKey)) {
            this.onIsRecordingEnable(this.isRecording);
        } else if(var1.equals(this.recordingTimeKey)) {
            this.onRecordingTimeChange(this.recordingTime);
        } else if(!var1.equals(this.isShootingPhotoKey) && !var1.equals(this.isShootingShallowFocusKey) && !var1.equals(this.isShootingPanoramaKey)) {
            if(var1.equals(this.isShootingIntervalPhotoKey)) {
                this.onIsShootingIntervalPhotoEnable(this.isShootingIntervalPhoto);
            } else if(var1.equals(this.photoAEBParamKey)) {
                if(this.shootPhotoMode == SettingsDefinitions.ShootPhotoMode.AEB) {
                    this.updateBackgroundAndForeground();
                }
            } else if(var1.equals(this.photoBurstCountKey)) {
                if(this.shootPhotoMode == SettingsDefinitions.ShootPhotoMode.BURST) {
                    this.updateBackgroundAndForeground();
                }
            } else if(var1.equals(this.photoIntervalParamKey)) {
                if(this.shootPhotoMode == SettingsDefinitions.ShootPhotoMode.INTERVAL) {
                    this.updateBackgroundAndForeground();
                }
            } else {
                this.updateBackgroundAndForeground();
            }
        } else {
            this.onIsStoringPhotoEnable();
        }

    }

    @MainThread
    private void onCameraModeChange(SettingsDefinitions.CameraMode var1) {
        if(var1 == null || var1 != SettingsDefinitions.CameraMode.SHOOT_PHOTO && var1 != SettingsDefinitions.CameraMode.RECORD_VIDEO) {
            this.setEnabled(false);
        } else {
            this.setEnabled(true);
            this.updateBackgroundAndForeground();
        }

    }

    private void playSound(final int var1) {
        Observable.just(Boolean.valueOf(true)).subscribeOn(Schedulers.computation()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if(var1 == 0) {
                    CameraCaptureWidget.this.captureSound.a(CameraCaptureWidget.this.getPictureNumber());
                } else if(var1 == 1) {
                    CameraCaptureWidget.this.captureSound.a();
                } else if(var1 == 2) {
                    CameraCaptureWidget.this.captureSound.b();
                }
            }
        });
    }

    @MainThread
    private void onIsRecordingEnable(boolean var1) {
        if(var1) {
            this.textviewVideoTime.setEnabled(true);
            this.textviewVideoTime.setVisibility(VISIBLE);
        } else {
            this.textviewVideoTime.setVisibility(GONE);
        }

        this.updateBackgroundAndForeground();
    }

    @MainThread
    private void onRecordingTimeChange(String var1) {
        this.textviewVideoTime.setText(var1);
    }

    @MainThread
    private void onIsStoringPhotoEnable() {
        if(this.isShootingAndStoringPhoto()) {
            if(this.isAnimationRunning.compareAndSet(false, true)) {
                this.updateBackgroundAndForeground();
                this.imageBackground.startAnimation(this.savingAnim);
            }
        } else {
            this.imageBackground.clearAnimation();
            this.savingAnim.cancel();
            this.savingAnim.reset();
            this.updateBackgroundAndForeground();
        }

    }

    @MainThread
    private void onIsShootingIntervalPhotoEnable(boolean var1) {
        this.updateBackgroundAndForeground();
    }


    private void updateBackgroundAndForeground() {
        if(this.currentBackgroundResId != null) {
            this.imageBackground.setBackgroundResource(this.currentBackgroundResId.intValue());
        }

        if(this.currentForegroundBackResId != null) {
            if(this.currentForegroundBackResId.intValue() == 0) {
                this.imageForeground.setVisibility(GONE);
            } else {
                this.imageForeground.setVisibility(VISIBLE);
                this.imageForeground.setImageResource(this.currentForegroundBackResId.intValue());
            }

            if(this.cameraMode == SettingsDefinitions.CameraMode.SHOOT_PHOTO && this.currentForegroundTopResId.intValue() != 0) {
                this.imageForegroundTop.setVisibility(VISIBLE);
                this.imageForegroundTop.setImageDrawable(UiDJ.a(this.context, this.currentForegroundTopResId.intValue()));
            } else {
                this.imageForegroundTop.setVisibility(GONE);
            }
        }

    }

    private String formatVideoTime(Integer var1) {
        if(var1 == null) {
            var1 = Integer.valueOf(0);
        }

        int[] var2 = UiDE.a(var1.intValue());
        return this.getContext().getString(R.string.videotime, new Object[]{Integer.valueOf(var2[2]), Integer.valueOf(var2[1]), Integer.valueOf(var2[0])});
    }

    private int getPictureNumber() {
        if(this.shootPhotoMode == SettingsDefinitions.ShootPhotoMode.AEB) {
            if(this.aebParam != null) {
                return this.aebParam.value();
            }
        } else if(this.shootPhotoMode == SettingsDefinitions.ShootPhotoMode.BURST) {
            if(this.photoBurstCount != null) {
                return this.photoBurstCount.value();
            }
        } else if(this.shootPhotoMode == SettingsDefinitions.ShootPhotoMode.RAW_BURST && this.rawBurstCount != null) {
            return this.rawBurstCount.value();
        }

        return 0;
    }

    private void setCaptureBackground(SettingsDefinitions.CameraMode var1) {
        if(var1 != null) {
            switch(var1.ordinal()) {
                case 0://原来为1
                    if(this.isShootingAndStoringPhoto()) {
                        this.currentBackgroundResId = Integer.valueOf(R.drawable.btn_cam_capture_progress);
                    } else {
                        this.currentBackgroundResId = Integer.valueOf(R.drawable.btn_cam_capture_border);
                    }
                    break;
                case 1://原来为2
                    if(this.isRecording) {
                        this.currentBackgroundResId = Integer.valueOf(R.drawable.selector_camera_control_stopvideo);
                    } else {
                        this.currentBackgroundResId = Integer.valueOf(R.drawable.selector_camera_control_recordvideo);
                    }
            }

        }
    }

    private void setForegroundRes(SettingsDefinitions.ShootPhotoMode var1) {

        if(this.cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO) {
            this.currentForegroundBackResId = Integer.valueOf(0);
        } else if(var1 != null) {
            this.currentForegroundBackResId = Integer.valueOf(R.drawable.selector_camera_control_shoot_photo);
            this.currentForegroundTopResId = Integer.valueOf(0);
            int var2;
            switch(var1.ordinal()) {
                case 1:
                    break;
                case 2:
                    this.currentForegroundBackResId = Integer.valueOf(R.drawable.selector_camera_control_shoot_hdr_photo);
                    break;
                case 3:
                    if(this.isShootingIntervalPhoto) {
                        this.currentForegroundBackResId = Integer.valueOf(R.drawable.selector_camera_control_stopintervalphoto);
                        this.currentForegroundTopResId = Integer.valueOf(0);
                    } else {
                        this.currentBackgroundResId = Integer.valueOf(R.drawable.btn_cam_capture_border);
                        if(this.photoIntervalParam != null) {
                            this.currentForegroundTopResId = Integer.valueOf(Inter_G.a(SettingsDefinitions.ShootPhotoMode.INTERVAL.value(), this.photoIntervalParam.getTimeIntervalInSeconds()));
                        }
                    }
                    break;
                case 4:
                    if(this.photoBurstCount != null) {
                        var2 = this.photoBurstCount.value();
                        this.currentForegroundTopResId = Integer.valueOf(Inter_G.a(SettingsDefinitions.ShootPhotoMode.BURST.value(), var2));
                    }
                    break;
                case 5:
                    if(this.aebParam != null) {
                        var2 = this.aebParam.value();
                        this.currentForegroundTopResId = Integer.valueOf(Inter_G.a(SettingsDefinitions.ShootPhotoMode.AEB.value(), var2));
                    }
                    break;
                case 6:
                    if(this.rawBurstCount != null) {
                        var2 = this.rawBurstCount.value();
                        this.currentForegroundTopResId = Integer.valueOf(Inter_G.a(SettingsDefinitions.ShootPhotoMode.RAW_BURST.value(), var2));
                    }
                    break;
                case 7:
                    if(this.panoramMode != null) {
                        var2 = this.panoramMode.value();
                        this.currentForegroundTopResId = Integer.valueOf(Inter_G.a(SettingsDefinitions.ShootPhotoMode.PANORAMA.value(), var2));
                    }
                    break;
                case 8:
                    this.currentForegroundTopResId = Integer.valueOf(Inter_G.a(SettingsDefinitions.ShootPhotoMode.SHALLOW_FOCUS.value(), 0));
                    break;
                default:
                    this.currentForegroundTopResId = Integer.valueOf(0);
            }

        }
    }

    public void onClick(View var1) {
        if(this.cameraMode == SettingsDefinitions.CameraMode.SHOOT_PHOTO) {
            this.onClickPhoto();
        } else if(this.cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO) {
            this.onClickVideo();
        }

    }

    private void onClickVideo() {
        if(KeyManager.getInstance() != null) {
            if(!this.isRecording) {
                KeyManager.getInstance().performAction(this.startRecordVideoKey, new ActionCallback() {
                    public void onSuccess() {
                    }

                    public void onFailure(@NonNull DJIError var1) {
                        DJILog.d("CameraCaptureWidget", "Failed to start record video: " + var1.getDescription());
                    }
                }, new Object[0]);
            } else {
                KeyManager.getInstance().performAction(this.stopRecordVideoKey, new ActionCallback() {
                    public void onSuccess() {
                    }

                    public void onFailure(@NonNull DJIError var1) {
                        DJILog.d("CameraCaptureWidget", "Failed to stop record video: " + var1.getDescription());
                    }
                }, new Object[0]);
            }

        }
    }

    private void onClickPhoto() {
        if(KeyManager.getInstance() != null) {
            if(this.shootPhotoMode == SettingsDefinitions.ShootPhotoMode.INTERVAL && this.isShootingIntervalPhoto) {
                this.playSound(2);
                KeyManager.getInstance().performAction(this.stopShootPhotoKey, new ActionCallback() {
                    public void onSuccess() {
                    }

                    public void onFailure(@NonNull DJIError var1) {
                        DJILog.d("CameraCaptureWidget", "Failed to stop shoot photo: " + var1.getDescription());
                    }
                }, new Object[0]);
            } else {
                KeyManager.getInstance().performAction(this.startShootPhotoKey, new ActionCallback() {
                    public void onSuccess() {
                    }

                    public void onFailure(@NonNull DJIError var1) {
                        DJILog.d("CameraCaptureWidget", "Failed to start shoot photo:" + var1.getDescription());
                    }
                }, new Object[0]);
            }

        }
    }
}
