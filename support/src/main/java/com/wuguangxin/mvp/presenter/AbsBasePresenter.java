package com.wuguangxin.mvp.presenter;

import com.wuguangxin.base.LoadingStatus;
import com.wuguangxin.mvp.IModel;
import com.wuguangxin.mvp.IPresenter;
import com.wuguangxin.mvp.IView;

/**
 * Presenter的基础实现类，实现了公共方法，子类无需在实现。
 * Created by wuguangxin on 2016-08-26.
 */
public abstract class AbsBasePresenter<M extends IModel, V extends IView> implements IPresenter<M, V> {
    public V view;
    public M model;
    public Object data;

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
    public void setLoadingStatus(int loadingStatus) {
        getView().setLoadingStatus(loadingStatus, isPull(), isCached());
    }

    @Override
    public void showLoading() {
        getView().setLoadingStatus(LoadingStatus.START, isPull(), isCached());
    }

    @Override
    public void hideLoading() {
        getView().setLoadingStatus(LoadingStatus.FINISH, isPull(), isCached());
    }

    @Override
    public boolean isCached() {
        return this.data != null;
    }

    @Override
    public void onStart() {
        showLoading();
    }

    @Override
    public void onSuccess(Object data, String key) {
        this.data = data;
        setLoadingStatus(LoadingStatus.SUCCESS);
        getView().onFinish();
    }

    @Override
    public void onFailure(String msg) {
        setLoadingStatus(LoadingStatus.FAILURE);
        getView().showToast(msg);
        getView().onFinish();
    }

    @Override
    public void onFinish() {
        setLoadingStatus(LoadingStatus.FINISH);
        getView().onFinish();
    }

    @Override
    public void onDestroy() {
        view = null;
        model = null;
        System.gc();
    }
}