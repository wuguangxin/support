package com.wuguangxin.simple.ui.call;

import android.os.Build;
import android.telecom.Call;
import android.telecom.InCallService;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class PhoneCallService extends InCallService {

    private static final String TAG = "PhoneCallService";

    private Call.Callback callback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int state) {
            super.onStateChanged(call, state);
            switch (state) {
            case Call.STATE_ACTIVE: // 通话中
                Log.i(TAG, "onStateChanged：通话中");
                break;

            case Call.STATE_DISCONNECTED: // 通话结束
                //  ActivityStack.getInstance().finishActivity(PhoneCallActivity.class);
                Log.i(TAG, "onStateChanged：通话结束");
                break;
            }
        }
    };


    // 电话进来时会被调用
    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);

        call.registerCallback(callback);
        PhoneCallManager.call = call; // 拿到它可以实现系统的接听挂断的功能

        CallType callType = null;

        if (call.getState() == Call.STATE_RINGING) {
            callType = CallType.CALL_IN;
        } else if (call.getState() == Call.STATE_CONNECTING) {
            callType = CallType.CALL_OUT;
        }

        if (callType != null) {
            Call.Details details = call.getDetails();
            String phoneNumber = details.getHandle().getSchemeSpecificPart();
            Log.i(TAG, "onCallAdded：" + phoneNumber);

            // 这里开启一个自定义的我们的界面
        }

        if (callType != null) {
            Call.Details details = call.getDetails();
            String phoneNumber = details.getHandle().toString().substring(4).replaceAll("%20", ""); // 去除拨出电话中的空格
//            CallActivity.enter(this, phoneNumber, callType);
            Log.i(TAG, "onCallAdded2：" + phoneNumber);
        }
    }

    // 电话断开时会被调用
    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        call.unregisterCallback(callback);
        PhoneCallManager.call = null;
        Log.i(TAG, "onCallRemoved：电话挂断了");
    }
}