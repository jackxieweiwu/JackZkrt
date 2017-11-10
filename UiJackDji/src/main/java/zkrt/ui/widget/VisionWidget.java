package zkrt.ui.widget;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.MainThread;
import android.util.AttributeSet;
import android.widget.ImageView;

import dji.keysdk.DJIKey;
import dji.keysdk.FlightControllerKey;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseGViewDULFrameLayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBA_N;
import zkrt.ui.d.UiDA;

/**
 * Created by jack_xie on 17-6-1.
 */

public class VisionWidget extends UiBaseGViewDULFrameLayout {
    private UiCAC widgetAppearances;
    private ImageView visionIcon;
    private DJIKey visionUsedKey;
    private DJIKey visionSensorEnableKey;
    private boolean isVisionWorking = false;
    private boolean visionIsUsing;
    private boolean visionSensorEnabled;

    public VisionWidget(Context var1, AttributeSet var2) {
        super(var1, var2, 0);
        UiDA.b();
    }

    @MainThread
    @Keep
    public void onIsVisionUsedChange(boolean var1) {
        int var2;
        if(var1) {
            var2 = R.drawable.visual_enable;
        } else {
            var2 = R.drawable.visual;
        }

        this.visionIcon.setImageResource(var2);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.visionIcon = (ImageView)this.findViewById(R.id.imageview_signal_icon);
        this.visionIcon.setImageResource(R.drawable.visual);
    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBA_N();
        }

        return this.widgetAppearances;
    }

    public void initKey() {
        this.visionUsedKey = FlightControllerKey.create("IsVisionPositioningSensorBeingUsed");
        this.addDependentKey(this.visionUsedKey);
        this.visionSensorEnableKey = FlightControllerKey.createFlightAssistantKey("VisionPositioningEnabled");
        this.addDependentKey(this.visionSensorEnableKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.visionUsedKey)) {
            this.visionIsUsing = ((Boolean)var1).booleanValue();
        } else if(var2.equals(this.visionSensorEnableKey)) {
            this.visionSensorEnabled = ((Boolean)var1).booleanValue();
        }

        this.isVisionWorking = this.visionSensorEnabled && this.visionIsUsing;
    }

    public void updateWidget(DJIKey var1) {
        this.onIsVisionUsedChange(this.isVisionWorking);
    }
}
