package zkrtdrone.zkrt.com.maplib.info.markers;


import java.util.ArrayList;
import java.util.List;

import zkrtdrone.zkrt.com.maplib.info.MarkerInfo;
import zkrtdrone.zkrt.com.maplib.info.mission.MissionItemProxy;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.Survey;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;

/**
 *jack_xie
 */
public class SurveyMarkerInfoProvider {

	private final Survey mSurvey;
	private final List<MarkerInfo> mPolygonMarkers = new ArrayList<MarkerInfo>();

	protected SurveyMarkerInfoProvider(MissionItemProxy origin) {
		mSurvey = (Survey) origin.getMissionItem();
		updateMarkerInfoList();
	}

	private void updateMarkerInfoList() {
        List<LatLong> points = mSurvey.getPolygonPoints();
        if(points != null) {
            final int pointsCount = points.size();
            for (int i = 0; i < pointsCount; i++) {
                mPolygonMarkers.add(new PolygonMarkerInfo(points.get(i), mSurvey, i));
            }
        }
	}

	public List<MarkerInfo> getMarkersInfos() {
		return mPolygonMarkers;
	}
}
