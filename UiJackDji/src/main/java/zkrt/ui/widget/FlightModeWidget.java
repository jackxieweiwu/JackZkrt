package zkrt.ui.widget;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import dji.keysdk.DJIKey;
import dji.keysdk.FlightControllerKey;
import dji.log.DJILog;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseGViewDULFrameLayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBR;
import zkrt.ui.d.UiDA;

/**
 * Created by jack_xie on 17-6-1.
 */

public class FlightModeWidget extends UiBaseGViewDULFrameLayout {
    private final String TAG;
    private UiCBR widgetAppearances;
    private TextView valueTextView;
    private String flightModeString;
    private FlightControllerKey flightModeStringKey;

    public FlightModeWidget(Context var1) {
        this(var1, null, 0);
    }

    public FlightModeWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public FlightModeWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.TAG = "FlyingModeWidget";
        UiDA.b();
    }

    @MainThread
    @Keep
    public void onFlyControllerModeChange(@Nullable String var1) {
        this.valueTextView.setText(var1);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.valueTextView = (TextView)this.findViewById(R.id.textview_flight_mode_string);
        int var4 = R.string.string_default_value;
        if(this.valueTextView != null) {
            String var5 = (String)this.getContext().getResources().getText(var4);
            this.valueTextView.setText(var5);
        }

    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBR();
        }
        return this.widgetAppearances;
    }

    public void initKey() {
        this.flightModeStringKey = FlightControllerKey.create("FlightModeString");
        this.addDependentKey(this.flightModeStringKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.flightModeStringKey)) {
            this.flightModeString = (String)var1;
            DJILog.d("LWF", "FlightMode is " + this.flightModeString);
        }

    }

    public void updateWidget(DJIKey var1) {
        this.onFlyControllerModeChange(this.flightModeString);
    }
}
