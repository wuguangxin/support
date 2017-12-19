package com.wuguangxin.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wuguangxin.utils.ShakeUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

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

//        Logger.i(this, MoneyUtils.format(100.00D, "元"));
//        Logger.i(this, MoneyUtils.format(100.00D));
//        Logger.i(this, MoneyUtils.format(0.00D));
//        Logger.i(this, MoneyUtils.format(0.00D, "元"));
//        Logger.i(this, MoneyUtils.format(100.9, "元"));
//        Logger.i(this, MoneyUtils.format(0.9, "元"));

//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatTimestamp("2017.12.18"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatTimestamp("2017-12-18"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatTimestamp("2017/12/18"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatTimestamp("2017年12月18日"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatTimestamp("2017年12月18日02时13分01秒"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatTimestamp("2017/12/18 02:13:01"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatTimestamp("2017-12-18 00:00:00"));
//        Logger.i(this, "格式化为时间戳："+ DateUtils.formatTimestamp("2017-12-18 02:13:01"));
//        Logger.i(this, "===========================");
//        Logger.i(this, "间隔天数："+ DateUtils.dateDiff(1513451568000L, 1514044800000L));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
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
                        public void onError(Call call, Exception e, int id) {
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
}
