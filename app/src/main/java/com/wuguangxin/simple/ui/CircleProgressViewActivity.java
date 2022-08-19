package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.widget.SeekBar;

import com.wuguangxin.simple.R;
import com.wuguangxin.simple.databinding.ActivityCircleprogressviewBinding;

public class CircleProgressViewActivity extends BaseActivity<ActivityCircleprogressviewBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_circleprogressview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("圆形进度条");

        binding.homeCircleProgressView.setUseAnim(false);
//        binding.seekBarSweepAngle.setProgress((int) binding.homeCircleProgressView.getSweepAngle());
//        binding.seekBarProgress.setProgress((int) (binding.homeCircleProgressView.getProgress() * 100));

        binding.seekBarSweepAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                printLogI("seekBarSweepAngle progress = " + progress);
                binding.homeCircleProgressView.setSweepAngle(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                printLogI("seekBarProgress progress = " + progress);
                binding.homeCircleProgressView.setProgress(((float) progress) / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
