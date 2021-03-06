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
import zkrt.ui.c.b.UiCBA_N;
import zkrt.ui.c.b.UiCBA_O;
import zkrt.ui.d.UiDA;

/**
 * Created by jack_xie on 17-6-1.
 */

public class WiFiSignalWidget extends UiBaseGViewDULFrameLayout {
    private int currentWifiSignalResId;
    private ImageView wifiSignal;
    private UiCBA_O widgetAppearances;
    private DJIKey wifiSignalKey;
    private int signalValue;

    public WiFiSignalWidget(Context var1, AttributeSet var2) {
        super(var1, var2, 0);
        UiDA.b();
    }

    @MainThread
    @Keep
    public void onWifiSignalChange(@IntRange(from = 0L,to = 100L) int var1) {
        if(var1 <= 0) {
            this.currentWifiSignalResId = R.drawable.ic_topbar_wifi_level_1;
        } else if(var1 > 75) {
            this.currentWifiSignalResId = R.drawable.ic_topbar_wifi_level_5;
        } else if(var1 > 0 && var1 <= 25) {
            this.currentWifiSignalResId = R.drawable.ic_topbar_wifi_level_2;
        } else if(var1 > 25 && var1 <= 50) {
            this.currentWifiSignalResId = R.drawable.ic_topbar_wifi_level_3;
        } else if(var1 > 50 && var1 <= 75) {
            this.currentWifiSignalResId = R.drawable.ic_topbar_wifi_level_4;
        }

        this.wifiSignal.setImageResource(this.currentWifiSignalResId);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.wifiSignal = (ImageView)this.findViewById(R.id.imageview_signal_icon);
        this.wifiSignal.setImageResource(R.drawable.ic_topbar_wifi_level_1);
    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBA_O();
        }

        return this.widgetAppearances;
    }

    public void initKey() {
        this.wifiSignalKey = AirLinkKey.createWiFiLinkKey("DownlinkSignalQuality");
        this.addDependentKey(this.wifiSignalKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.wifiSignalKey)) {
            this.signalValue = ((Integer)var1).intValue();
        }

    }

    public void updateWidget(DJIKey var1) {
        this.onWifiSignalChange(this.signalValue);
    }
}
