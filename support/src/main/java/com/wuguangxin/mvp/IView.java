package com.wuguangxin.mvp;

import android.content.Context;

/**
 * MVP架构中 View 的基础接口
 *
 * Created by wuguangxin on 2016-08-26.
 */
public interface IView {

    Context getContext();

    /**
     * 根据加载状态控制对话框显示/隐藏（该方法已被BaseActivity实现，子Activity不需要再重写）
     * @param loadingStatus 加载状态, 看@{@link com.wuguangxin.base.LoadingStatus}
     * @param isPull 是否是获取数据
     * @param isCached 是否有缓存数据
     */
    void setLoadingStatus(int loadingStatus, boolean isPull, boolean isCached);

    /**
     * 弹出Toast （该方法已被BaseActivity实现，子Activity不需要再重写）
     * @param msg 信息
     */
    void showToast(String msg);

    /**
     * 网络请求完成
     */
    void onFinish();

}
