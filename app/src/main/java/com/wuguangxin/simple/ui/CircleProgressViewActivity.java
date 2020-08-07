package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.widget.SeekBar;

import com.wuguangxin.simple.R;
import com.wuguangxin.view.CircleProgressView;

public class CircleProgressViewActivity extends BaseActivity {
    private CircleProgressView mCircleProgressView;
    private SeekBar mSeekBarSweepAngle; // 跨度
    private SeekBar mSeekBarProgress;   // 进度

    @Override
    public int getLayoutRes() {
        return R.layout.activity_circleprogressview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("圆形进度条");

        mCircleProgressView = findViewById(R.id.home_CircleProgressView);
        mCircleProgressView.setUseAnim(false);

        mSeekBarSweepAngle= findViewById(R.id.seekBar_sweepAngle);
        mSeekBarSweepAngle.setProgress((int) mCircleProgressView.getSweepAngle());
        mSeekBarSweepAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCircleProgressView.setSweepAngle(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mSeekBarProgress= findViewById(R.id.seekBar_progress);
        mSeekBarProgress.setProgress((int) (mCircleProgressView.getProgress() * 100));
        mSeekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCircleProgressView.setProgress(((float) progress) / 100);
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
