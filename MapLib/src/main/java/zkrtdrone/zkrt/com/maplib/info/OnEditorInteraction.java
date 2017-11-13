package zkrtdrone.zkrt.com.maplib.info;

import zkrtdrone.zkrt.com.maplib.info.mission.MissionItemProxy;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;

/**
 * Created by jack_xie on 17-4-24.
 */

public interface OnEditorInteraction {
    boolean onItemLongClick(MissionItemProxy item);

    void onItemClick(MissionItemProxy item, boolean zoomToFit);

    void onMapClick(LatLong coord);

    void onListVisibilityChanged();
}
