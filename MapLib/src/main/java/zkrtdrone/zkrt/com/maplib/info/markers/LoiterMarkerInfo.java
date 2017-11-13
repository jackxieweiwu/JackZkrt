package zkrtdrone.zkrt.com.maplib.info.markers;

import zkrtdrone.zkrt.com.maplib.R;
import zkrtdrone.zkrt.com.maplib.info.mission.MissionItemProxy;

/**
 * jack_xie
 */
class LoiterMarkerInfo extends MissionItemMarkerInfo {
	protected LoiterMarkerInfo(MissionItemProxy origin) {
		super(origin);
	}

	@Override
	protected int getSelectedIconResource() {
		return R.drawable.ic_wp_loiter_selected;
	}

	@Override
	protected int getIconResource() {
		return R.drawable.ic_wp_circle_cw;
	}
}
