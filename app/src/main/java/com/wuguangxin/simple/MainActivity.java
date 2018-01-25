package com.wuguangxin.simple;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wuguangxin.utils.AndroidUtils;
import com.wuguangxin.utils.Logger;
import com.wuguangxin.utils.ShakeUtils;
import com.wuguangxin.view.ViewPagerIndicator;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ViewPagerIndicator mViewPagerIndicator;
    /**
     * 6.0动态请求的权限-所有，存储空间和手机状态
     */
    public static final String[] PERMISSION_ALL = {
            Manifest.permission.READ_EXTERNAL_STORAGE,	// 读存储卡
            Manifest.permission.WRITE_EXTERNAL_STORAGE,	// 写存储卡
            Manifest.permission.READ_PHONE_STATE,		// 手机状态
            Manifest.permission.ACCESS_WIFI_STATE		// WIFI状态
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tabHost).setOnClickListener(this);
        findViewById(R.id.okhttp).setOnClickListener(this);
        findViewById(R.id.ToastUtils).setOnClickListener(this);
        findViewById(R.id.MyDialog).setOnClickListener(this);
        findViewById(R.id.ItemView).setOnClickListener(this);
        findViewById(R.id.CircleProgressView).setOnClickListener(this);
        findViewById(R.id.GestureView).setOnClickListener(this);
        findViewById(R.id.permission).setOnClickListener(this);

        findViewById(R.id.GestureView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ShakeUtils.shake(v);
                return false;
            }
        });

        findViewById(R.id.EditText).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openActivity(GestureViewActivity.class);
                return false;
            }
        });

//        Logger.i(this, MoneyUtils.formatString(100.00D, "元"));
//        Logger.i(this, MoneyUtils.formatString(100.00D));
//        Logger.i(this, MoneyUtils.formatString(0.00D));
//        Logger.i(this, MoneyUtils.formatString(0.00D, "元"));
//        Logger.i(this, MoneyUtils.formatString(100.9, "元"));
//        Logger.i(this, MoneyUtils.formatString(0.9, "元"));

//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatDate("2017.12.18"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatDate("2017-12-18"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatDate("2017/12/18"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatDate("2017年12月18日"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatDate("2017年12月18日02时13分01秒"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatDate("2017/12/18 02:13:01"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatDate("2017-12-18 00:00:00"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatDate("2017-12-18 02:13:01"));
//        Logger.i(this, "===========================");
//        Logger.i(this, "间隔天数："+ DateUtils.dateDiff(1513451568000L, 1514044800000L));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.permission:
            requestPermissions(PERMISSION_ALL);
            String deviceId = AndroidUtils.getDeviceId(this);
            Logger.e("onActivityResult", "deviceId："+deviceId);
            break;
        case R.id.tabHost:
            openActivity(TabHostDemoActivity.class);
            break;
        case R.id.okhttp:
            testOkHttp();
            break;
        case R.id.ToastUtils:
            openActivity(ToastUtilsActivity.class);
            break;
        case R.id.MyDialog:
            openActivity(MyDialogActivity.class);
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
     * @param permissions 权限
     */
    public void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, 1);
    }


    public void testOkHttp(){
        try {
            com.zhy.http.okhttp.OkHttpUtils
                    .get()
                    .url("http://223.223.202.183:8066/rest-p2p/front/rest/index/indexinfo?bannerCount=10&webType=2&sign=046fa7b613c4d9073991625036261318&pageSize=1")
//                    .url("http://192.168.1.126:5000/statistics/base")
                    .build()
                    .connTimeOut(1000*30)
                    .readTimeOut(1000*30)
                    .execute(new StringCallback() {
                        @Override
                        public String parseNetworkResponse(Response response, int id) {
                            try {
                                return response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        public void onError(okhttp3.Call call, Exception e, int id) {
                            Log.e(TAG, e.toString());

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e(TAG, response);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openActivity(Class<? extends Activity> clazz){
        startActivity(new Intent(this, clazz));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.e("onActivityResult", "requestCode = " +requestCode);
        if (requestCode == 1) {
            Logger.e("onActivityResult", "resultCode = " +resultCode);
            Logger.e("onActivityResult", "data = " +data.toString());
            String deviceId = AndroidUtils.getDeviceId(this);
            Logger.e("onActivityResult", "deviceId："+deviceId);
        }
    }
}
