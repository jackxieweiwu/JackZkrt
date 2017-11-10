package zkrt.ui.widget.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import dji.keysdk.DJIKey;
import dji.keysdk.FlightControllerKey;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseKView;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBA_L;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDG;

/**
 * Created by jack_xie on 17-6-1.
 */

public class HorizontalVelocityWidget extends UiBaseKView {
    private float speedX;
    private float speedY;
    private DJIKey aircraftVelocityXKey;
    private DJIKey aircraftVelocityYKey;

    public HorizontalVelocityWidget(Context var1) {
        this(var1, null, 0);
    }

    public HorizontalVelocityWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public HorizontalVelocityWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.speedX = 0.0F;
        this.speedY = 0.0F;
        this.setDecimalFormat("#0.0");
        UiDA.b();
    }

    protected String getTitle() {
        Resources var1 = this.getResources();
        return var1.getString(R.string.horizontal_speed_title);
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
        return (double)UiDG.b(this.metricValue, value_Unit_Type);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.setMetricMinValue(0.0F);
        this.setMetricMaxValue(50.0F);
    }

    public void initKey() {
        this.aircraftVelocityXKey = FlightControllerKey.create("VelocityX");
        this.aircraftVelocityYKey = FlightControllerKey.create("VelocityY");
        this.addDependentKey(this.aircraftVelocityXKey);
        this.addDependentKey(this.aircraftVelocityYKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.aircraftVelocityXKey)) {
            this.speedX = ((Float)var1).floatValue();
        } else if(var2.equals(this.aircraftVelocityYKey)) {
            this.speedY = ((Float)var1).floatValue();
        }

        this.metricValue = (float)Math.sqrt((double)(this.speedX * this.speedX + this.speedY * this.speedY));
    }
}
