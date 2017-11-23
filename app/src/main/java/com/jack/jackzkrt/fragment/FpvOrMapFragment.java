package com.jack.jackzkrt.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jack.frame.core.AbsFragment;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.MainActivity;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.databinding.FragmentMainBinding;
import com.zhy.android.percent.support.PercentFrameLayout;

import butterknife.Bind;
import butterknife.OnClick;
import dji.common.flightcontroller.Attitude;
import zkrtdrone.zkrt.com.maplib.info.GestureMapFragment;
import zkrtdrone.zkrt.com.maplib.info.OnEditorInteraction;
import zkrtdrone.zkrt.com.maplib.info.mission.MissionItemProxy;
import zkrtdrone.zkrt.com.maplib.info.mission.coordinate.LatLong;

import static zkrtdrone.zkrt.com.maplib.info.mapApplication.setEditorListener;

/**
 * Created by jack on 2017/11/13.
 */

public class FpvOrMapFragment extends AbsFragment implements OnEditorInteraction{

    GestureMapFragment mgestureMapFragment ;

    @Bind(R.id.mapMain)
    FrameLayout mapMain;//map 百度地图


    @Bind(R.id.frame_fpv_or_map)
    FrameLayout fpvCameraViewMax;//viedo max

    @Bind(R.id.frame_fpv_min_video)
    FrameLayout frame_fpv_min_video;//viedo min

    @Bind(R.id.img_common_conceal)//隐藏
    ImageView img_common_conceal;
    @Bind(R.id.img_common_exchange)//切换窗口
    ImageView img_common_exchange;
    @Bind(R.id.img_common_location)//定位
    ImageView img_common_location;
    @Bind(R.id.img_common_clear_pryline)//清除
    ImageView img_common_clear_pryline;
    @Bind(R.id.Compass_layyout)
    RelativeLayout Compass_layyout;

    @Bind(R.id.mount_open)
    ImageView mount_open;
    @Bind(R.id.txt_tishi)
    TextView txt_tishi;
    @Bind(R.id.img_common_exchange4)
    ImageView img_common_exchange4;//视频点击全屏
    @Bind(R.id.frame_mount)
    FrameLayout frame_mount;

    private MapMountFragment mapMountFragment;
    private MountFragment mountFragment;

    public static FpvOrMapFragment newInstance(){
        return new FpvOrMapFragment();
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        setEditorListener(this);
        FragmentManager fragmentManager  = getChildFragmentManager();
        mgestureMapFragment= (GestureMapFragment) fragmentManager.findFragmentById(R.id.gestureMapFragment);

        if (mapMountFragment == null) {
            mapMountFragment = new MapMountFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_map, mapMountFragment)
                    .commit();
        }
        mapMountFragment.GestureMapFragment(mgestureMapFragment);

        if (mountFragment == null) {
            mountFragment = new MountFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.module_value, mountFragment)
                    .commit();
        }
        mountFragment.setImgOpen(mount_open, txt_tishi, img_common_exchange4);
    }


    @OnClick({R.id.img_common_exchange,R.id.img_common_exchange4,R.id.img_common_location,R.id.img_common_clear_pryline,R.id.img_common_conceal,R.id.mount_open})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.img_common_exchange://切换窗口
                Log.i("jll","map min -->map max");
                //map min -->map max
                mapMain.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mapMain.bringToFront();
                //video max vis--> video max gone
                fpvCameraViewMax.setVisibility(View.GONE);
                //video min gone --> video max vis
                frame_fpv_min_video.setVisibility(View.VISIBLE);
                //定位隐藏，清除隐藏,切换窗口隐藏
                img_common_exchange.setVisibility(View.GONE);
                img_common_location.setVisibility(View.GONE);
                img_common_clear_pryline.setVisibility(View.GONE);
                frame_fpv_min_video.bringToFront();
                img_common_conceal.bringToFront();
                mapMountFragment.setCheckGoneViesbile(true);
                Compass_layyout.bringToFront();
                frame_mount.bringToFront();
                break;
            case R.id.img_common_exchange4:
                //map max -->map min
                RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(frame_fpv_min_video.getWidth(), frame_fpv_min_video.getHeight());
                lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                lay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                mapMain.setLayoutParams(lay);
                //video max gone--> video max vis
                fpvCameraViewMax.setVisibility(View.VISIBLE);
                //video min vis --> video max gone
                frame_fpv_min_video.setVisibility(View.GONE);
                //定位显示，清除显示,切换显示
                img_common_exchange.setVisibility(View.VISIBLE);
                img_common_location.setVisibility(View.VISIBLE);
                img_common_clear_pryline.setVisibility(View.VISIBLE);
                fpvCameraViewMax.bringToFront();
                mapMain.bringToFront();
                img_common_conceal.bringToFront();
                Compass_layyout.bringToFront();
                mapMountFragment.setCheckGoneViesbile(false);
                frame_mount.bringToFront();
                break;
            case R.id.img_common_location://定位
                T.show(mActivity,"定位",0);
                mgestureMapFragment.getMapFragment().goToMyLocation();
                break;
            case R.id.img_common_clear_pryline://清除
                T.show(mActivity,"清除",0);
                mgestureMapFragment.getMapFragment().clearMapMarker();
                break;
            case R.id.img_common_conceal://隐藏
                if (fpvCameraViewMax.getVisibility() == View.VISIBLE) {
                    //video max
                    if (mapMain.getVisibility() == View.VISIBLE) {
                        mapMain.startAnimation(startAnimViewGone());
                        mapMain.setVisibility(View.GONE);
                    }else {
                        mapMain.startAnimation(startAnimViewShow());
                        mapMain.setVisibility(View.VISIBLE);
                    }

                } else {
                    //map max
                    if (frame_fpv_min_video.getVisibility()== View.VISIBLE) {
                        frame_fpv_min_video.startAnimation(startAnimViewGone());
                        frame_fpv_min_video.setVisibility(View.GONE);
                    }else {
                        frame_fpv_min_video.startAnimation(startAnimViewShow());
                        frame_fpv_min_video.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.mount_open:
                if (mountFragment.frame_mount.getVisibility() == View.GONE) {
                    mountFragment.frame_mount.setVisibility(View.VISIBLE);
                    mount_open.setVisibility(View.GONE);
                    mountFragment.mount_clear.startAnimation(mountFragment.animation1);
                    mountFragment.frame_mount.startAnimation(mountFragment.startAnimViewShow());
                } else {
                    mountFragment.frame_mount.setVisibility(View.GONE);
                    mount_open.setVisibility(View.VISIBLE);
                    mountFragment.mount_clear.startAnimation(mountFragment.animation);
                    mountFragment.frame_mount.startAnimation(mountFragment.startAnimViewGone());
                }

                break;
        }

    }

    @Override
    protected void onDelayLoad() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fpv_or_map;
    }

    @Override
    public boolean onItemLongClick(MissionItemProxy item) {
        return false;
    }

    @Override
    public void onItemClick(MissionItemProxy item, boolean zoomToFit) {
    }

    @Override
    public void onMapClick(LatLong coord) {
    }

    @Override
    public void onListVisibilityChanged() {

    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

    //显示动画
    private TranslateAnimation startAnimViewShow() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    //隐藏动画
    private TranslateAnimation startAnimViewGone() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }
}
