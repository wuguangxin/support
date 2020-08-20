package com.wuguangxin.mvp;

import android.content.Context;

/**
 * MVP架构中 View 的基础接口
 * 泛型D：需要返回的数据类型。
 *
 * Created by wuguangxin on 2016-08-26.
 */
public interface BaseView/*<D>*/ {

    Context getContext();

    /**
     * 根据加载状态控制对话框显示/隐藏（该方法已被BaseActivity实现，子Activity不需要再重写）
     * @param state 加载状态
     * @param isPull 是否是获取数据
     * @param isCached 是否有缓存数据
     */
    void setLoadingStatus(int state, boolean isPull, boolean isCached);

    /**
     * 弹出Toast （该方法已被BaseActivity实现，子Activity不需要再重写）
     * @param msg 信息
     */
    void showToast(String msg);

//    /**
//     * @param d 需要返回的数据类型
//     * @param key url加密的值，可以作为一次接口请求的标识
//     */
//    void onSuccess(D d, String key);

    /**
     * 网络请求完成
     */
    void onFinish();

}
