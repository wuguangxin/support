package com.wuguangxin.mvp.callback;

/**
 * MVP架构中V和P层的基础回调接口。
 * 泛型D：为返回的数据类型
 * Created by wuguangxin on 2016-10-08.
 */
public interface BaseModelCallback<D> {

    /**
     * 是否是拉取数据
     */
    boolean isPull();

    /**
     * 是否有缓存数据
     */
    boolean isCached();

    /**
     * 当成请求开始时回调。
     */
    void onStart();

    /**
     * 当成请求失败时回调。
     * @param msg String类型，错误消息
     */
    void onFailure(String msg);

    /**
     * 当一个请求结束时回调
     */
    void onFinish();

    /**
     * 销毁
     */
    void onDestroy();


}
