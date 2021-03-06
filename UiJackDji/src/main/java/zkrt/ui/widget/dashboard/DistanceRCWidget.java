package zkrt.ui.widget.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;

import dji.common.remotecontroller.GPSData;
import dji.keysdk.DJIKey;
import dji.keysdk.FlightControllerKey;
import dji.keysdk.RemoteControllerKey;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseHView;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDE;
import zkrt.ui.d.UiDF;
import zkrt.ui.d.UiDG;
import zkrt.ui.d.UiDH;

/**
 * Created by jack_xie on 17-6-1.
 */

public class DistanceRCWidget extends UiBaseHView {
    private double aircraftLatitude;
    private double aircraftLongitude;
    private double rcLatitude;
    private double rcLongitude;
    private DJIKey aircraftLatitudeKey;
    private DJIKey aircraftLongitudeKey;
    private DJIKey rcGPSDataKey;

    public DistanceRCWidget(Context var1) {
        this(var1, null, 0);
    }

    public DistanceRCWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public DistanceRCWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.aircraftLatitude = 0.0D;
        this.aircraftLongitude = 0.0D;
        this.rcLatitude = 0.0D;
        this.rcLongitude = 0.0D;
        this.setDecimalFormat("###0.0");
        UiDA.b();
    }

    protected String getTitle() {
        Resources var1 = this.getResources();
        return var1.getString(R.string.distance_to_rc_title);
    }

    protected String getUnitString() {
        return UiDG.a(value_Unit_Type);
    }

    protected double getValueFromMetricValue() {
        return (double) UiDG.a(this.metricValue, value_Unit_Type);
    }

    protected void onDraw(Canvas var1) {
        super.onDraw(var1);
    }

    public void initKey() {
        this.aircraftLatitudeKey = FlightControllerKey.create("AircraftLocationLatitude");
        this.aircraftLongitudeKey = FlightControllerKey.create("AircraftLocationLongitude");
        this.rcGPSDataKey = RemoteControllerKey.create("GPSData");
        this.addDependentKey(this.aircraftLatitudeKey);
        this.addDependentKey(this.aircraftLongitudeKey);
        this.addDependentKey(this.rcGPSDataKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.aircraftLatitudeKey)) {
            this.aircraftLatitude = ((Double)var1).doubleValue();
        } else if(var2.equals(this.aircraftLongitudeKey)) {
            this.aircraftLongitude = ((Double)var1).doubleValue();
        } else if(var2.equals(this.rcGPSDataKey)) {
            GPSData var3 = (GPSData)var1;
            if(var3 != null) {
                this.rcLatitude = var3.getLocation().getLatitude();
                this.rcLongitude = var3.getLocation().getLongitude();
            }
        }

        if(this.aircraftLatitude != 0.0D && this.aircraftLongitude != 0.0D && this.rcLatitude != 0.0D && this.rcLongitude != 0.0D) {
            this.metricValue = UiDE.a(this.rcLatitude, this.rcLongitude, this.aircraftLatitude, this.aircraftLongitude);
        }

    }
}
