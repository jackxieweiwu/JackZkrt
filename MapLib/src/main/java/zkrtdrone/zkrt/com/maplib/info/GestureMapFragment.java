package zkrtdrone.zkrt.com.maplib.info;

import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;

import com.jack.frame.core.AbsFragment;

import java.util.ArrayList;
import java.util.List;

import zkrtdrone.zkrt.com.maplib.R;
import zkrtdrone.zkrt.com.maplib.databinding.FragmentGestureMapBinding;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;
import zkrtdrone.zkrt.com.maplib.info.units.MathUtils;


/**
 * Created by jack_xie on 17-4-24.  fragment_gesture_map
 */
public class GestureMapFragment extends AbsFragment<FragmentGestureMapBinding> implements OnGestureListener {
    private EditorMapFragment mapFragment;
    private GestureOverlayView overlay;

    private static final int TOLERANCE = 15;
    private static final int STROKE_WIDTH = 3;
    private double toleranceInPixels;

    public interface OnPathFinishedListener {
        void onPathFinished(List<LatLong> path);
    }
    private OnPathFinishedListener listener;

    @Override
    protected void init(Bundle savedInstanceState) {
        overlay = (GestureOverlayView) mRootView.findViewById(R.id.overlay1);
        overlay.addOnGestureListener(this);
        overlay.setEnabled(false);
        overlay.setGestureStrokeWidth(scaleDpToPixels(STROKE_WIDTH));
        toleranceInPixels = scaleDpToPixels(TOLERANCE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
       /* mapFragment = ((EditorMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.editor_map_fragment));*/
        mapFragment = new EditorMapFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.editor_map_fragment, mapFragment)
                .commit();
    }

    @Override
    public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
        overlay.setEnabled(false);
        List<LatLong> path = decodeGesture();
        if (path.size() > 1) {
            path = MathUtils.simplify(path, toleranceInPixels);
        }
        listener.onPathFinished(path);
    }

    private List<LatLong> decodeGesture() {
        List<LatLong> path = new ArrayList<LatLong>();
        extractPathFromGesture(path);
        return path;
    }

    private int scaleDpToPixels(double value) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) Math.round(value * scale);
    }

    public void enableGestureDetection(boolean bool) {
        overlay.setEnabled(bool);
    }

    public void setOnPathFinishedListener(OnPathFinishedListener listener) {
        this.listener = listener;
    }

    private void extractPathFromGesture(List<LatLong> path) {
        float[] points = overlay.getGesture().getStrokes().get(0).points;
        for (int i = 0; i < points.length; i += 2) {
            path.add(new LatLong((int) points[i], (int) points[i + 1]));
        }
    }

    public EditorMapFragment getMapFragment(){
        return mapFragment;
    }

    @Override
    public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
    }

    @Override
    protected void onDelayLoad() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_gesture_map;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
    @Override
    public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
    }

    @Override
    public void onGesture(GestureOverlayView overlay, MotionEvent event) {
    }
}
