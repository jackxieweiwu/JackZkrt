package zkrt.ui.widget.config;

import android.content.Context;
import android.util.AttributeSet;

import dji.common.camera.ExposureSettings;
import dji.common.camera.SettingsDefinitions;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import zkrt.ui.base.UiBaseDView;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDC;

/**
 * Created by jack_xie on 17-6-1.
 */

public class CameraConfigShutterWidget extends UiBaseDView {
    private SettingsDefinitions.ShutterSpeed shutterSpeed;
    private CameraKey currentExposureValueKey;

    public CameraConfigShutterWidget(Context var1) {
        this(var1, (AttributeSet)null, 0);
    }

    public CameraConfigShutterWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public CameraConfigShutterWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        UiDA.b();
    }

    protected String getTitle() {
        return "SHUTTER";
    }

    public void initKey() {
        this.currentExposureValueKey = CameraKey.create("ExposureSettings");
        this.addDependentKey(this.currentExposureValueKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.currentExposureValueKey)) {
            ExposureSettings var3 = (ExposureSettings)var1;
            this.shutterSpeed = var3.getShutterSpeed();
        }

    }

    public void updateWidget(DJIKey var1) {
        if(this.shutterSpeed != null) {
            this.valueText.setText(this.convertToDisplayName(this.shutterSpeed));
        }

    }

    private String convertToDisplayName(SettingsDefinitions.ShutterSpeed var1) {
        String var2 = UiDC.a(var1);
        if(!var2.contains("\"") && var1 != SettingsDefinitions.ShutterSpeed.UNKNOWN) {
            var2 = "1/" + var2 + "\"";
        }

        return var2;
    }
}
