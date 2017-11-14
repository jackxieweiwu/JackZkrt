package zkrt.ui.widget;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Keep;
import android.support.annotation.MainThread;
import android.util.AttributeSet;
import android.widget.ImageView;

import dji.keysdk.AirLinkKey;
import dji.keysdk.DJIKey;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseGViewDULFrameLayout;
import zkrt.ui.c.a.UiCAB;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBA_G;
import zkrt.ui.d.UiDA;

/**
 * Created by jack_xie on 17-6-1.
 */

public class RemoteControlSignalWidget extends UiBaseGViewDULFrameLayout {
    private ImageView rcSignal;
    private UiCAC widgetAppearances;
    private int signalValue;
    private AirLinkKey ocuSignalQualityKey;
    private AirLinkKey wifiSignalQualityKey;
    private AirLinkKey lightbridgeSignalQualityKey;

    public RemoteControlSignalWidget(Context var1, AttributeSet var2) {
        super(var1, var2, 0);
        UiDA.b();
    }

    @MainThread
    @Keep
    public void onRemoteControllerSignalStrengthChange(@IntRange(from = 0L,to = 100L) int var1) {
        int var2;
        if(var1 <= 0) {
            var2 = R.drawable.ic_topbar_signal_level_0;
        } else if(var1 > 0 && var1 <= 20) {
            var2 = R.drawable.ic_topbar_signal_level_1;
        } else if(var1 > 20 && var1 <= 40) {
            var2 = R.drawable.ic_topbar_signal_level_2;
        } else if(var1 > 40 && var1 <= 60) {
            var2 = R.drawable.ic_topbar_signal_level_3;
        } else if(var1 > 60 && var1 <= 80) {
            var2 = R.drawable.ic_topbar_signal_level_4;
        } else {
            var2 = R.drawable.ic_topbar_signal_level_5;
        }

        this.rcSignal.setImageResource(var2);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.rcSignal = (ImageView)this.findViewById(R.id.imageview_rc_video_signal);
    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBA_G();
        }

        return this.widgetAppearances;
    }

    public void initKey() {
        this.ocuSignalQualityKey = AirLinkKey.createOcuSyncLinkKey("UplinkSignalQuality");
        this.wifiSignalQualityKey = AirLinkKey.createWiFiLinkKey("UplinkSignalQuality");
        this.lightbridgeSignalQualityKey = AirLinkKey.createLightbridgeLinkKey("UplinkSignalQuality");
        this.addDependentKey(this.ocuSignalQualityKey);
        this.addDependentKey(this.wifiSignalQualityKey);
        this.addDependentKey(this.lightbridgeSignalQualityKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.ocuSignalQualityKey) || var2.equals(this.wifiSignalQualityKey) || var2.equals(this.lightbridgeSignalQualityKey)) {
            this.signalValue = ((Integer)var1).intValue();
        }

    }

    public void updateWidget(DJIKey var1) {
        this.onRemoteControllerSignalStrengthChange(this.signalValue);
    }
}
