package zkrtdrone.zkrt.com.maplib.info.mission;

import java.util.ArrayList;
import java.util.List;

import zkrtdrone.zkrt.com.maplib.info.MarkerInfo;
import zkrtdrone.zkrt.com.maplib.info.markers.MissionItemMarkerInfo;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.Survey;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.spatil.Circle;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.MissionItem;
import zkrtdrone.zkrt.com.maplib.info.units.MathUtils;

/**
 * Created by jack_xie on 17-8-28.
 */

public class MissionItemProxy {
    /**
     * This is the mission item object this class is built around.
     */
    private final MissionItem mMissionItem;

    /**
     * This is the mission render to which this item belongs.
     */
    private final MissionProxy mMission;

    /**
     * This is the marker source for this mission item render.
     */
    private final List<MarkerInfo> mMarkerInfos;

    public MissionItemProxy(MissionProxy mission, MissionItem missionItem) {
        mMission = mission;
        mMissionItem = missionItem;
        mMarkerInfos = MissionItemMarkerInfo.newInstance(this);

        if(mMissionItem instanceof Survey){
            //mMission.getDrone().buildComplexMissionItem((Survey) mMissionItem);
        }
        /*else if(mMissionItem instanceof StructureScanner){
            mMission.getDrone().buildComplexMissionItem((StructureScanner) mMissionItem);
        }*/
    }

    /**
     * Provides access to the owning mission render instance.
     *
     * @return
     */
    public MissionProxy getMissionProxy() {
        return mMission;
    }

    public MissionProxy getMission(){return mMission;}

    /**
     * Provides access to the mission item instance.
     *
     */
    public MissionItem getMissionItem() {
        return mMissionItem;
    }


    public List<MarkerInfo> getMarkerInfos() {
        return mMarkerInfos;
    }

    /**
     * @param previousPoint
     *            Previous point on the path, null if there wasn't a previous
     *            point
     * @return the set of points/coords making up this mission item.
     */
    public List<LatLong> getPath(LatLong previousPoint) {
        List<LatLong> pathPoints = new ArrayList<LatLong>();
        switch (mMissionItem.getType()) {
            case LAND:
            case WAYPOINT:
            case SPLINE_WAYPOINT:
                pathPoints.add(((MissionItem.SpatialItem) mMissionItem).getCoordinate());
                break;

            case CIRCLE:
                for (int i = 0; i <= 360; i += 10) {
                    Circle circle = (Circle) mMissionItem;
                    double startHeading = 0;
                    if (previousPoint != null) {
                        startHeading = MathUtils.getHeadingFromCoordinates(circle.getCoordinate(),
                                previousPoint);
                    }
                    pathPoints.add(MathUtils.newCoordFromBearingAndDistance(circle.getCoordinate(),
                            startHeading + i, circle.getRadius()));
                }
                break;

            case SURVEY:
                List<LatLong> gridPoints = ((Survey)mMissionItem).getGridPoints();
                if (gridPoints != null && !gridPoints.isEmpty()) {
                    pathPoints.addAll(gridPoints);
                }
                break;


            default:
                break;
        }

        return pathPoints;
    }
}
