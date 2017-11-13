package zkrtdrone.zkrt.com.maplib.info;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.MapStatusUpdate;

import java.util.List;
import java.util.Set;

import zkrtdrone.zkrt.com.maplib.R;
import zkrtdrone.zkrt.com.maplib.info.mission.MissionProxy;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.AutoPanMode;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;
import zkrtdrone.zkrt.com.maplib.info.providers.DPMapProvider;
import zkrtdrone.zkrt.com.maplib.info.until.DroidPlannerPrefs;
import zkrtdrone.zkrt.com.maplib.info.until.Utils;

import static zkrtdrone.zkrt.com.maplib.info.mapApplication.getMissionProxy;

/**
 * Created by jack_xie on 17-4-24.
 */

public abstract class DroneMap extends Fragment {
    protected Context context;
    protected DPmap mMapFragment;
    protected DroidPlannerPrefs mAppPrefs;
    protected MissionProxy missionProxy;
    private final Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        final View view = inflater.inflate(R.layout.fragment_drone_map, viewGroup, false);
        mAppPrefs = new DroidPlannerPrefs(context);
        updateMapFragment();
        missionProxy = getMissionProxy();
        return view;
    }

    public final void postUpdate() {
        mHandler.post(mUpdateMap);
    }

    @Override
    public void onAttach(Context contexts) {
        super.onAttach(contexts);
        context = contexts;//contexts.getApplicationContext();
    }


    private final Runnable mUpdateMap = new Runnable() {
        @Override
        public void run() {
            if (getActivity() == null && mMapFragment == null)
                return;

            final List<MarkerInfo> missionMarkerInfos = missionProxy.getMarkersInfos();

            final boolean isThereMissionMarkers = !missionMarkerInfos.isEmpty();

            // Get the list of markers currently on the map.
            final Set<MarkerInfo> markersOnTheMap = mMapFragment.getMarkerInfoList();

            if (!markersOnTheMap.isEmpty()) {

                if (isThereMissionMarkers) {
                    markersOnTheMap.removeAll(missionMarkerInfos);
                }

                mMapFragment.removeMarkers(markersOnTheMap);
            }

            if (isThereMissionMarkers) {
                mMapFragment.updateMarkers(missionMarkerInfos, isMissionDraggable());
            }

            mMapFragment.updateMissionPath(missionProxy);

            mMapFragment.updatePolygonsPaths(missionProxy.getPolygonsPath());

            mHandler.removeCallbacks(this);
        }
    };

    protected abstract boolean isMissionDraggable();

    private void updateMapFragment() {
        final DPMapProvider mapProvider = Utils.getMapProvider(context);
        final FragmentManager fm = getChildFragmentManager();
        mMapFragment = (DPmap) fm.findFragmentById(R.id.map_fragment_container);
        if (mMapFragment == null || mMapFragment.getProvider() != mapProvider) {
            final Bundle mapArgs = new Bundle();
            mapArgs.putInt(DPmap.EXTRA_MAX_FLIGHT_PATH_SIZE, getMaxFlightPathSize());
            mMapFragment = mapProvider.getMapFragment();
            ((Fragment) mMapFragment).setArguments(mapArgs);
            fm.beginTransaction().replace(R.id.map_fragment_container, (Fragment) mMapFragment)
                    .commit();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacksAndMessages(null);
    }

    public void setMapPadding(int left, int top, int right, int bottom) {
        mMapFragment.setMapPadding(left, top, right, bottom);
    }

    public List<LatLong> projectPathIntoMap(List<LatLong> path) {
        return mMapFragment.projectPathIntoMap(path);
    }

    /**
     * Move the map to the user location.
     */
    public void goToMyLocation() {
        mMapFragment.goToMyLocation();
    }

    public void goTomapType(int i) {
        mMapFragment.mapType(i);
    }

    public abstract boolean setAutoPanMode(AutoPanMode target);
    /**
     * Move the map to the drone location.
     */
    public void goToDroneLocation() {
        mMapFragment.goToDroneLocation();
    }

    public void setmapZoom(MapStatusUpdate mapStatusUpdate){
        mMapFragment.setZoomStates(mapStatusUpdate);
    }

    //清除地图表面marker
    public void clearMapMarker(){
        mMapFragment.clearMarkers();
        mMapFragment.clearFlightPath();
    }

    /**
     * Update the map rotation.
     *
     * @param bearing
     */
    public void updateMapBearing(float bearing) {
        mMapFragment.updateCameraBearing(bearing);
    }

    /**
     * Ignore marker clicks on the map and instead report the event as a
     * mapClick
     *
     * @param skip
     *            if it should skip further events
     */
    public void skipMarkerClickEvents(boolean skip) {
        mMapFragment.skipMarkerClickEvents(skip);
    }

    protected int getMaxFlightPathSize() {
        return 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMapFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapFragment.saveCameraPosition();
    }

    public void saveCameraPosition() {
        mMapFragment.saveCameraPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapFragment.loadCameraPosition();
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
    }
}
