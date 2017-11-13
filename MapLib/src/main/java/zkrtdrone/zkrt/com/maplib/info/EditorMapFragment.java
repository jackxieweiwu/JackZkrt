package zkrtdrone.zkrt.com.maplib.info;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dji.common.flightcontroller.Attitude;
import zkrtdrone.zkrt.com.maplib.info.markers.MissionItemMarkerInfo;
import zkrtdrone.zkrt.com.maplib.info.markers.PolygonMarkerInfo;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.AutoPanMode;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.MissionItem;

/**
 * Created by jack_xie on 17-4-24.
 */

public class EditorMapFragment extends DroneMap implements DPmap.OnMapLongClickListener,
        DPmap.OnMarkerDragListener, DPmap.OnMapClickListener, DPmap.OnMarkerClickListener {
    private OnEditorInteraction editorListener;

    @Override
    public void onAttach(Context contexts) {
        super.onAttach(contexts);
        editorListener = mapApplication.getEditorListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        View view = super.onCreateView(inflater, viewGroup, bundle);
        mMapFragment.setOnMarkerDragListener(this);
        mMapFragment.setOnMarkerClickListener(this);
        mMapFragment.setOnMapClickListener(this);
        mMapFragment.setOnMapLongClickListener(this);
        return view;
    }

    @Override
    protected boolean isMissionDraggable() {
        return true;
    }

    @Override
    public boolean setAutoPanMode(AutoPanMode target) {
        if (target == AutoPanMode.DISABLED)
            return true;
        return false;
    }

    @Override
    public void onMapClick(LatLong coord) {
        if(coord !=null)
        editorListener.onMapClick(coord);
    }

    @Override
    public void onMapLongClick(LatLong coord) {

    }

    public void zoomToFit() {
        final List<LatLong> visibleCoords = missionProxy.getVisibleCoords();
        /*Home home = drone.getHome();
        if(home != null ) {
            final LatLong homeCoord = drone.getHome().getCoordinate();
            if (homeCoord != null && homeCoord.getLongitude() != 0 && homeCoord.getLatitude() != 0)
                visibleCoords.add(homeCoord);
        }*/
        zoomToFit(visibleCoords);
    }

    public void zoomToFit(List<LatLong> itemsToFit){
        if(!itemsToFit.isEmpty()){
            mMapFragment.zoomToFit(itemsToFit);
        }
    }

    @Override
    public boolean onMarkerClick(MarkerInfo markerInfo) {
        if (markerInfo instanceof MissionItemMarkerInfo) {
            editorListener.onItemClick(((MissionItemMarkerInfo) markerInfo).getMarkerOrigin(), false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onMarkerDrag(MarkerInfo markerInfo) {
        checkForWaypointMarkerMoving(markerInfo);
    }

    @Override
    public void onMarkerDragEnd(MarkerInfo markerInfo) {
        checkForWaypointMarker(markerInfo);
    }

    @Override
    public void onMarkerDragStart(MarkerInfo markerInfo) {
        checkForWaypointMarkerMoving(markerInfo);
    }

    private void checkForWaypointMarkerMoving(MarkerInfo markerInfo) {
        if (markerInfo instanceof MissionItem.SpatialItem) {
            LatLong position = markerInfo.getPosition();

            // update marker source
            MissionItem.SpatialItem waypoint = (MissionItem.SpatialItem) markerInfo;
            LatLong waypointPosition = waypoint.getCoordinate();
            waypointPosition.setLatitude(position.getLatitude());
            waypointPosition.setLongitude(position.getLongitude());

            // update flight path
            mMapFragment.updateMissionPath(missionProxy);
        }
    }

    private void checkForWaypointMarker(MarkerInfo markerInfo) {
        if ((markerInfo instanceof MissionItemMarkerInfo)) {
            missionProxy.move(((MissionItemMarkerInfo) markerInfo).getMarkerOrigin(),
                    markerInfo.getPosition());
        }else if ((markerInfo instanceof PolygonMarkerInfo)) {
            PolygonMarkerInfo marker = (PolygonMarkerInfo) markerInfo;
            missionProxy.movePolygonPoint(marker.getSurvey(), marker.getIndex(), markerInfo.getPosition());
        }
    }

    public void setDroneBitmap(Attitude attitude) {
        if(attitude == null ) return;
        if(mMapFragment ==null) return;
        mMapFragment.setDroneMap(attitude);
    }
}