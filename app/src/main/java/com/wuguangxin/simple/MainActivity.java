package com.wuguangxin.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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
        findViewById(R.id.okhttp).setOnClickListener(this);
        findViewById(R.id.ToastUtils).setOnClickListener(this);
        findViewById(R.id.MyDialog).setOnClickListener(this);
        findViewById(R.id.ItemView).setOnClickListener(this);
        findViewById(R.id.CircleProgressView).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
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
