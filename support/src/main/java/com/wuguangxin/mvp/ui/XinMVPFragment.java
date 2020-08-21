package com.wuguangxin.mvp.ui;

import android.os.Bundle;

import com.wuguangxin.mvp.BaseModel;
import com.wuguangxin.mvp.BaseView;
import com.wuguangxin.mvp.presenter.AbsBasePresenter;
import com.wuguangxin.ui.XinBaseFragment;

/**
 * 基础Fragment
 * Created by wuguangxin on 16/8/26
 */
public abstract class XinMVPFragment<P extends AbsBasePresenter<? extends BaseModel, ? extends BaseView>> extends XinBaseFragment {
    protected P mPresenter;

    @Override
    public void initArguments(Bundle arguments) {
        super.initArguments(arguments);
        mPresenter = newPresenter();
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        super.onDestroyView();
    }

    /**
     * 创建 MVP 的 Presenter
     */
    public abstract P newPresenter();

    /**
     * 获取 MVP 的 Presenter
     * @return
     */
    public P getPresenter() {
        if (mPresenter == null) {
            newPresenter();
        }
        if (mPresenter == null) {
            throw new NullPointerException("filed [mPresenter] is null");
        }
        return mPresenter;
    }

}