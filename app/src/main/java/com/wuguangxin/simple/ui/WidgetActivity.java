package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.wuguangxin.base.BaseActivity;
import com.wuguangxin.simple.R;
import com.wuguangxin.utils.ShakeUtils;
import com.wuguangxin.view.ViewPagerIndicator;

import butterknife.OnClick;
import butterknife.OnTouch;

public class WidgetActivity extends BaseActivity {

    private static final String TAG = "WidgetActivity";
    private ViewPagerIndicator mViewPagerIndicator;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_widget;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setBackVisibility(false);
    }

    @Override
    public void initListener() {
    }

    @OnClick({
            R.id.tabHost,
            R.id.okhttp,
            R.id.ItemView,
            R.id.CircleProgressView,
            R.id.XinDialog,
            R.id.GestureView,
    })
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.tabHost:
            openActivity(TabHostDemoActivity.class);
            break;
        case R.id.okhttp:
            testOkHttp();
            break;
        case R.id.ItemView:
            openActivity(ItemViewActivity.class);
            break;
        case R.id.CircleProgressView:
            openActivity(CircleProgressViewActivity.class);
            break;
        case R.id.XinDialog:
            openActivity(MyDialogActivity.class);
            break;
        case R.id.GestureView:
            openActivity(GestureViewActivity.class);
            break;
        }
    }

    @OnTouch(R.id.GestureView)
    public boolean onTouch(View v, MotionEvent event) {
        ShakeUtils.shake(v);
        return false;
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


}
