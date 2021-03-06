package zkrtdrone.zkrt.com.maplib.info;

import android.graphics.Color;

import com.baidu.mapapi.map.MapStatusUpdate;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import dji.common.flightcontroller.Attitude;
import zkrtdrone.zkrt.com.maplib.info.location.Location;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.AutoPanMode;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.FootPrint;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;
import zkrtdrone.zkrt.com.maplib.info.providers.DPMapProvider;

/**
 * Created by jack_xie on 17-4-24.
 * Defines the functionality expected from the map providers.
 */

public interface DPmap {
    public static final String PACKAGE_NAME = DPmap.class.getPackage().getName();
    public static final String EXTRA_MAX_FLIGHT_PATH_SIZE = PACKAGE_NAME + ""
            + ".EXTRA_MAX_FLIGHT_PATH_SIZE";
    public static final int FLIGHT_PATH_DEFAULT_COLOR = 0xfffd693f;
    public static final int FLIGHT_PATH_DEFAULT_WIDTH = 6;

    public static final int MISSION_PATH_DEFAULT_COLOR = Color.WHITE;
    public static final int MISSION_PATH_DEFAULT_WIDTH = 4;

    public static final int DRONE_LEASH_DEFAULT_COLOR = Color.WHITE;
    public static final int DRONE_LEASH_DEFAULT_WIDTH = 2;

    public static final int POLYGONS_PATH_DEFAULT_COLOR = Color.RED;
    public static final int POLYGONS_PATH_DEFAULT_WIDTH = 4;

    public static final int FOOTPRINT_DEFAULT_COLOR = 0;
    public static final int FOOTPRINT_DEFAULT_WIDTH = 2;
    public static final int FOOTPRINT_FILL_COLOR = Color.argb(80, 0, 0, 200);


    void setDroneMap(Attitude attitude);

    interface PathSource {
        public List<LatLong> getPathPoints();
    }

    interface OnMapClickListener {
        /**
         * Triggered when the map is clicked.
         * @param coord
         *            location where the map was clicked.
         */
        void onMapClick(LatLong coord);
    }

    /**
     * Implemented by classes interested in map long click events.
     */
    interface OnMapLongClickListener {
        /**
         * Triggered when the map is long clicked.
         *
         * @param coord
         *            location where the map was long clicked.
         */
        void onMapLongClick(LatLong coord);
    }

    /**
     * Implemented by classes interested in marker(s) click events.
     */
    interface OnMarkerClickListener {
        /**
         * Triggered when a marker is clicked.
         *
         * @param markerInfo
         *            info about the clicked marker
         * @return true if the listener has consumed the event.
         */
        boolean onMarkerClick(MarkerInfo markerInfo);
    }

    /**
     * Callback interface for drag events on markers.
     */
    interface OnMarkerDragListener {
        /**
         * Called repeatedly while a marker is being dragged. The marker's
         * location can be accessed via {@link MarkerInfo#getPosition()}
         *
         * @param markerInfo
         *            info about the marker that was dragged.
         */
        void onMarkerDrag(MarkerInfo markerInfo);

        /**
         * Called when a marker has finished being dragged. The marker's
         * location can be accessed via {@link MarkerInfo#getPosition()}
         *
         * @param markerInfo
         *            info about the marker that was dragged.
         */
        void onMarkerDragEnd(MarkerInfo markerInfo);

        /**
         * Called when a marker starts being dragged. The marker's location can
         * be accessed via {@link MarkerInfo#getPosition()}; this position may
         * be different to the position prior to the start of the drag because
         * the marker is popped up above the touch point.
         *
         * @param markerInfo
         *            info about the marker that was dragged.
         */
        void onMarkerDragStart(MarkerInfo markerInfo);
    }

    /**
     * Draw the footprint of the camera in the ground
     * @param footprintToBeDraw
     */
    public void addCameraFootprint(FootPrint footprintToBeDraw);

    /**
     * Remove all markers from the map.
     */
    public void clearMarkers();

    /**
     * Clears the drone's flight path.
     */
    public void clearFlightPath();

    public void selectAutoPanMode(AutoPanMode mode);

    /**
     * @return the map center coordinates.
     */
    public LatLong getMapCenter();

    /**
     * @return the map current zoom level.
     */
    public float getMapZoomLevel();

    /**
     * @return a list of marker info currently on the map.
     */
    public Set<MarkerInfo> getMarkerInfoList();

    /**
     * @return the map maximum zoom level.
     */
    public float getMaxZoomLevel();

    /**
     * @return the map minimum zoom level.
     */
    public float getMinZoomLevel();

    public void saveCameraPosition();
    public void loadCameraPosition();

    public void setZoomStates(MapStatusUpdate mapStatusUpdate);

    /**
     * @return this map's provider.
     */
    public DPMapProvider getProvider();
    /**
     * Move the map to the drone location.
     */
    public void goToDroneLocation();

    /**
     * Move the map to the user location.
     */
    public void goToMyLocation();

    public List<LatLong> projectPathIntoMap(List<LatLong> pathPoints);

    /**
     * Remove the markers whose info is in the list from the map.
     *
     * @param markerInfoList
     *            list of markers to remove.
     */
    public void removeMarkers(Collection<MarkerInfo> markerInfoList);


    /**
     * Adds padding around the edges of the map.
     *
     * @param left
     *            the number of pixels of padding to be added on the left of the
     *            map.
     * @param top
     *            the number of pixels of padding to be added on the top of the
     *            map.
     * @param right
     *            the number of pixels of padding to be added on the right of
     *            the map.
     * @param bottom
     *            the number of pixels of padding to be added on the bottom of
     *            the map.
     */
    public void setMapPadding(int left, int top, int right, int bottom);

    /**
     * Sets a callback that's invoked when the map is tapped.
     *
     * @param listener
     *            The callback that's invoked when the map is tapped. To unset
     *            the callback, use null.
     */
    public void setOnMapClickListener(OnMapClickListener listener);

    /**
     * Sets a callback that's invoked when the map is long pressed.
     *
     * @param listener
     *            The callback that's invoked when the map is long pressed. To
     *            unset the callback, use null.
     */
    public void setOnMapLongClickListener(OnMapLongClickListener listener);

    /**
     * Sets a callback that's invoked when a marker is clicked.
     *
     * @param listener
     *            The callback that's invoked when a marker is clicked. To unset
     *            the callback, use null.
     */
    public void setOnMarkerClickListener(OnMarkerClickListener listener);

    /**
     * Sets a callback that's invoked when a marker is dragged.
     *
     * @param listener
     *            The callback that's invoked on marker drag events. To unset
     *            the callback, use null.
     */
    public void setOnMarkerDragListener(OnMarkerDragListener listener);

    /**
     * Sets a callback that's invoked when the user location is updated.
     * @param listener
     */
    public void setLocationListener(Location.LocationReceiver listener);

    /**
     * Adds a coordinate to the drone's flight path.
     *
     * @param coord
     *            drone's coordinate
     */
    public void addFlightPathPoint(LatLong coord);
    /**
     * Updates the map's center, and zoom level.
     *
     * @param coord
     *            location for the map center
     * @param zoomLevel
     *            zoom level for the map
     */
    public void updateCamera(LatLong coord, float zoomLevel);

    /**
     * Updates the map's bearing.
     * @param bearing direction that the camera is pointing in.
     */
    public void updateCameraBearing(float bearing);

    /**
     * Updates the drone leash path on the map.
     *
     * @param pathSource
     *            source to use to generate the drone leash path.
     */
    public void updateDroneLeashPath(PathSource pathSource);

    /**
     * Adds / updates the marker corresponding to the given marker info
     * argument.
     *
     * @param markerInfo
     *            used to generate / update the marker
     */
    public void updateMarker(MarkerInfo markerInfo);

    /**
     * Adds / updates the marker corresponding to the given marker info
     * argument.
     *
     * @param markerInfo
     *            used to generate / update the marker
     * @param isDraggable
     *            overwrites markerInfo draggable preference
     */
    public void updateMarker(MarkerInfo markerInfo, boolean isDraggable);

    /**
     * Adds / updates the markers corresponding to the given list of markers
     * infos.
     *
     * @param markersInfos
     *            source for the new markers to add/update
     */
    public void updateMarkers(List<MarkerInfo> markersInfos);

    /**
     * Adds / updates the markers corresponding to the given list of markers
     * infos.
     *
     * @param markersInfos
     *            source for the new markers to add/update
     * @param isDraggable
     *            overwrites markerInfo draggable preference
     */
    public void updateMarkers(List<MarkerInfo> markersInfos, boolean isDraggable);

    /**
     * Updates the mission path on the map.
     *
     * @param pathSource
     *            source to use to draw the mission path
     */
    public void updateMissionPath(PathSource pathSource);

    public void updateRealTimeFootprint(FootPrint footprint);

    /**
     * Updates the polygons on the map.
     *
     */
    public void updatePolygonsPaths(List<List<LatLong>> paths);

    /**
     * Zoom to fit coordinates on map
     *
     * @param coords
     *            to be displayed
     */
    public void zoomToFit(List<LatLong> coords);

    /**
     * Zoom to fit my location and the given coordinates on map
     * @param coords
     */
    public void zoomToFitMyLocation(List<LatLong> coords);

    public void mapType(int i);

    /**
     * Ignore marker clicks on the map and instead report the event as a mapClick
     * @param skip if it should skip further events
     */
    public void skipMarkerClickEvents(boolean skip);
}
