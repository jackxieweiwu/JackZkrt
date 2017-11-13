package zkrtdrone.zkrt.com.maplib.info.markers;


import zkrtdrone.zkrt.com.maplib.R;
import zkrtdrone.zkrt.com.maplib.info.mission.MissionItemProxy;

/**
 * This implements the marker source for a takeoff mission item.
 */
class TakeoffMarkerInfo extends MissionItemMarkerInfo {

	protected TakeoffMarkerInfo(MissionItemProxy origin) {
		super(origin);
	}

	@Override
	protected int getSelectedIconResource() {
		return R.drawable.ic_wp_takeof_selected;
	}

	@Override
	protected int getIconResource() {
		return R.drawable.ic_wp_takeof_selected;
	}
}
