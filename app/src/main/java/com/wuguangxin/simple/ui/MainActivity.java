package com.wuguangxin.simple.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wuguangxin.simple.R;
import com.wuguangxin.simple.constans.Constants;
import com.wuguangxin.utils.AndroidUtils;
import com.wuguangxin.utils.Logger;

import java.util.HashMap;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private Map<Integer, Class<? extends Activity>> mActivityMap;
    private PopupWindow popupWindow;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setBackVisibility(false);
        mActivityMap = new HashMap<>();
        mActivityMap.put(R.id.widget, WidgetActivity.class);
        mActivityMap.put(R.id.mvp, MvpActivity.class);
        mActivityMap.put(R.id.game, GameActivity.class);
        mActivityMap.put(R.id.test1, TestActivity.class);
    }

    @Override
    public void initListener() {
    }

    @OnClick({
            R.id.permission,
            R.id.widget,
            R.id.mvp,
            R.id.game,
            R.id.test1,
    })
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.permission) {
            requestPermissions(Constants.PERMISSION_ALL);
        } else {
            openActivity(mActivityMap.get(id));
        }
    }

    private void testPopupWindow(View v) {

        LinearLayout rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        textView.setText("哈哈哈哈");
        textView.setHeight(300);
        textView.setBackgroundColor(Color.RED);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -2));

        Button button = new Button(this);
        button.setText("确认");
        button.setWidth(200);
        button.setHeight(100);

        rootView.addView(textView);
        rootView.addView(button);

        popupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, 400);
//        popupWindow.setContentView(rootView);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(v);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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
