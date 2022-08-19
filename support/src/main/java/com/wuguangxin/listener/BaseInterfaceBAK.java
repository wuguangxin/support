package com.wuguangxin.listener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

/**
 * View的基本接口。（View指 Activity、Fragment）
 * Created by wuguangxin on 17/5/19.
 */
public interface BaseInterfaceBAK extends LoadingListener {
    /**
     * 获取Activity上下文
     * @return
     */
    Context getContext();

    /**
     * 获取布局资源ID
     * @return
     */
    @LayoutRes int getLayoutRes();

    /**
     * 初始化View。
     * 在 BaseActivity.onCreate() 中初始化基本数据后被调用
     * @param savedInstanceState
     */
    void initView(Bundle savedInstanceState);

    /**
     * 初始化监听器。
     * 在 BaseActivity.onCreate() 中 initView() 后被调用
     */
    void initListener();

    /**
     * 初始化数据。
     * 在 BaseActivity.onStart() 被调用
     */
    void initData();

    /**
     * 设置数据。BaseActivity中未回调此函数，需要子类手动回调
     */
    void setData();

    /**
     * 查找View，就是findViewById。
     * @param id 资源ID
     * @param <T> 返回的View
     * @return
     */
    <T extends View> T findView(@IdRes int id);

    /**
     * 弹出 Toast
     * @param text 文本信息
     */
    void showToast(String text);

    /**
     * 打印日志
     * @param text 文本信息
     */
    void printLogI(String text);

    /**
     * 打开Activity
     * @param clazz 文本信息
     */
    void openActivity(Class<? extends Activity> clazz);

    /**
     * 打开Activity
     * @param clazz 目标Activity
     * @param bundle 参数
     */
    void openActivity(Class<? extends Activity> clazz, Bundle bundle);

}
