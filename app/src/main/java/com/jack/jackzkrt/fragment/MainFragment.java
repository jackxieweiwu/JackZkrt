package com.jack.jackzkrt.fragment;

import android.os.Bundle;

import com.jack.frame.core.AbsFragment;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.databinding.FragmentMainBinding;

/**
 * Created by jack on 2017/11/10.
 */

public class MainFragment extends AbsFragment<FragmentMainBinding> {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

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
