package com.wuguangxin.simple.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.wuguangxin.simple.R;
import com.wuguangxin.utils.AndroidUtils;
import com.wuguangxin.utils.Logger;
import com.wuguangxin.utils.ShakeUtils;
import com.wuguangxin.view.ViewPagerIndicator;

import androidx.core.app.ActivityCompat;
import butterknife.OnClick;
import butterknife.OnTouch;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private ViewPagerIndicator mViewPagerIndicator;

    // 6.0动态请求的权限-所有，存储空间和手机状态
    public static final String[] PERMISSION_ALL = {
            Manifest.permission.READ_EXTERNAL_STORAGE,    // 读存储卡
            Manifest.permission.WRITE_EXTERNAL_STORAGE,    // 写存储卡
            Manifest.permission.READ_PHONE_STATE,        // 手机状态
            Manifest.permission.ACCESS_WIFI_STATE        // WIFI状态
    };

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("SupportDemo");
        getTitleBar().setBackVisibility(false);
    }

    @Override
    public void initListener() {
    }

    @OnTouch(R.id.GestureView)
    public boolean onTouch(View v, MotionEvent event) {
        ShakeUtils.shake(v);
        return false;
    }

    @OnClick({
            R.id.tabHost,
            R.id.okhttp,
            R.id.XinDialog,
            R.id.ItemView,
            R.id.CircleProgressView,
            R.id.GestureView,
            R.id.permission,})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.permission:
            requestPermissions(PERMISSION_ALL);
            String deviceId = AndroidUtils.getDeviceId(this);
            Logger.e("onActivityResult", "deviceId：" + deviceId);
            break;
        case R.id.tabHost:
            openActivity(TabHostDemoActivity.class);
            break;
        case R.id.okhttp:
            testOkHttp();
            break;
        case R.id.XinDialog:
            openActivity(XinDialogActivity.class);
            break;
        case R.id.ItemView:
            openActivity(ItemViewActivity.class);
            break;
        case R.id.CircleProgressView:
            openActivity(CircleProgressViewActivity.class);
            break;
        case R.id.GestureView:
            openActivity(GestureViewActivity.class);
            break;
        }
    }

    /**
     * 请求获取权限
     *
     * @param permissions 权限
     */
    public void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, 1);
    }


    public void testOkHttp() {
//        try {
//            com.zhy.http.okhttp.OkHttpUtils
//                    .get()
//                    .url("http://223.223.202.183:8066/rest-p2p/front/rest/index/indexinfo?bannerCount=10&webType=2&sign=046fa7b613c4d9073991625036261318&pageSize=1")
////                    .url("http://192.168.1.126:5000/statistics/base")
//                    .build()
//                    .connTimeOut(1000*30)
//                    .readTimeOut(1000*30)
//                    .execute(new StringCallback() {
//                        @Override
//                        public String parseNetworkResponse(Response response, int id) {
//                            try {
//                                return response.body().string();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            return null;
//                        }
//
//                        @Override
//                        public void onError(okhttp3.Call call, Exception e, int id) {
//                            Log.e(TAG, e.toString());
//
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.e(TAG, response);
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    @Override
    public void initData() {

    }

    @Override
    public void setData() {

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
