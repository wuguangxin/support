package com.wuguangxin.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.wuguangxin.dialog.MyDialog;
import com.wuguangxin.utils.ToastUtils;

public class BActivity extends AppCompatActivity implements View.OnClickListener{

    private MyDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

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
                    int size = 10;
                    for (int i = 0; i < size; i++) {
                        try {
                            Thread.sleep(1000);
                            Log.d("test", String.valueOf(i));
                            if(i == size-1){
                                ToastUtils.showToast(BActivity.this, "异步Toast");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            break;
        }
    }
}
