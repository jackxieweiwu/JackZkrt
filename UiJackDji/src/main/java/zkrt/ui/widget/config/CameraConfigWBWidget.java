package zkrt.ui.widget.config;

import android.content.Context;
import android.util.AttributeSet;

import dji.common.camera.SettingsDefinitions;
import dji.common.camera.WhiteBalance;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseDView;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBJ;
import zkrt.ui.d.UiDA;

/**
 * Created by jack_xie on 17-6-1.
 */

public class CameraConfigWBWidget extends UiBaseDView {
    private WhiteBalance whiteBalance;
    private CameraKey wbAndColorTempKey;
    private String[] wbNameArray;
    private UiCBJ widgetAppearances;

    public CameraConfigWBWidget(Context var1) {
        this(var1, (AttributeSet)null, 0);
    }

    public CameraConfigWBWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public CameraConfigWBWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        UiDA.b();
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.wbNameArray = this.getResources().getStringArray(R.array.camera_white_balance_name_array);
    }

    protected String getTitle() {
        return "WB";
    }

    protected UiCAC getWidgetAppearances() {
        this.widgetAppearances = this.widgetAppearances;
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBJ();
        }

        return this.widgetAppearances;
    }

    public void initKey() {
        this.wbAndColorTempKey = CameraKey.create("WhiteBalance");
        this.addDependentKey(this.wbAndColorTempKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.wbAndColorTempKey) && var1 != null && var1 instanceof WhiteBalance) {
            this.whiteBalance = (WhiteBalance)var1;
        }

    }

    public void updateWidget(DJIKey var1) {
        if(this.whiteBalance.getWhiteBalancePreset() == SettingsDefinitions.WhiteBalancePreset.CUSTOM) {
            String var2 = String.format("%dK", new Object[]{Integer.valueOf(this.whiteBalance.getColorTemperature() * 100)});
            this.valueText.setText(var2);
        } else {
            int var3 = this.whiteBalance.getWhiteBalancePreset().value();
            if(var3 < this.wbNameArray.length) {
                this.valueText.setText(this.wbNameArray[var3]);
            }
        }

    }
}
