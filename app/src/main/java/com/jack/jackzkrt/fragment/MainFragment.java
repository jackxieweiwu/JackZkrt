package com.jack.jackzkrt.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jack.frame.core.AbsFragment;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.databinding.FragmentMainBinding;
import com.jack.jackzkrt.widget.genericdrawerLayout.GenericDrawerLayout;
import com.jack.jackzkrt.widget.genericdrawerLayout.MaterialMenuButton;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jack on 2017/11/10.
 */

public class MainFragment extends AbsFragment<FragmentMainBinding> {

    private FpvOrMapFragment fpvOrMapFragment;
    @Bind(R.id.genericdrawerlayout) GenericDrawerLayout mGenericDrawerLayout;
    @Bind(R.id.menubtn_right) MaterialMenuButton mMaterialMenuButton;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            fpvOrMapFragment = (FpvOrMapFragment) mActivity.getSupportFragmentManager()
                    .getFragment(savedInstanceState, "StartFragment");

        } else {
            fpvOrMapFragment = FpvOrMapFragment.newInstance();
        }

        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_map_fpv, fpvOrMapFragment)
                .commit();

        mGenericDrawerLayout.setOpaqueWhenTranslating(true);
        mGenericDrawerLayout.setMaxOpaque(0.6f);

        TextView textView = new TextView(mActivity);
        textView.setBackgroundColor(Color.parseColor("#00A4A6"));
        textView.setGravity(Gravity.CENTER);
        textView.setText("GenericDrawerLayout");
        textView.setTextSize(22);
        mGenericDrawerLayout.setContentLayout(textView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 设置抽屉留白
        mGenericDrawerLayout.setDrawerEmptySize((int) (getResources().getDisplayMetrics().density * 120 + 0.5f));
        // 设置不需要自动播放动画，因为抽屉会回调动画的执行
        mGenericDrawerLayout.setDrawerCallback(mGenericDrawerCallback);
    }

    @OnClick(R.id.menubtn_right)
    public void MenuClick(View v){
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
