package zkrt.ui.widget.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import dji.keysdk.DJIKey;
import dji.keysdk.FlightControllerKey;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseHView;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDE;
import zkrt.ui.d.UiDG;

/**
 * Created by jack_xie on 17-6-1.
 */

public class DistanceHomeWidget extends UiBaseHView {
    private double aircraftLatitude;
    private double aircraftLongitude;
    private double homeLatitude;
    private double homeLongitude;
    private DJIKey aircraftLatitudeKey;
    private DJIKey aircraftLongitudeKey;
    private FlightControllerKey homeLatitudeKey;
    private FlightControllerKey homeLongitudeKey;

    public DistanceHomeWidget(Context var1) {
        this(var1, null, 0);
    }

    public DistanceHomeWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public DistanceHomeWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.aircraftLatitude = 0.0D;
        this.aircraftLongitude = 0.0D;
        this.homeLatitude = 0.0D;
        this.homeLongitude = 0.0D;
        this.setDecimalFormat("###0.0");
        UiDA.b();
    }

    protected String getTitle() {
        Resources var1 = this.getResources();
        return var1.getString(R.string.distance_to_home_title);
    }

    protected String getUnitString() {
        return UiDG.a(value_Unit_Type).toUpperCase();
    }

    protected double getValueFromMetricValue() {
        return (double) UiDG.a(this.metricValue, value_Unit_Type);
    }

    public void initKey() {
        this.aircraftLatitudeKey = FlightControllerKey.create("AircraftLocationLatitude");
        this.aircraftLongitudeKey = FlightControllerKey.create("AircraftLocationLongitude");
        this.homeLatitudeKey = FlightControllerKey.create("HomeLocationLatitude");
        this.homeLongitudeKey = FlightControllerKey.create("HomeLocationLongitude");
        this.addDependentKey(this.aircraftLatitudeKey);
        this.addDependentKey(this.aircraftLongitudeKey);
        this.addDependentKey(this.homeLatitudeKey);
        this.addDependentKey(this.homeLongitudeKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.aircraftLatitudeKey)) {
            this.aircraftLatitude = ((Double)var1).doubleValue();
        } else if(var2.equals(this.aircraftLongitudeKey)) {
            this.aircraftLongitude = ((Double)var1).doubleValue();
        } else if(var2.equals(this.homeLatitudeKey)) {
            this.homeLatitude = ((Double)var1).doubleValue();
        } else if(var2.equals(this.homeLongitudeKey)) {
            this.homeLongitude = ((Double)var1).doubleValue();
        }

        if(this.aircraftLatitude != 0.0D && this.aircraftLongitude != 0.0D && this.homeLatitude != 0.0D && this.homeLongitude != 0.0D) {
            this.metricValue = UiDE.a(this.homeLatitude, this.homeLongitude, this.aircraftLatitude, this.aircraftLongitude);
        }

    }
}
