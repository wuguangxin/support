package com.wuguangxin.simple.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.wuguangxin.simple.listener.LoadingListener;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

/**
 * View的基本接口。（View指 Activity、Fragment）
 * Created by wuguangxin on 17/5/19.
 */
public interface BaseInterface extends LoadingListener {
    /**
     * 获取Activity上下文
     * @return
     */
    Context getContext();

    @LayoutRes int getLayoutRes();

    /**
     * 初始化View。
     * 在 BaseActivity.onCreate() 中初始化基本数据后调用
     * @param savedInstanceState
     */
    void initView(Bundle savedInstanceState);

    /**
     * 初始化监听器。
     * 在 BaseActivity.onCreate() 中 initView() 后调用
     */
    void initListener();

    /**
     * 初始化数据。
     * 在 BaseActivity.onStart() 调用
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
     * 显示消息（3.5秒后消失）
     * @param text 文本信息
     */
    void showToastLong(String text);

    /**
     * 显示消息（2.5秒后消失）
     * @param text 文本信息
     */
    void showToastShort(String text);

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

    /**
     * 打开APP WEB浏览器
     * @param webTitle 浏览器标题
     * @param webUrl 浏览器打开的URL
     */
    void openWeb(String webTitle, String webUrl);

    /**
     * 打开APP WEB浏览器，同时指定分享信息
     * @param webTitle 浏览器标题
     * @param webUrl 浏览器打开的URL
     * @param shareConfig 分享配置。把URL分享到第三方平台(如微信、朋友圈、QQ好友等)
     */
//    void openWeb(String webTitle, String webUrl, ShareInfo shareConfig);
}
