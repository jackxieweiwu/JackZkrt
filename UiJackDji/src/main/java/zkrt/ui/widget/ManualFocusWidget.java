package zkrt.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import dji.common.bus.UILibEventBus;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.SetCallback;
import dji.thirdparty.rx.android.schedulers.AndroidSchedulers;
import dji.thirdparty.rx.functions.Action1;
import zkrt.ui.d.UiDL;
import zkrt.ui.internal.EInterNal;
import zkrt.ui.widget.FocusExposureSwitchWidget.ControlMode;

/**
 * Created by jack_xie on 17-11-6.
 */

public class ManualFocusWidget extends EInterNal {
    private static int focusRingMaxValue = 100;
    private CameraKey cameraFocusRingUpperBoundKey;
    private CameraKey cameraFocusRingKey;
    private CameraKey cameraTypeKey;
    private int focusRingValue;
    private boolean inverted;

    public ManualFocusWidget(Context var1, AttributeSet var2) {
        super(var1, var2, 0);
        this.setVisibility(INVISIBLE);
        UILibEventBus.getInstance().register(ControlMode.class).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ControlMode>() {
                    @Override
                    public void call(ControlMode controlMode) {
                        UiDL.a(ManualFocusWidget.this, controlMode == ControlMode.MANUAL_FOCUS);
                    }
                });
    }

    public void initKey() {
        this.cameraFocusRingUpperBoundKey = CameraKey.create("FocusRingValueUpperBound");
        this.addDependentKey(this.cameraFocusRingUpperBoundKey);
        this.cameraTypeKey = CameraKey.create("CameraType");
        this.addDependentKey(this.cameraTypeKey);
        this.cameraFocusRingKey = CameraKey.create("FocusRingValue");
        this.addDependentKey(this.cameraFocusRingKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2 == this.cameraFocusRingUpperBoundKey) {
            focusRingMaxValue = ((Integer)var1).intValue();
        } else if(var2 == this.cameraTypeKey) {
            SettingsDefinitions.CameraType var3 = (SettingsDefinitions.CameraType)var1;
            this.inverted = var3 == SettingsDefinitions.CameraType.DJICameraTypeFC6510 || var3 == SettingsDefinitions.CameraType.DJICameraTypeFC6520 || var3 == SettingsDefinitions.CameraType.DJICameraTypeFC550 || var3 == SettingsDefinitions.CameraType.DJICameraTypeFC550Raw || var3 == SettingsDefinitions.CameraType.DJICameraTypeFC220 || var3 == SettingsDefinitions.CameraType.DJICameraTypeFC220S || var3 == SettingsDefinitions.CameraType.DJICameraTypeGD600 || var3 == SettingsDefinitions.CameraType.DJICameraTypeFC6310;
        } else {
            this.focusRingValue = ((Integer)var1).intValue();
            float var4 = (float)this.focusRingValue / (float)focusRingMaxValue;
            if(this.inverted) {
                var4 = 1.0F - var4;
            }

            this.onValueUpdatedExternally(var4);
        }

    }

    protected void onValueUpdate(float var1) {
        if(this.inverted) {
            var1 = 1.0F - var1;
        }

        this.focusRingValue = (int)(var1 * (float)focusRingMaxValue);
        if(KeyManager.getInstance() != null) {
            KeyManager.getInstance().setValue(this.cameraFocusRingKey, Integer.valueOf(this.focusRingValue), new SetCallback() {
                public void onSuccess() {
                }

                public void onFailure(@NonNull DJIError var1) {
                }
            });
        }
    }
}
