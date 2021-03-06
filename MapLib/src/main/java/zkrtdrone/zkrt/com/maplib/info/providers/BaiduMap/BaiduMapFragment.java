package zkrtdrone.zkrt.com.maplib.info.providers.BaiduMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Gradient;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.jack.frame.util.DensityUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import dji.common.flightcontroller.Attitude;
import zkrt.ui.base.BaseApplication;
import zkrtdrone.zkrt.com.maplib.R;
import zkrtdrone.zkrt.com.maplib.info.DPmap;
import zkrtdrone.zkrt.com.maplib.info.MarkerInfo;
import zkrtdrone.zkrt.com.maplib.info.location.Coord2D;
import zkrtdrone.zkrt.com.maplib.info.location.Location;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.AutoPanMode;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.FootPrint;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;
import zkrtdrone.zkrt.com.maplib.info.providers.DPMapProvider;
import zkrtdrone.zkrt.com.maplib.info.units.DroneHelper;
import zkrtdrone.zkrt.com.maplib.info.units.collection.HashBiMap;
import zkrtdrone.zkrt.com.maplib.info.widght.RotateImageView;

import static android.content.Context.SENSOR_SERVICE;
import static com.jack.frame.PublicStackNUm.droneloLat;
import static com.jack.frame.PublicStackNUm.droneloLng;
import static com.jack.frame.PublicStackNUm.peploLat;
import static com.jack.frame.PublicStackNUm.peploLng;
import static java.lang.Float.NaN;

public class BaiduMapFragment extends SupportMapFragment implements DPmap, SensorEventListener {
    private static final String TAG = BaiduMapFragment.class.getSimpleName();
    boolean isFirstLoc = true; // 是否首次定位
    private static final float GO_TO_MY_LOCATION_ZOOM = 19f; //定位我的地址的缩放级别
    private SensorManager mSensorManager;
    private final HashBiMap<MarkerInfo, Marker> mBiMarkersMap = new HashBiMap<MarkerInfo, Marker>();
    private MapView mMapView;
    private Polyline flightPath;
    private Polyline missionPath;
    private Polyline mDroneLeashPath;
    private int maxFlightPathSize;
    List<LatLng> mdFlightPathList = new ArrayList<LatLng>();
    private DPmap.OnMapClickListener mMapClickListener;
    private DPmap.OnMapLongClickListener mMapLongClickListener;
    private DPmap.OnMarkerClickListener mMarkerClickListener;
    private DPmap.OnMarkerDragListener mMarkerDragListener;
    protected boolean useMarkerClickAsMapClick = false; //是否使用地图上的覆盖物作为地图的点击事件
    private List<Polygon> polygonsPaths = new ArrayList<Polygon>();
    private Polygon footprintPoly;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private Location.LocationReceiver mLocationListener;
    private final AtomicReference<AutoPanMode> mPanMode = new AtomicReference<AutoPanMode>(
            AutoPanMode.DISABLED);
    private Double lastX = 0.0;
    private float mCurrentAccracy;
    private int mCurrentDirection = 0;
    private MyLocationData locData;
    private CoordinateConverter converter = new CoordinateConverter();
    private RotateImageView rotateImageView;
    private View viewDrone;
    protected BaseApplication dpApp;
    private Marker markerDroneBit;
    private List<LatLng> randomList = new ArrayList<>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dpApp = (BaseApplication) activity.getApplication();
        viewDrone = View.inflate(dpApp, R.layout.view_drone, null);
        rotateImageView = (RotateImageView) viewDrone.findViewById(R.id.img_drone);
        //getPleteGpsData();
    }

    //get Drone GPS
   /*private void getPleteGpsData(){
        if(ModuleVerificationUtil.isFlightControllerAvailable()){
            BaseApplication.getAircraftInstance().getRemoteController().setGPSDataCallback(new GPSData.Callback() {
                @Override
                public void onUpdate(@NonNull GPSData gpsData) {
                    setPeleGps(gpsData);
                }
            });
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle bundle) {
        final View view = super.onCreateView(inflater, viewGroup, bundle);
        final BaiduMap.OnMapClickListener onMapClickListener = (new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (mMapClickListener != null) {
                    mMapClickListener.onMapClick(DroneHelper.BaiduLatLngToCoord(point));
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi poi) {
                return false;
            }
        });
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        getBaiduMap().setOnMapClickListener(onMapClickListener);

        getBaiduMap().setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            public void onMapLongClick(LatLng point) {
                if (mMapLongClickListener != null) {
                    mMapLongClickListener.onMapLongClick((DroneHelper.BaiduLatLngToCoord(point)));
                }
            }
        });
        //设置地图覆盖物监听
        getBaiduMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                if (useMarkerClickAsMapClick) {
                    onMapClickListener.onMapClick(marker.getPosition());
                    return true;
                }
                if (mMarkerClickListener != null) {
                    return mMarkerClickListener.onMarkerClick(mBiMarkersMap.getKey(marker));
                }
                return false;
            }
        });

        //设置覆盖物的拖拽事件
        getBaiduMap().setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            //拖拽过程
            public void onMarkerDrag(Marker marker) {
                if (mMarkerDragListener != null) {
                    final MarkerInfo markerInfo = mBiMarkersMap.getKey(marker);
                    markerInfo.setPosition((DroneHelper.BaiduLatLngToCoord(marker.getPosition())));
                    mMarkerDragListener.onMarkerDrag(markerInfo);
                }
            }
            //拖拽开始
            public void onMarkerDragStart(Marker marker) {
                if (mMarkerDragListener != null) {
                    final MarkerInfo markerInfo = mBiMarkersMap.getKey(marker);
                    markerInfo.setPosition((DroneHelper.BaiduLatLngToCoord(marker.getPosition())));
                    mMarkerDragListener.onMarkerDragStart(markerInfo);
                }
            }
            //拖拽结束
            public void onMarkerDragEnd(Marker marker) {
                if (mMarkerDragListener != null) {
                    final MarkerInfo markerInfo = mBiMarkersMap.getKey(marker);
                    markerInfo.setPosition((DroneHelper.BaiduLatLngToCoord(marker.getPosition())));
                    mMarkerDragListener.onMarkerDragEnd(markerInfo);
                }
            }
        });

        final Bundle args = getArguments();
        if (args != null) {
            maxFlightPathSize = args.getInt(EXTRA_MAX_FLIGHT_PATH_SIZE);
        }
        updateCamera(new LatLong(30.3250427246094, 120.063011169434), GO_TO_MY_LOCATION_ZOOM);
        mMapView = getMapView();
        mapType(BaiduMap.MAP_TYPE_NORMAL);

        mMapView = getMapView();
        //设置是否允许定位图层
        getBaiduMap().setMyLocationEnabled(true);
        //设置是否允许指南针
        getBaiduMap().getUiSettings().setCompassEnabled(true);

        //设置是否允许俯视手势
        getBaiduMap().getUiSettings().setOverlookingGesturesEnabled(false);

        mMapView.removeViewAt(1);
        //设置是否显示缩放控件
        mMapView.showZoomControls(false);
        //设置是否显示比例尺控件
        mMapView.showScaleControl(false);

        //设置卫星图
        getBaiduMap().setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //设置是否允许定位图层
        getBaiduMap().setMyLocationEnabled(true);
        //初始定位监听
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        //定位SDK默认输出GCJ02坐标，地图SDK默认输出BD09ll
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        getBaiduMap().setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));

        converter.from(CoordinateConverter.CoordType.GPS);
        return view;
    }

    @Override
    public void onPause() {
        if (mMapView != null)
            mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mMapView != null)
            mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void clearFlightPath() {
        if (flightPath != null) {
            mdFlightPathList.clear();
            flightPath.remove();
            flightPath = null;
        }
    }

    @Override
    public void selectAutoPanMode(AutoPanMode target) {
        final AutoPanMode currentMode = mPanMode.get();
        if (currentMode == target)
            return;
    }


    @Override
    public void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        mLocClient.stop();
        getBaiduMap().setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        //    mGApiClientMgr.start();
        if (mPanMode.get() == AutoPanMode.DRONE) {
            /*LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                    .registerReceiver(eventReceiver, eventFilter);*/
        }
    }

    @Override
    public void setZoomStates(MapStatusUpdate mapStatusUpdate) {
        getBaiduMap().setMapStatus(mapStatusUpdate);
    }

    @Override
    public LatLong getMapCenter() {
        return DroneHelper.BaiduLatLngToCoord(getBaiduMap().getMapStatus().target);
        // return new LatLng(getBaiduMap().getMapStatus().target.latitude,getBaiduMap().getMapStatus().target.longitude);
    }

    @Override
    public float getMapZoomLevel() {
        return getBaiduMap().getMapStatus().zoom;
    }

    @Override
    public float getMaxZoomLevel() {
        return getBaiduMap().getMaxZoomLevel();
    }

    @Override
    public float getMinZoomLevel() {
        return getBaiduMap().getMinZoomLevel();
    }

    @Override
    public DPMapProvider getProvider() {
        return DPMapProvider.百度地图;
    }

    @Override
    public void addFlightPathPoint(LatLong coord) {
        final LatLng position = DroneHelper.CoordToBaiduLatLang(coord);
        if (maxFlightPathSize > 0) {
            if (mdFlightPathList.size() > maxFlightPathSize) {
                mdFlightPathList.remove(0);
            }
            mdFlightPathList.add(position);
            if (mdFlightPathList.size() < 2) {
                if (flightPath != null) {
                    flightPath.remove();
                    flightPath = null;
                }
                return;
            }
            if (flightPath == null) {
                PolylineOptions flightPathOptions = new PolylineOptions()
                        .color(FLIGHT_PATH_DEFAULT_COLOR)
                        .width(FLIGHT_PATH_DEFAULT_WIDTH).zIndex(1);

                flightPathOptions.points(mdFlightPathList);
                flightPath = (Polyline) getBaiduMap().addOverlay(flightPathOptions);
            }
            flightPath.setPoints(mdFlightPathList);
        }
    }

    private int numberColour = -1;

    public void setDroneMap(Attitude attitude) {
        if (droneloLat == NaN) return;
        if (getBaiduMap() == null) return;
        rotateImageView.setAttitude(attitude.yaw);
        //坐标转换
        //converter.coord(new LatLng(droneloLat, droneloLng));
        converter.coord(new LatLng(0, 0));
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(loadBitmapFromView());
        OverlayOptions option = new MarkerOptions()
                .position(converter.convert())
                .icon(bitmap);
        if (markerDroneBit != null) markerDroneBit.remove();
        markerDroneBit = (Marker) getBaiduMap().addOverlay(option);
        randomList.add(converter.convert());
        numberColour++;
        /*if(numberColour>5 && randomList.size()>0){
            setColour();
            numberColour = -1;
        }*/
    }

    //设置渐变颜色起始值
    private float[] DEFAULT_GRADIENT_START_POINTS = {0.2f, 1f};
    private int ColorRs = Color.rgb(255, 0, 0);
    private HeatMap heatmap;

    //添加毒气地图色彩
    private void setColour() {
        /*String Co2NumerStr = SharePreUtil.getString("Gams", mActivity, "StrCO2").trim();
        if(Co2NumerStr.equals("") || Co2NumerStr == "") return;
        if(coNumber>Integer.parseInt(Co2NumerStr)){
            ColorRs = Color.rgb(255, 0, 0);
        }
        if(coNumber == Integer.parseInt(Co2NumerStr)){
            ColorRs = Color.rgb(255, 255, 0);
        }*/
        //设置渐变颜色值
        int[] DEFAULT_GRADIENT_COLORS = {Color.rgb(102, 225, 0), ColorRs};
        //构造颜色渐变对象
        final Gradient gradient = new Gradient(DEFAULT_GRADIENT_COLORS, DEFAULT_GRADIENT_START_POINTS);
        //在大量热力图数据情况下，build过程相对较慢，建议放在新建线程实现

//        RxjavaUtil.executeRxTask(new CommonRxTask<Object>() {
//            @Override
//            public void doInIOThread() {
//                heatmap = new HeatMap.Builder()
//                        .data(randomList)
//                        .radius(50)
//                        .gradient(gradient)
//                        .build();
//            }
//
//            @Override
//            public void doInUIThread() {
//                //在地图上添加热力图
//                getBaiduMap().addHeatMap(heatmap);
//            }
//        });
    }

    @Override
    public void clearMarkers() {
        for (Marker marker : mBiMarkersMap.valueSet()) {
            marker.remove();
        }
        mBiMarkersMap.clear();
    }

    @Override
    public void updateMarker(MarkerInfo markerInfo) {
        updateMarker(markerInfo, markerInfo.isDraggable());
    }

    @Override
    public void updateMarker(MarkerInfo markerInfo, boolean isDraggable) {
        // if the drone hasn't received a gps signal yet
        final LatLong coord = markerInfo.getPosition();
        if (coord == null) {
            return;
        }
        final LatLng position = DroneHelper.CoordToBaiduLatLang(coord);
        Marker marker = mBiMarkersMap.getValue(markerInfo);
        if (marker == null) {
            generateMarker(markerInfo, position, isDraggable);
        } else {
            updateMarker(marker, markerInfo, position, isDraggable);
        }
    }

    private void generateMarker(MarkerInfo markerInfo, LatLng position, boolean isDraggable) {
        final MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .draggable(isDraggable)
                .rotate(-markerInfo.getRotation())
                .anchor(markerInfo.getAnchorU(), markerInfo.getAnchorV())
                .title(markerInfo.getSnippet()).title(markerInfo.getTitle());
        final Bitmap markerIcon = markerInfo.getIcon(getResources());
        if (markerIcon != null) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerIcon));
        } else {
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_marker_white));
        }
        if (markerOptions == null || getBaiduMap() == null) return;
        Marker marker = (Marker) getBaiduMap().addOverlay(markerOptions);
        mBiMarkersMap.put(markerInfo, marker);
    }

    private void updateMarker(Marker marker, MarkerInfo markerInfo, LatLng position, boolean isDraggable) {
        final Bitmap markerIcon = markerInfo.getIcon(getResources());
        if (markerIcon != null) {
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(markerIcon));
        }
        marker.setAnchor(markerInfo.getAnchorU(), markerInfo.getAnchorV());
        marker.setPosition(position);
        marker.setRotate(-markerInfo.getRotation());
        marker.setTitle(markerInfo.getSnippet());
        marker.setTitle(markerInfo.getTitle());
        marker.setDraggable(isDraggable);
        marker.setVisible(markerInfo.isVisible());
    }

    @Override
    public void updateMarkers(List<MarkerInfo> markersInfos) {
        for (MarkerInfo info : markersInfos) {
            updateMarker(info);
        }
    }

    @Override
    public void updateMarkers(List<MarkerInfo> markersInfos, boolean isDraggable) {
        for (MarkerInfo info : markersInfos) {
            updateMarker(info, isDraggable);
        }
    }

    @Override
    public Set<MarkerInfo> getMarkerInfoList() {
        return new HashSet<MarkerInfo>(mBiMarkersMap.keySet());
    }

    @Override
    public List<LatLong> projectPathIntoMap(List<LatLong> path) {
        List<LatLong> coords = new ArrayList<LatLong>();
        Projection projection = getBaiduMap().getProjection();

        for (LatLong point : path) {
            LatLng coord = projection.fromScreenLocation(new Point((int) point
                    .getLatitude(), (int) point.getLongitude()));
            coords.add(DroneHelper.BaiduLatLngToCoord(coord));
        }

        return coords;
    }

    @Override
    public void removeMarkers(Collection<MarkerInfo> markerInfoList) {
        if (markerInfoList == null || markerInfoList.isEmpty()) {
            return;
        }

        for (MarkerInfo markerInfo : markerInfoList) {
            Marker marker = mBiMarkersMap.getValue(markerInfo);
            if (marker != null) {
                marker.remove();
                mBiMarkersMap.removeKey(markerInfo);
            }
        }
    }

    @Override
    public void setMapPadding(int left, int top, int right, int bottom) {
        //getMap().setPadding(left, top, right, bottom);
    }

    @Override
    public void setOnMapClickListener(OnMapClickListener listener) {
        mMapClickListener = listener;
    }

    @Override
    public void setOnMapLongClickListener(OnMapLongClickListener listener) {
        mMapLongClickListener = listener;
    }

    @Override
    public void setOnMarkerDragListener(OnMarkerDragListener listener) {
        mMarkerDragListener = listener;
    }

    @Override
    public void setLocationListener(Location.LocationReceiver listener) {
        mLocationListener = listener;
    }

    @Override
    public void setOnMarkerClickListener(OnMarkerClickListener listener) {
        mMarkerClickListener = listener;
    }

    private void updateCamera(final LatLong coord) {
        if (coord != null) {
            final float zoomLevel = getBaiduMap().getMapStatus().zoom;
            getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(DroneHelper.CoordToBaiduLatLang(coord), zoomLevel));
        }
    }

    //以动画的形式更新地图状态
    @Override
    public void updateCamera(final LatLong coord, final float zoomLevel) {
        if (coord != null) {
            getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(DroneHelper.CoordToBaiduLatLang(coord), zoomLevel));
        }
    }

    @Override
    public void updateCameraBearing(float bearing) {
        MapStatus ms = new MapStatus.Builder(getBaiduMap().getMapStatus()).rotate(bearing).build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
        getBaiduMap().animateMapStatus(u);
    }

    @Override
    public void updateDroneLeashPath(PathSource pathSource) {
        List<LatLong> pathCoords = pathSource.getPathPoints();

        final List<LatLng> pathPoints = new ArrayList<LatLng>(pathCoords.size());
        for (LatLong coord : pathCoords) {
            pathPoints.add(DroneHelper.CoordToBaiduLatLang(coord));
        }

        if (pathPoints.size() < 2) {
            if (mDroneLeashPath != null) {
                mDroneLeashPath.remove();
                mDroneLeashPath = null;
            }
            return;
        }

        if (mDroneLeashPath == null) {
            PolylineOptions flightPath = new PolylineOptions();
            flightPath.color(DRONE_LEASH_DEFAULT_COLOR).width(
                    DroneHelper.scaleDpToPixels(DRONE_LEASH_DEFAULT_WIDTH,
                            getResources()));
            flightPath.points(pathPoints);
            mDroneLeashPath = (Polyline) getBaiduMap().addOverlay(flightPath);
        }
        mDroneLeashPath.setPoints(pathPoints);
    }

    @Override
    public void updateMissionPath(PathSource pathSource) {
        List<LatLong> pathCoords = pathSource.getPathPoints();
        final List<LatLng> pathPoints = new ArrayList<LatLng>(pathCoords.size());
        for (LatLong coord : pathCoords) {
            pathPoints.add(DroneHelper.CoordToBaiduLatLang(coord));
        }
        if (pathPoints.size() < 2) {
            if (missionPath != null) {
                missionPath.remove();
                missionPath = null;
            }
            return;
        }
        if (missionPath == null) {
            PolylineOptions pathOptions = new PolylineOptions();
            pathOptions.color(MISSION_PATH_DEFAULT_COLOR).width(
                    MISSION_PATH_DEFAULT_WIDTH);
            pathOptions.points(pathPoints);
            missionPath = (Polyline) getBaiduMap().addOverlay(pathOptions);
        }
        missionPath.setPoints(pathPoints);
    }

    @Override
    public void updatePolygonsPaths(List<List<LatLong>> paths) {
        for (Polygon poly : polygonsPaths) {
            poly.remove();
        }

        for (List<LatLong> contour : paths) {
            PolygonOptions pathOptions = new PolygonOptions();
            pathOptions.stroke(new Stroke(POLYGONS_PATH_DEFAULT_WIDTH, POLYGONS_PATH_DEFAULT_COLOR));
            final List<LatLng> pathPoints = new ArrayList<LatLng>(contour.size());
            for (LatLong coord : contour) {
                pathPoints.add(DroneHelper.CoordToBaiduLatLang(coord));
            }
            pathOptions.points(pathPoints);
            polygonsPaths.add((Polygon) getBaiduMap().addOverlay(pathOptions));
        }
    }

    @Override
    public void addCameraFootprint(FootPrint footprintToBeDraw) {
        PolygonOptions pathOptions = new PolygonOptions();
        pathOptions.stroke(new Stroke(FOOTPRINT_DEFAULT_WIDTH, FOOTPRINT_DEFAULT_COLOR));
        pathOptions.fillColor(FOOTPRINT_FILL_COLOR);
        final List<LatLng> pathPoints = new ArrayList<LatLng>();
        for (LatLong vertex : footprintToBeDraw.getVertexInGlobalFrame()) {
            pathPoints.add(DroneHelper.CoordToBaiduLatLang(vertex));
        }
        pathOptions.points(pathPoints);
        getBaiduMap().addOverlay(pathOptions);
    }

    @Override
    public void saveCameraPosition() {
        /*MapStatus camera = getBaiduMap().getMapStatus();
        mAppPrefs.prefs.edit()
                .putFloat("BAIDU_LAT", (float) camera.target.latitude)
                .putFloat("BAIDU_LNG", (float) camera.target.longitude)
                .putFloat("BAIDU_BEA", camera.rotate)
                .putFloat("BAIDU_TILT", camera.overlook)
                .putFloat("BAIDU_ZOOM", camera.zoom).apply();*/
    }

    @Override
    public void loadCameraPosition() {
        /*final SharedPreferences settings = mAppPrefs.prefs;
        MapStatus.Builder camera = new MapStatus.Builder();
        camera.rotate(settings.getFloat("BAIDU_BEA", DEFAULT_BEARING));
        camera.overlook(settings.getFloat("BAIDU_TILT", DEFAULT_TILT));
        camera.zoom(settings.getFloat("BAIDU_ZOOM", DEFAULT_ZOOM_LEVEL));
        camera.target(new LatLng(settings.getFloat("BAIDU_LAT", DEFAULT_LATITUDE),
                settings.getFloat("BAIDU_LNG", DEFAULT_LONGITUDE)));
        getBaiduMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(camera.build()));*/

    }

    @Override
    public void zoomToFit(List<LatLong> coords) {
        if (!coords.isEmpty()) {
            final List<LatLng> points = new ArrayList<LatLng>();
            for (LatLong coord : coords)
                points.add(DroneHelper.CoordToBaiduLatLang(coord));

            final LatLngBounds bounds = getBounds(points);
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngBounds(bounds);
            getBaiduMap().animateMapStatus(update);
        }
    }

    @Override
    public void zoomToFitMyLocation(final List<LatLong> coords) {
        MyLocationData locationData = getBaiduMap().getLocationData();
        if (locationData != null) {
            final List<LatLong> updatedCoords = new ArrayList<LatLong>(coords);
            updatedCoords.add(DroneHelper.BDLocationToCoord(locationData));
            zoomToFit(updatedCoords);
        } else {
            zoomToFit(coords);
        }
    }

    @Override
    public void mapType(int i) {
        getBaiduMap().setMapType(i);
    }

    @Override
    public void goToMyLocation() {
        MyLocationData locationData = getBaiduMap().getLocationData();
        if (locationData != null)
            updateCamera(DroneHelper.BDLocationToCoord(locationData), GO_TO_MY_LOCATION_ZOOM);
    }

    @Override
    public void goToDroneLocation() {
        final float currentZoomLevel = getBaiduMap().getMapStatus().zoom;
        // sourceLatLng待转换坐标
        if (droneloLat == NaN) return;
        /*converter.coord(new LatLng(droneloLat,droneloLng));
        LatLng desLatLng = converter.convert();*/
        updateCamera(new LatLong(droneloLat, droneloLng), (int) currentZoomLevel);
    }

    private LatLngBounds getBounds(List<LatLng> pointsList) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : pointsList) {
            builder.include(point);
        }
        return builder.build();
    }

    @Override
    public void skipMarkerClickEvents(boolean skip) {
        useMarkerClickAsMapClick = skip;
    }

    @Override
    public void updateRealTimeFootprint(FootPrint footprint) {
        if (footprintPoly == null) {
            PolygonOptions pathOptions = new PolygonOptions();
            pathOptions.stroke(new Stroke(FOOTPRINT_DEFAULT_WIDTH, FOOTPRINT_DEFAULT_COLOR));
            pathOptions.fillColor(FOOTPRINT_FILL_COLOR);
            final List<LatLng> pathPoints = new ArrayList<LatLng>();
            for (LatLong vertex : footprint.getVertexInGlobalFrame()) {
                pathPoints.add(DroneHelper.CoordToBaiduLatLang(vertex));
            }
            pathOptions.points(pathPoints);
            footprintPoly = (Polygon) getBaiduMap().addOverlay(pathOptions);
        } else {
            List<LatLng> list = new ArrayList<LatLng>();
            for (LatLong vertex : footprint.getVertexInGlobalFrame()) {
                list.add(DroneHelper.CoordToBaiduLatLang(vertex));
            }
            footprintPoly.setPoints(list);
        }
    }

    //传感器方向改变
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(peploLat)
                    .longitude(peploLng).build();
            getBaiduMap().setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null)
                return;
            peploLat = location.getLatitude();
            peploLng = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            getBaiduMap().setMyLocationData(locData);

            LatLong latlong = DroneHelper.BDLocationToCoord(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(19f);
                getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

            if (mPanMode.get() == AutoPanMode.USER) {
                updateCamera(latlong, (int) getBaiduMap().getMapStatus().zoom);
            }

            if (mLocationListener != null) {
                Location loc = new Location(new Coord2D(latlong.getLatitude(), latlong.getLongitude()), locData.direction, locData.speed, locData.satellitesNum > 3);
                mLocationListener.onLocationChanged(loc);
            }
        }

    }

    private Bitmap loadBitmapFromView() {
        if (rotateImageView == null) {
            return null;
        }
        rotateImageView.measure(View.MeasureSpec.makeMeasureSpec(DensityUtils.dp2px(dpApp, 40f),
                View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                DensityUtils.dp2px(dpApp, 45f), View.MeasureSpec.EXACTLY));
        rotateImageView.layout(0, 0, rotateImageView.getMeasuredWidth(), rotateImageView.getMeasuredHeight());
        rotateImageView.setDrawingCacheEnabled(true);
        rotateImageView.buildDrawingCache();
        return rotateImageView.getDrawingCache();
    }

    /*private void setPeleGps(GPSData gpsData){
        if(gpsData.isValid()){
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(gpsData.getLocationAccuracy())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(gpsData.getLocation().getLatitude())
                    .longitude(gpsData.getLocation().getLongitude()).build();
            getBaiduMap().setMyLocationData(locData);
            T.show(BaseApplication.mActivity,"遥控器获取GPS数据 Es"+
                    gpsData.getEastSpeed()+"** NS"+gpsData.getNorthSpeed()+"** LocationAccuracy"+gpsData.getLocationAccuracy()+
            "** SatelliteCount"+gpsData.getSatelliteCount()+"** Latitude"+gpsData.getLocation().getLatitude());

            //LatLng latlong = DroneHelper.BDLocationToCoord(locData);
            *//*if (mPanMode.get() == AutoPanMode.USER) {
                updateCamera(new MyLocationData(gpsData.getLocation().getLatitude(),
                        gpsData.getLocation().getLongitude(),
                        gpsData.getEastSpeed(),
                        gpsData.get), (int) getBaiduMap().getMapStatus().zoom);
            }*//*

            *//*MyLocationData(double var1, double var3, float var5, float var6, float var7, int var8) {
                this.direction = var6;
                this.accuracy = var7;
                this.satellitesNum = var8;
            }*//*

            *//*if (mLocationListener != null) {
                Location loc = new Location(new Coord2D(latlong.latitude,latlong.longitude),locData.direction,locData.speed,locData.satellitesNum>3);
                mLocationListener.onLocationChanged(loc);
            }*//*
        }else{
            T.show(BaseApplication.mActivity,"遥控器获取GPS数据失败");
        }
    }*/
}