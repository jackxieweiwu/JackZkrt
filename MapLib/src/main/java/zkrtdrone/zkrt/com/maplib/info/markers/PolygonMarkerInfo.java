package zkrtdrone.zkrt.com.maplib.info.markers;


import zkrtdrone.zkrt.com.maplib.info.MarkerInfo;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.Survey;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;

/**
 * jack_xie
 */
public class PolygonMarkerInfo extends MarkerInfo.SimpleMarkerInfo {

	private LatLong mPoint;
    private final Survey survey;
    private final int polygonIndex;

	public PolygonMarkerInfo(LatLong point, Survey mSurvey, int index) {
		mPoint = point;
		survey = mSurvey;
		polygonIndex = index;
	}
	
	public Survey getSurvey(){
		return survey;
	}

	public int getIndex(){
		return polygonIndex;
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
		return mPoint;
	}

	@Override
	public void setPosition(LatLong coord) {
		mPoint = coord;
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
