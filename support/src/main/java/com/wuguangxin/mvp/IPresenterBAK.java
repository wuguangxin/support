package com.wuguangxin.mvp;

/**
 * MVP架构的基础接口-Presenter
 * M: extends IModel
 * V: extends IView
 *
 * Created by wuguangxin on 2016-08-26.
 */
public interface IPresenterBAK<M extends IModel, V extends IView> {

    /**
     * 创建M层的实现类实例，不能多次调用。如果一个P有多个model，可以在P的实现类里创建另外的model即可
     * @return 返回 BaseModel 的实现类
     */
    M newModel();

    /**
     * 获取 Model层 的接口实现类对象
     * @return
     */
    M getModel();

    /**
     * 获取 View层 的接口实现类对象
     * @return
     */
    V getView();


    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    /**
     * 是否是拉取数据
     * @return
     */
    default boolean isPull() { return true; }

    /**
     * 是否缓存了数据
     * @return
     */
    default boolean isCached() { return false; }

    /**
     * 当成请求开始时回调。
     */
    void onStart();

    /**
     * 当请求成功时回调
     * @param object 返回的数据
     * @param key 请求接口地址的MD5值
     */
    void onSuccess(Object object, String key);

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


    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    /**
     * 设置加载对话框显示/隐藏
     * @param loadingStatus 加载状态
     */
    void setLoadingStatus(int loadingStatus);

    /**
     * 显示加载对话框
     */
    void showLoading();

    /**
     * 隐藏加载对话框就
     */
    void hideLoading();

}