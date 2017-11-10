package zkrt.ui.widget.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import dji.keysdk.DJIKey;
import dji.keysdk.FlightControllerKey;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseHView;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDG;

/**
 * Created by jack_xie on 17-6-1.
 */

public class AltitudeWidget extends UiBaseHView {
    private DJIKey aircraftAltitudeKey;

    public AltitudeWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public AltitudeWidget(Context var1) {
        this(var1, (AttributeSet)null, 0);
    }

    public AltitudeWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        UiDA.b();
    }

    protected String getTitle() {
        Resources var1 = this.getResources();
        return var1.getString(R.string.altitude_title);
    }

    protected String getUnitString() {
        return UiDG.a(value_Unit_Type).toUpperCase();
    }

    protected double getValueFromMetricValue() {
        return (double)UiDG.a(this.metricValue, value_Unit_Type);
    }

    public void initKey() {
        this.aircraftAltitudeKey = FlightControllerKey.create("Altitude");
        this.addDependentKey(this.aircraftAltitudeKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.aircraftAltitudeKey)) {
            this.metricValue = ((Float)var1).floatValue();
        }

    }
}
