package zkrt.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import dji.common.bus.UILibEventBus;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.SetCallback;
import dji.log.DJILog;
import dji.thirdparty.rx.android.schedulers.AndroidSchedulers;
import dji.thirdparty.rx.functions.Action1;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseGViewDULFrameLayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBS;
import zkrt.ui.d.UiDA;
import zkrt.ui.internal.BInterNal;

/**
 * Created by jack_xie on 17-6-1.
 */

public class FocusExposureSwitchWidget extends UiBaseGViewDULFrameLayout implements View.OnClickListener {
    private static final String TAG = "DULFocusExposureSwitchWidget";
    private UiCAC widgetAppearances;
    private ImageView foregroundImage;
    private FocusExposureSwitchWidget.ControlMode controlMode;
    private boolean manualFocusEnabled;
    private int foregroundResId;
    private DJIKey meteringModeKey;
    private DJIKey lenIsFocusAssistantWorkingKey;

    public FocusExposureSwitchWidget(Context var1) {
        super(var1, null, 0);
    }

    public FocusExposureSwitchWidget(Context var1, AttributeSet var2) {
        super(var1, var2, 0);
    }

    public FocusExposureSwitchWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        UiDA.b();
        this.foregroundImage = this.findViewById(R.id.image_button_foreground);
        this.foregroundImage.setOnClickListener(this);
        this.switchMode(FocusExposureSwitchWidget.ControlMode.AUTO_FOCUS);
        UILibEventBus.getInstance().register(BInterNal.c.class).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BInterNal.c>() {
                    @Override
                    public void call(BInterNal.c c) {
                        FocusExposureSwitchWidget.this.manualFocusEnabled = c.a == SettingsDefinitions.FocusMode.MANUAL;
                        FocusExposureSwitchWidget.this.switchMode(FocusExposureSwitchWidget.this.controlMode);
                    }
                });
    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBS();
        }

        return this.widgetAppearances;
    }

    public void initKey() {
        this.meteringModeKey = CameraKey.create("MeteringMode");
        this.lenIsFocusAssistantWorkingKey = CameraKey.create("LensIsFocusAssistantWorking");
        this.addDependentKey(this.lenIsFocusAssistantWorkingKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
    }

    public void updateWidget(DJIKey var1) {
        this.foregroundImage.setImageResource(this.foregroundResId);
    }

    public void onClick(View var1) {
        if(KeyManager.getInstance() != null) {
            if(this.controlMode == FocusExposureSwitchWidget.ControlMode.SPOT_METER) {
                this.switchMode(FocusExposureSwitchWidget.ControlMode.AUTO_FOCUS);
            } else {
                this.switchMode(FocusExposureSwitchWidget.ControlMode.SPOT_METER);
                KeyManager.getInstance().setValue(this.meteringModeKey, SettingsDefinitions.MeteringMode.SPOT, new SetCallback() {
                    public void onSuccess() {
                        DJILog.d("DULFocusExposureSwitchWidget", "Spot metering set successfully!");
                    }

                    public void onFailure(@NonNull DJIError var1) {
                        DJILog.d("DULFocusExposureSwitchWidget", "Failed to set Spot metering:" + var1.getDescription());
                    }
                });
            }

        }
    }

    private void switchMode(FocusExposureSwitchWidget.ControlMode var1) {
        switch(var1.ordinal()) {
            case 0:
            case 1:
                this.foregroundResId = R.drawable.rectangle_1112_metering_icon;
                this.controlMode = FocusExposureSwitchWidget.ControlMode.SPOT_METER;
                break;
            case 2:
            case 3:
                if(this.manualFocusEnabled) {
                    this.foregroundResId = R.drawable.mf_area;
                    this.controlMode = FocusExposureSwitchWidget.ControlMode.MANUAL_FOCUS;
                } else {
                    this.foregroundResId = R.drawable.rectangle_314_copy_2;
                    this.controlMode = FocusExposureSwitchWidget.ControlMode.AUTO_FOCUS;
                }
        }

        UILibEventBus.getInstance().post(this.controlMode);
        this.updateWidget(null);
    }

    public static enum ControlMode {
        SPOT_METER,
        CENTER_METER,
        AUTO_FOCUS,
        MANUAL_FOCUS;

        private ControlMode() {
        }
    }
}
