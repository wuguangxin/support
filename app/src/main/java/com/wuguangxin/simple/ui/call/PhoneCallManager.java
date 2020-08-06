package com.wuguangxin.simple.ui.call;

import android.os.Build;
import android.telecom.Call;
import android.telecom.VideoProfile;

import androidx.annotation.RequiresApi;

public class PhoneCallManager {

    public static Call call;

    /**
     * 接听电话
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void answer() {
        if (call != null) {
            call.answer(VideoProfile.STATE_AUDIO_ONLY);
        }
    }

    /**
     * 断开电话，包括来电时的拒接以及接听后的挂断
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void disconnect() {
        if (call != null) {
            call.disconnect();
        }
    }
}