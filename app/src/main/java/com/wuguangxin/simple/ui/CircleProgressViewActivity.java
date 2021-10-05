package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.widget.SeekBar;

import com.wuguangxin.simple.R;
import com.wuguangxin.view.CircleProgressView;

import butterknife.BindView;

public class CircleProgressViewActivity extends BaseActivity {
    @BindView(R.id.home_CircleProgressView) CircleProgressView mCircleProgressView;
    @BindView(R.id.seekBar_sweepAngle) SeekBar mSeekBarSweepAngle; // 跨度
    @BindView(R.id.seekBar_progress)  SeekBar mSeekBarProgress;   // 进度

    @Override
    public int getLayoutRes() {
        return R.layout.activity_circleprogressview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("圆形进度条");

        mCircleProgressView.setUseAnim(false);
        mSeekBarSweepAngle.setProgress((int) mCircleProgressView.getSweepAngle());
        mSeekBarProgress.setProgress((int) (mCircleProgressView.getProgress() * 100));
    }

    @Override
    public void initListener() {
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
    public void initData() {

    }

    @Override
    public void setData() {

    }
}
