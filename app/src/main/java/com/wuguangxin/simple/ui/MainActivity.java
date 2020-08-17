package com.wuguangxin.simple.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wuguangxin.simple.R;
import com.wuguangxin.simple.constans.Constants;
import com.wuguangxin.utils.AndroidUtils;
import com.wuguangxin.utils.Logger;

import androidx.core.app.ActivityCompat;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setBackVisibility(false);
    }

    @Override
    public void initListener() {
    }

    @OnClick({
            R.id.widget,
            R.id.permission,
    })
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.widget:
            openActivity(WidgetActivity.class);
            break;
        case R.id.permission:
            requestPermissions(Constants.PERMISSION_ALL);
//            String deviceId = AndroidUtils.getDeviceId(this);
//            Logger.e("onActivityResult", "deviceId：" + deviceId);
            break;
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }

    /**
     * 请求获取权限
     *
     * @param permissions 权限
     */
    public void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("onActivityResult", "requestCode = " + requestCode);
        if (requestCode == 1) {
            Logger.e("onActivityResult", "resultCode = " + resultCode);
            Logger.e("onActivityResult", "data = " + data.toString());
            String deviceId = AndroidUtils.getDeviceId(this);
            Logger.e("onActivityResult", "deviceId：" + deviceId);
        }
    }

}
