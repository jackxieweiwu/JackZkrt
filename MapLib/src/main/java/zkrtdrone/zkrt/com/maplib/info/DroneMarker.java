package zkrtdrone.zkrt.com.maplib.info;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import dji.common.flightcontroller.Attitude;
import dji.common.flightcontroller.FlightControllerState;
import zkrtdrone.zkrt.com.maplib.R;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;

/**
 * Created by jack_xie on 17-5-11.
 */

public class DroneMarker extends MarkerInfo.SimpleMarkerInfo {

    private FlightControllerState flightControllerState;
    public DroneMarker(FlightControllerState drone) {
        this.flightControllerState = drone;
    }
    @Override
    public float getAnchorU() {
        return 0.5f;
    }

    @Override
    public float getAnchorV() {
        return 0.5f;
    }

    @Override
    public LatLong getPosition() {
        return flightControllerState == null ? null : new LatLong(flightControllerState.getAircraftLocation().getLatitude(),
                flightControllerState.getAircraftLocation().getLongitude());
    }

    @Override
    public Bitmap getIcon(Resources res) {
        return BitmapFactory.decodeResource(res, R.drawable.gs_follow_me_aircraft);
    }

    @Override
    public float getRotation() {
        Attitude attitude = flightControllerState.getAttitude();
        return attitude == null ? 0f : (float) attitude.yaw;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean isFlat() {
        return true;
    }
}
