package zkrt.ui.widget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import dji.common.bus.UILibEventBus;
import dji.common.camera.SettingsDefinitions;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.SetCallback;
import dji.thirdparty.rx.android.schedulers.AndroidSchedulers;
import dji.thirdparty.rx.functions.Action1;
import zkrt.ui.base.UiBaseGViewDULFrameLayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.d.UiDA;
import zkrt.ui.internal.AInterNal;

/**
 * Created by jack_xie on 17-6-1.
 */

public class FPVOverlayWidget extends UiBaseGViewDULFrameLayout implements View.OnTouchListener {
    private AInterNal crosshair;
    private static final DJIKey METERING_MODE_KEY = CameraKey.create("MeteringMode");
    private static final DJIKey FOCUS_MODE_KEY = CameraKey.create("FocusMode");
    private static final DJIKey FOCUS_TARGET_KEY = CameraKey.create("FocusTarget");
    private static final DJIKey METERING_TARGET_KEY = CameraKey.create("SpotMeteringTarget");
    private SettingsDefinitions.MeteringMode meteringMode;
    private SettingsDefinitions.FocusMode focusMode;
    private FocusExposureSwitchWidget.ControlMode controlMode;
    private static final Integer[] METER_ROW_INDEX = new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7)};
    private static final Integer[] METER_COL_INDEX = new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11)};

    public FPVOverlayWidget(Context var1) {
        this(var1, (AttributeSet)null, 0);
    }

    public FPVOverlayWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public FPVOverlayWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.meteringMode = null;
        this.focusMode = null;
        this.controlMode = FocusExposureSwitchWidget.ControlMode.AUTO_FOCUS;
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        UiDA.b();
        this.setOnTouchListener(this);
        this.crosshair = new AInterNal(var1);
        this.addView(this.crosshair);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.subscription.add(UILibEventBus.getInstance().register(FocusExposureSwitchWidget.ControlMode.class).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FocusExposureSwitchWidget.ControlMode>() {
                    @Override
                    public void call(FocusExposureSwitchWidget.ControlMode controlMode) {
                        FPVOverlayWidget.this.controlMode = controlMode;
                        FPVOverlayWidget.this.crosshair.setControlMode(controlMode);
                    }
                }));
    }

    public boolean onTouch(View var1, MotionEvent var2) {
        switch(var2.getAction()) {
            case 0:
                return true;
            case 1:
                float var3 = var2.getX();
                float var4 = var2.getY();
                float var5 = var3 / (float)var1.getWidth();
                float var6 = var4 / (float)var1.getHeight();
                this.controlMode = this.crosshair.a(this.controlMode, var3, var4, (float)var1.getWidth(), (float)var1.getHeight());
                this.updateCameraTargetIfNeeded(var5, var6);
                return true;
            default:
                return false;
        }
    }

    private void updateCameraTargetIfNeeded(float var1, float var2) {
        if(this.controlMode == FocusExposureSwitchWidget.ControlMode.AUTO_FOCUS) {
            if(this.focusMode != null && this.focusMode == SettingsDefinitions.FocusMode.AUTO) {
                KeyManager.getInstance().setValue(FOCUS_TARGET_KEY, new PointF(var1, var2), (SetCallback)null);
            }
        } else if(this.controlMode == FocusExposureSwitchWidget.ControlMode.SPOT_METER) {
            int var3 = METER_COL_INDEX[(int)(var1 * (float)METER_COL_INDEX.length)].intValue();
            int var4 = METER_ROW_INDEX[(int)(var2 * (float)METER_ROW_INDEX.length)].intValue();
            if(this.meteringMode != null && this.meteringMode != SettingsDefinitions.MeteringMode.SPOT) {
                KeyManager.getInstance().setValue(METERING_MODE_KEY, SettingsDefinitions.MeteringMode.SPOT, (SetCallback)null);
            }

            KeyManager.getInstance().setValue(METERING_TARGET_KEY, new Point(var3, var4), (SetCallback)null);
        } else {
            KeyManager.getInstance().setValue(METERING_MODE_KEY, SettingsDefinitions.MeteringMode.CENTER, (SetCallback)null);
        }

    }

    public void initKey() {
        this.addDependentKey(METERING_MODE_KEY);
        this.addDependentKey(FOCUS_MODE_KEY);
        this.addDependentKey(FOCUS_TARGET_KEY);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(METERING_MODE_KEY)) {
            this.meteringMode = (SettingsDefinitions.MeteringMode)var1;
        } else if(var2.equals(FOCUS_MODE_KEY)) {
            this.focusMode = (SettingsDefinitions.FocusMode)var1;
        }

    }

    protected UiCAC getWidgetAppearances() {
        return null;
    }

    public void updateWidget(DJIKey var1) {
    }
}
