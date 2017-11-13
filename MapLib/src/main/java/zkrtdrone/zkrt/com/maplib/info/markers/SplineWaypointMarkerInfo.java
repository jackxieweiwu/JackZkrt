package zkrtdrone.zkrt.com.maplib.info.markers;


import zkrtdrone.zkrt.com.maplib.R;
import zkrtdrone.zkrt.com.maplib.info.mission.MissionItemProxy;

/**
 * jack_xie
 * TODO: update this marker info's icons.
 */
public class SplineWaypointMarkerInfo extends MissionItemMarkerInfo {

	protected SplineWaypointMarkerInfo(MissionItemProxy origin) {
		super(origin);
	}

	@Override
	protected int getSelectedIconResource() {
		return R.drawable.ic_spline_wp_map_selected;
	}

	@Override
	protected int getIconResource() {
		return R.drawable.ic_spline_wp_map;
	}
}
