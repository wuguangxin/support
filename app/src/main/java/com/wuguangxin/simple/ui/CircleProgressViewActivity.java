package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.widget.SeekBar;

import com.wuguangxin.simple.R;
import com.wuguangxin.view.CircleProgressView;

import androidx.appcompat.app.AppCompatActivity;

public class CircleProgressViewActivity extends AppCompatActivity {
    private CircleProgressView mCircleProgressView;
    private SeekBar mSeekBarSweepAngle; // 跨度
    private SeekBar mSeekBarProgress;   // 进度


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circleprogressview);
        setTitle("圆形进度条");

        mCircleProgressView = (CircleProgressView) findViewById(R.id.home_CircleProgressView);
        mCircleProgressView.setUseAnim(false);

        mSeekBarSweepAngle= (SeekBar) findViewById(R.id.seekBar_sweepAngle);
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


        mSeekBarProgress= (SeekBar) findViewById(R.id.seekBar_progress);
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
}
