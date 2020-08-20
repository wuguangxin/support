package com.wuguangxin.mvp.presenter;

import com.wuguangxin.base.LoadingStatus;
import com.wuguangxin.mvp.BaseModel;
import com.wuguangxin.mvp.BasePresenter;
import com.wuguangxin.mvp.BaseView;

/**
 * Presenter的基础实现类，实现了公共方法，子类无序在实现。
 * Created by wuguangxin on 2016-08-26.
 */
public abstract class AbsBasePresenter<M extends BaseModel, V extends BaseView> implements BasePresenter<M, V> {
    public V view;
    public M model;
    private boolean hasCache;

    public AbsBasePresenter(V view) {
        this.view = view;
        this.model = newModel();
    }

    @Override
    public V getView() {
        return view;
    }

    public M getModel() {
        return model;
    }

    @Override
    public boolean isCached() {
        return hasCache;
    }

    @Override
    public boolean isPull() {
        return true;
    }

    @Override
    public void setLoadingStatus(int loadingStatus) {
        view.setLoadingStatus(loadingStatus, isPull(), isCached());
    }

    @Override
    public void showLoading() {
        view.setLoadingStatus(LoadingStatus.START, isPull(), isCached());
    }

    @Override
    public void hideLoading() {
        view.setLoadingStatus(LoadingStatus.FINISH, isPull(), isCached());
    }

    @Override
    public void onStart() {
        showLoading();
    }

    @Override
    public void onSuccess(Object data, String key) {
        this.hasCache = hasCache || data != null;
        setLoadingStatus(LoadingStatus.SUCCESS);
        view.onFinish();
    }

    @Override
    public void onFailure(String msg) {
        setLoadingStatus(LoadingStatus.FAILURE);
        view.showToast(msg);
        view.onFinish();
    }

    @Override
    public void onFinish() {
        setLoadingStatus(LoadingStatus.FINISH);
        view.onFinish();
    }

    @Override
    public void onDestroy() {
        view = null;
        model = null;
        System.gc();
    }
}