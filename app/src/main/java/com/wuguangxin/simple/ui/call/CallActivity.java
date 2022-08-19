package com.wuguangxin.simple.ui.call;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wuguangxin.simple.R;
import com.wuguangxin.simple.databinding.ActivityCallBinding;
import com.wuguangxin.simple.ui.BaseActivity;
import com.wuguangxin.utils.PhoneUtils;

/**
 * 拨号界面
 * Created by wuguangxin on 2020-03-18.
 */
public class CallActivity extends BaseActivity<ActivityCallBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_call;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setTitle("电话");
    }

    @Override
    public void initListener() {
    }

    public void onViewClicked(View view) {
        int viewId = view.getId();
        switch (viewId) {
        case R.id.call_0:
        case R.id.call_1:
        case R.id.call_2:
        case R.id.call_3:
        case R.id.call_4:
        case R.id.call_5:
        case R.id.call_6:
        case R.id.call_7:
        case R.id.call_8:
        case R.id.call_9:
        case R.id.call_x:
        case R.id.call_j:
            setNumber(view);
            break;
        case R.id.call_delete:
            deleteNumber();
            break;
        case R.id.call_commit:
            call(getNumber());
            break;
        case R.id.call_set:
            setting();
            break;
        }
    }

    private void setting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
            intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
            startActivity(intent);
        } else {
            showToast("Android SDK版本（" + Build.VERSION.SDK_INT + "）太低，必须>=23");
        }
    }

    private void call(String number) {
        if (TextUtils.isEmpty(number)) {
            showToast("请输入号码");
            return;
        }
//        if (!PhoneUtils.isPhoneNumber(number)) {
//            showToast("号码格式不正确");
//            return;
//        }
        PhoneUtils.call(this, number);

        overridePendingTransition(R.anim.xin_anim_activity_open_enter, R.anim.xin_anim_activity_open_exit);
    }

    private void deleteNumber() {
        String number = getNumber();
        if (number != null) {
            if (!TextUtils.isEmpty(number)) {
                number = number.substring(0, number.length() - 1);
            }
        }
        binding.callNumber.setText(number);
    }

    private void setNumber(View view) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            String s = textView.getText().toString();
            binding.callNumber.setText(binding.callNumber.getText() + s);
        }
    }

    private String getNumber() {
        CharSequence charSequence = binding.callNumber.getText();
        if (charSequence != null) {
            return charSequence.toString();
        }
        return null;
    }

    @Override
    public void initData() {
    }

    @Override
    public void setData() {
    }

    /**
     * Android M 以上检查是否是系统默认电话应用
     */
    public static boolean isDefaultPhoneCallApp(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelecomManager manger = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
            if (manger != null) {
                String name = manger.getDefaultDialerPackage();
                return name.equals(context.getPackageName());
            }
        }
        return false;
    }

    public static void enter(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CallActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
}
