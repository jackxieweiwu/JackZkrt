package zkrt.ui.widget.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ImageView;

import dji.keysdk.DJIKey;
import dji.keysdk.FlightControllerKey;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseJView;
import zkrt.ui.base.UiBaseKView;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBA_L;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDG;
import zkrt.ui.d.UiDH;

/**
 * Created by jack_xie on 17-6-1.
 */

public class VerticalVelocityWidget extends UiBaseKView {
    private static final String TAG = "VerticalVelocityWidget";
    private DJIKey aircraftVelocityZKey;
    private ImageView speedDirection;
    private int visibility;
    private int resource;

    public VerticalVelocityWidget(Context var1) {
        this(var1, null, 0);
    }

    public VerticalVelocityWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public VerticalVelocityWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.setDecimalFormat("#0.0");
        this.setWidgetStyle(UiBaseJView.b);
        UiDA.b();
    }

    protected String getTitle() {
        Resources var1 = this.getResources();
        return var1.getString(R.string.vertical_speed_title);
    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBA_L();
        }

        return this.widgetAppearances;
    }

    protected String getUnitString() {
        return UiDG.b(value_Unit_Type).toUpperCase();
    }

    protected double getValueFromMetricValue() {
        return (double) UiDG.b(this.metricValue, value_Unit_Type);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.setMetricMaxValue(20.0F);
        this.setMetricMinValue(-20.0F);
    }

    public void initKey() {
        this.aircraftVelocityZKey = FlightControllerKey.create("VelocityZ");
        this.addDependentKey(this.aircraftVelocityZKey);
    }

    private void updateVerticalSpeed(float var1) {
        this.metricValue = var1;
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.aircraftVelocityZKey)) {
            this.updateVerticalSpeed(((Float)var1).floatValue());
        }

    }
}
