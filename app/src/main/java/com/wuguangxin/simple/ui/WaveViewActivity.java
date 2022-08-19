package com.wuguangxin.simple.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import com.wuguangxin.simple.R;
import com.wuguangxin.simple.databinding.ActivityWaveViewBinding;

/**
 * 波浪View
 *
 * Created by wuguangxin on 2022/4/8.
 */
public class WaveViewActivity extends BaseActivity<ActivityWaveViewBinding> {

    private AnimationDrawable animDrawable;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wave_view;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("");
//        animDrawable = (AnimationDrawable) binding.extremeRainVolAnim2.getDrawable();
//        animDrawable.start();
//
//        binding.extremeRainVolAnim.startAnim();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {
    }

}
