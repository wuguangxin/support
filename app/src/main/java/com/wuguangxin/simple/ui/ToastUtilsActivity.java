package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.View;

import com.wuguangxin.simple.R;
import com.wuguangxin.utils.ToastUtils;

import androidx.appcompat.app.AppCompatActivity;

public class ToastUtilsActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Toast工具类");
        setContentView(R.layout.activity_toastutils);
        findViewById(R.id.show_toast).setOnClickListener(this);
        findViewById(R.id.sync_show_toast).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.show_toast:
            ToastUtils.showToast(this, "同步Toast");
            break;

        case R.id.sync_show_toast:
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(ToastUtilsActivity.this, "异步Toast");
                }
            }).start();

            break;
        }
    }
}
