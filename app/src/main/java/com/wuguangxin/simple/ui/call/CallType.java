package com.wuguangxin.simple.ui.call;

/**
 * Created by wuguangxin on 2020-04-19.
 */
public enum CallType {
    CALL_IN(0),

    CALL_OUT(1);

    public int value;

    CallType(int value) {
        this.value = value;
    }
}
