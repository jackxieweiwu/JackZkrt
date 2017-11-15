package com.jack.jackzkrt.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.jack.frame.core.AbsFragment;
import com.jack.frame.util.DensityUtils;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.adapter.SettingIconAdapter;
import com.jack.jackzkrt.databinding.FragmentMainBinding;
import com.jack.jackzkrt.fragment.setting.BatterySettingFragment;
import com.jack.jackzkrt.fragment.setting.DroneSettingFragment;
import com.jack.jackzkrt.fragment.setting.HdSettingFragment;
import com.jack.jackzkrt.fragment.setting.HolderSettingFragment;
import com.jack.jackzkrt.fragment.setting.MapSettingFragment;
import com.jack.jackzkrt.fragment.setting.RemoteSettingFragment;
import com.jack.jackzkrt.widget.genericdrawerLayout.GenericDrawerLayout;
import com.jack.jackzkrt.widget.genericdrawerLayout.MaterialMenuButton;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jack on 2017/11/10.
 */

public class MainFragment extends AbsFragment<FragmentMainBinding> {

    private FpvOrMapFragment fpvOrMapFragment;
    @Bind(R.id.genericdrawerlayout)
    GenericDrawerLayout mGenericDrawerLayout;
    @Bind(R.id.menubtn_right)
    MaterialMenuButton mMaterialMenuButton;
    private Integer[] settingicl = {R.drawable.ic_topbar_flight_mode, R.drawable.ic_topbar_hd_nor, R.drawable.ic_topbar_battery_nor
            , R.drawable.ic_topbar_remote_nor, R.drawable.ic_checklist_gimbal, R.drawable.ic_checklist_firmware};
    private SettingIconAdapter setAdapter;

    //this is setting fragment
    private DroneSettingFragment droneSettingFragment;
    private HdSettingFragment hdSettingFragment;
    private BatterySettingFragment batterySettingFragment;
    private RemoteSettingFragment remoteSettingFragment;
    private HolderSettingFragment holderSettingFragment;
    private MapSettingFragment mapSettingFragment;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            fpvOrMapFragment = (FpvOrMapFragment) mActivity.getSupportFragmentManager()
                    .getFragment(savedInstanceState, "StartFragment");
            droneSettingFragment = (DroneSettingFragment) mActivity.getSupportFragmentManager()
                    .getFragment(savedInstanceState, "DroneSettingFragment");
            /*hdSettingFragment = (HdSettingFragment) mActivity.getSupportFragmentManager()
                    .getFragment(savedInstanceState, "DroneSettingFragment");
            batterySettingFragment = (BatterySettingFragment) mActivity.getSupportFragmentManager()
                    .getFragment(savedInstanceState, "DroneSettingFragment");
            remoteSettingFragment = (RemoteSettingFragment) mActivity.getSupportFragmentManager()
                    .getFragment(savedInstanceState, "DroneSettingFragment");
            holderSettingFragment = (HolderSettingFragment) mActivity.getSupportFragmentManager()
                    .getFragment(savedInstanceState, "DroneSettingFragment");
            mapSettingFragment = (MapSettingFragment) mActivity.getSupportFragmentManager()
                    .getFragment(savedInstanceState, "DroneSettingFragment");*/
        } else {
            fpvOrMapFragment = FpvOrMapFragment.newInstance();
            droneSettingFragment = DroneSettingFragment.newInstance();
        }
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_map_fpv, fpvOrMapFragment)
                .commit();
        mGenericDrawerLayout.setOpaqueWhenTranslating(true);
        mGenericDrawerLayout.setMaxOpaque(0.6f);

        View mMenuViewLeft = LayoutInflater.from(mActivity).inflate(R.layout.setting_drone, null);
        ListView drone_tool_bar = (ListView) mMenuViewLeft.findViewById(R.id.setting_list_icon);
        setAdapter = new SettingIconAdapter(mActivity, Arrays.asList(settingicl));
        drone_tool_bar.setAdapter(setAdapter);
        drone_tool_bar.setOnItemClickListener(onItemClickListener);
        /*TabLayout drone_tool_bar = (TabLayout) mMenuViewLeft.findViewById(R.id.drone_tool_bar);
        ViewPager drone_setting_pager = (ViewPager) mMenuViewLeft.findViewById(R.id.drone_setting_pager);*/
        setSettingFragmentAdd(droneSettingFragment);
        //  宽50%
        int width = DensityUtils.getScreenWidth(mActivity)/2;
        mGenericDrawerLayout.setContentLayout(mMenuViewLeft,  new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mGenericDrawerLayout.setDrawerEmptySize((int) (getResources().getDisplayMetrics().density * 80 + 0.5f));
        mGenericDrawerLayout.setDrawerEmptySize(width);
        mGenericDrawerLayout.setDrawerCallback(mGenericDrawerCallback);
    }

    //this is setting icon
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            setAdapter.setPosIndex(position);
            Fragment fragment = null;
            if (position == 0) {
                if (droneSettingFragment == null) {
                    droneSettingFragment = DroneSettingFragment.newInstance();
                }
                fragment = droneSettingFragment;
            }
            if (position == 1) {
                if (hdSettingFragment == null) {
                    hdSettingFragment = HdSettingFragment.newInstance();
                }
                fragment = hdSettingFragment;
            }
            if (position == 2) {
                if (batterySettingFragment == null) {
                    batterySettingFragment = BatterySettingFragment.newInstance();
                }
                fragment = batterySettingFragment;
            }
            if (position == 3) {
                if (remoteSettingFragment == null) {
                    remoteSettingFragment = RemoteSettingFragment.newInstance();
                }
                fragment = remoteSettingFragment;
            }
            if (position == 4) {
                if (holderSettingFragment == null) {
                    holderSettingFragment = HolderSettingFragment.newInstance();
                }
                fragment = holderSettingFragment;
            }
            if (position == 5) {
                if (mapSettingFragment == null) {
                    mapSettingFragment = MapSettingFragment.newInstance();
                }
                fragment = mapSettingFragment;
            }
            setSettingFragmentAdd(fragment);
        }
    };

    private void setSettingFragmentAdd(Fragment fragment) {
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_fragment, fragment)
                .commit();
    }

    @OnClick(R.id.menubtn_right)
    public void MenuClick(View v) {
        mGenericDrawerLayout.switchStatus();
    }

    private GenericDrawerLayout.DrawerCallback mGenericDrawerCallback = new GenericDrawerLayout.DrawerCallbackAdapter() {
        @Override
        public void onTranslating(int gravity, float translation, float fraction) {
            super.onTranslating(gravity, translation, fraction);
            mMaterialMenuButton.update(fraction);
        }
    };

    @Override
    protected void onDelayLoad() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
