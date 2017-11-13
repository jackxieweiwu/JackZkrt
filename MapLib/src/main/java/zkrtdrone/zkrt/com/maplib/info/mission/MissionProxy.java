package zkrtdrone.zkrt.com.maplib.info.mission;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zkrtdrone.zkrt.com.maplib.info.DPmap;
import zkrtdrone.zkrt.com.maplib.info.MarkerInfo;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.Survey;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.spatil.SplineWaypoint;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.spatil.Waypoint;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLongAlt;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.MissionItem;
import zkrtdrone.zkrt.com.maplib.info.units.MathUtils;

import static com.jack.frame.PublicStackNUm.droneAlt;


/**
 * Created by jack_xie on 17-8-28.
 */

public class MissionProxy implements DPmap.PathSource{
    public MissionProxy(Context context) {
    }

    public MissionSelection selection = new MissionSelection();

    private final List<MissionItemProxy> missionItemProxies = new ArrayList<MissionItemProxy>();

    public void addWaypoints(List<LatLong> points) {
        final double alt = droneAlt;
        final List<MissionItem> missionItemsToAdd = new ArrayList<MissionItem>(points.size());
        for (LatLong point : points) {
            Waypoint waypoint = new Waypoint();
            waypoint.setCoordinate(new LatLongAlt(point.getLatitude(), point.getLongitude(),(float) alt));
            missionItemsToAdd.add(waypoint);
        }
        addMissionItems(missionItemsToAdd);
    }

    public void move(MissionItemProxy item, LatLong position) {
        MissionItem missionItem = item.getMissionItem();
        if(missionItem instanceof MissionItem.SpatialItem){
            MissionItem.SpatialItem spatialItem = (MissionItem.SpatialItem) missionItem;
            spatialItem.setCoordinate(new LatLongAlt(position.getLatitude(),
                    position.getLongitude(), spatialItem.getCoordinate().getAltitude()));

           /* if(spatialItem instanceof StructureScanner)
                this.drone.buildComplexMissionItem((StructureScanner) spatialItem);*/
        }
    }

    public void movePolygonPoint(Survey survey, int index, LatLong position) {
        survey.getPolygonPoints().get(index).set(position);
        //this.drone.buildComplexMissionItem(survey);
    }

    public List<LatLong> getVisibleCoords() {
        return getVisibleCoords(missionItemProxies);
    }

    public static List<LatLong> getVisibleCoords(List<MissionItemProxy> mipList){
        final List<LatLong> coords = new ArrayList<LatLong>();

        if(mipList == null || mipList.isEmpty()){
            return coords;
        }

        for (MissionItemProxy itemProxy : mipList) {
            MissionItem item = itemProxy.getMissionItem();
            if (!(item instanceof MissionItem.SpatialItem))
                continue;
            final LatLong coordinate = ((MissionItem.SpatialItem) item).getCoordinate();
            if (coordinate.getLatitude() == 0 || coordinate.getLongitude() == 0)
                continue;
            coords.add(coordinate);
        }
        return coords;
    }
    public double getDistanceFromLastWaypoint(MissionItemProxy waypointRender) {
        if(missionItemProxies.size() < 2)
            return 0;

        MissionItem waypoint = waypointRender.getMissionItem();
        if (!(waypoint instanceof MissionItem.SpatialItem))
            return 0;

        final int index = missionItemProxies.indexOf(waypointRender);
        if(index == -1 || index == 0)
            return 0;

        MissionItem previous = missionItemProxies.get(index - 1).getMissionItem();
        if(previous instanceof MissionItem.SpatialItem){
            return MathUtils.getDistance(((MissionItem.SpatialItem) waypoint).getCoordinate(),
                    ((MissionItem.SpatialItem) previous).getCoordinate());
        }

        return 0;
    }


    public void addSplineWaypoint(LatLong point) {
        final double alt = droneAlt;
        final SplineWaypoint splineWaypoint = new SplineWaypoint();
        splineWaypoint.setCoordinate(new LatLongAlt(point.getLatitude(), point.getLongitude(),(float) alt));
        addMissionItem(splineWaypoint);
    }

    public List<MissionItemProxy> getItems() {
        return missionItemProxies;
    }

    public void addSplineWaypoints(List<LatLong> points) {
        final double alt = droneAlt;
        final List<MissionItem> missionItemsToAdd = new ArrayList<MissionItem>(points.size());
        for (LatLong point : points) {
            SplineWaypoint splineWaypoint = new SplineWaypoint();
            splineWaypoint.setCoordinate(new LatLongAlt(point.getLatitude(), point.getLongitude(),
                    (float) alt));
            missionItemsToAdd.add(splineWaypoint);
        }

        addMissionItems(missionItemsToAdd);
    }

    /**
     * Removes a waypoint mission item from the set of mission items commands.
     *
     * @param item
     *            item to remove
     */
    public void removeItem(MissionItemProxy item) {
        missionItemProxies.remove(item);
        selection.mSelectedItems.remove(item);
        selection.notifySelectionUpdate();
    }

    /**
     * Updates a mission item render
     *
     * @param oldItem
     *            mission item render to update
     * @param newItem
     *            new mission item render
     */
    public void replace(MissionItemProxy oldItem, MissionItemProxy newItem) {
        final int index = missionItemProxies.indexOf(oldItem);
        if (index == -1)
            return;

        missionItemProxies.remove(index);
        missionItemProxies.add(index, newItem);

        if (selection.selectionContains(oldItem)) {
            selection.removeItemFromSelection(oldItem);
            selection.addToSelection(newItem);
        }
    }

    public void replaceAll(List<Pair<MissionItemProxy, MissionItemProxy>> oldNewList){
        if(oldNewList == null){
            return;
        }

        final int pairSize = oldNewList.size();
        if(pairSize == 0){
            return;
        }

        final List<MissionItemProxy> selectionsToRemove = new ArrayList<MissionItemProxy>(pairSize);
        final List<MissionItemProxy> itemsToSelect = new ArrayList<MissionItemProxy>(pairSize);

        for(int i = 0; i < pairSize; i++){
            final MissionItemProxy oldItem = oldNewList.get(i).first;
            final int index = missionItemProxies.indexOf(oldItem);
            if(index == -1){
                continue;
            }

            final MissionItemProxy newItem = oldNewList.get(i).second;
            missionItemProxies.remove(index);
            missionItemProxies.add(index, newItem);

            if(selection.selectionContains(oldItem)){
                selectionsToRemove.add(oldItem);
                itemsToSelect.add(newItem);
            }
        }

        //Update the selection list.
        selection.removeItemsFromSelection(selectionsToRemove);
        selection.addToSelection(itemsToSelect);
    }

    public double getAltitudeDiffFromPreviousItem(MissionItemProxy waypointRender) {
        final int itemsCount = missionItemProxies.size();
        if(itemsCount < 2)
            return 0;

        MissionItem waypoint = waypointRender.getMissionItem();
        if (!(waypoint instanceof MissionItem.SpatialItem))
            return 0;

        final int index = missionItemProxies.indexOf(waypointRender);
        if(index == -1 || index == 0)
            return 0;

        MissionItem previous = missionItemProxies.get(index - 1).getMissionItem();
        if (previous instanceof MissionItem.SpatialItem) {
            return ((MissionItem.SpatialItem)waypoint).getCoordinate().getAltitude()
                    - ((MissionItem.SpatialItem) previous).getCoordinate().getAltitude();
        }

        return 0;
    }

    /**
     * Returns the order for the given argument in the mission set.
     *
     * @param item
     * @return order of the given argument
     */
    public int getOrder(MissionItemProxy item) {
        return missionItemProxies.indexOf(item) + 1;
    }


    public void removeSelection(MissionSelection missionSelection) {
        missionItemProxies.removeAll(missionSelection.mSelectedItems);
        missionSelection.clearSelection();
    }

    public void clear() {
        selection.clearSelection();
        missionItemProxies.clear();
    }

    /**
     * Adds a survey mission item to the set.
     *
     * @param points
     *            2D points making up the survey
     */
    public void addSurveyPolygon(List<LatLong> points) {
        Survey survey = new Survey();
        survey.setPolygonPoints(points);
        addMissionItem(survey);
    }

    public List<MarkerInfo> getMarkersInfos() {
        List<MarkerInfo> markerInfos = new ArrayList<MarkerInfo>();

        for (MissionItemProxy itemProxy : missionItemProxies) {
            List<MarkerInfo> itemMarkerInfos = itemProxy.getMarkerInfos();
            if (itemMarkerInfos != null && !itemMarkerInfos.isEmpty()) {
                markerInfos.addAll(itemMarkerInfos);
            }
        }
        return markerInfos;
    }


    public List<List<LatLong>> getPolygonsPath() {
        ArrayList<List<LatLong>> polygonPaths = new ArrayList<List<LatLong>>();
        for (MissionItemProxy itemProxy : missionItemProxies) {
            MissionItem item = itemProxy.getMissionItem();
            if (item instanceof Survey) {
                polygonPaths.add(((Survey)item).getPolygonPoints());
            }
        }
        return polygonPaths;
    }

    public void addWaypoint(LatLong point) {
        final double alt = droneAlt;
        final Waypoint waypoint = new Waypoint();
        waypoint.setCoordinate(new LatLongAlt(point.getLatitude(), point.getLongitude(),(float) alt));
        addMissionItem(waypoint);
    }

    private void addMissionItem(MissionItem missionItem) {
        missionItemProxies.add(new MissionItemProxy(this, missionItem));
    }

    private void addMissionItems(List<MissionItem> missionItems) {
        for (MissionItem missionItem : missionItems) {
            missionItemProxies.add(new MissionItemProxy(this, missionItem));
        }
    }

    @Override
    public List<LatLong> getPathPoints() {
        if (missionItemProxies.isEmpty()) {
            return Collections.emptyList();
        }

        // Partition the mission items into spline/non-spline buckets.
        final List<Pair<Boolean, List<MissionItemProxy>>> bucketsList = new ArrayList<Pair<Boolean, List<MissionItemProxy>>>();

        boolean isSpline = false;
        List<MissionItemProxy> currentBucket = new ArrayList<MissionItemProxy>();
        for (MissionItemProxy missionItemProxy : missionItemProxies) {

            MissionItem missionItem = missionItemProxy.getMissionItem();
            if(missionItem instanceof MissionItem.Command){
                //Skip commands
                continue;
            }

            if (missionItem instanceof SplineWaypoint) {
                if (!isSpline) {
                    if (!currentBucket.isEmpty()) {
                        final MissionItemProxy lastItem = currentBucket
                                .get(currentBucket.size() - 1);

                        bucketsList.add(new Pair<Boolean, List<MissionItemProxy>>(Boolean.FALSE,
                                currentBucket));

                        currentBucket = new ArrayList<MissionItemProxy>();
                        currentBucket.add(lastItem);
                    }

                    isSpline = true;
                }

                currentBucket.add(missionItemProxy);
            } else {
                if (isSpline) {
                    if (!currentBucket.isEmpty()) {
                        currentBucket.add(missionItemProxy);

                        bucketsList.add(new Pair<Boolean, List<MissionItemProxy>>(Boolean.TRUE,
                                currentBucket));

                        currentBucket = new ArrayList<MissionItemProxy>();
                    }

                    isSpline = false;
                }

                currentBucket.add(missionItemProxy);
            }
        }

        bucketsList.add(new Pair<Boolean, List<MissionItemProxy>>(isSpline, currentBucket));

        final List<LatLong> pathPoints = new ArrayList<LatLong>();
        LatLong lastPoint = null;

        for (Pair<Boolean, List<MissionItemProxy>> bucketEntry : bucketsList) {

            final List<MissionItemProxy> bucket = bucketEntry.second;
            if (bucketEntry.first) {
                final List<LatLong> splinePoints = new ArrayList<LatLong>();
                for (MissionItemProxy missionItemProxy : bucket) {
                    splinePoints.addAll(missionItemProxy.getPath(lastPoint));

                    if (!splinePoints.isEmpty()) {
                        lastPoint = splinePoints.get(splinePoints.size() - 1);
                    }
                }

                pathPoints.addAll(MathUtils.SplinePath.process(splinePoints));
            } else {
                for (MissionItemProxy missionItemProxy : bucket) {
                    pathPoints.addAll(missionItemProxy.getPath(lastPoint));

                    if (!pathPoints.isEmpty()) {
                        lastPoint = pathPoints.get(pathPoints.size() - 1);
                    }
                }
            }
        }

        return pathPoints;
    }
}
