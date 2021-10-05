package com.wuguangxin.simple.retrofit;

import com.wuguangxin.utils.Logger;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseCallback<T extends Result<?>> implements Callback<T> {
    private static final String TAG = BaseCallback.class.getSimpleName();

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        int code = response.code();
        T body = response.body();
        if (code == 200) {
            onSuccess(body);
        } else {
            onError(code, response.message());
        }
        onComplete();
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onError(-1, t.getMessage());
        onComplete();
    }

    /**
     * 当成功时回调
     */
    public void onSuccess(T response) {};

    /**
     * 当错误时回调
     */
    public void onError(int errorCode, String errorMsg) {
        Logger.e(TAG, String.format("请求失败：errorCode=%s errorMsg=%s", errorCode, errorMsg));
    }

    /**
     * 当请求完成时回调
     */
    public void onComplete() {
        Logger.i(TAG, "请求完成");
    }
}