package com.wuguangxin.mvp.ui;

import android.content.Intent;

import com.wuguangxin.ui.XinBaseActivity;
import com.wuguangxin.mvp.IModel;
import com.wuguangxin.mvp.IView;
import com.wuguangxin.mvp.presenter.AbsBasePresenter;

/**
 * MVP 中 Activity基类
 * Created by wuguangxin on 2015/4/1
 */
public abstract class XinMVPActivity<P extends AbsBasePresenter<? extends IModel, ? extends IView>> extends XinBaseActivity {
    public P mPresenter;

    @Override
    public void initArguments(Intent intent) {
        super.initArguments(intent);
        mPresenter = newPresenter();
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
        return mPresenter;
    }

    @Override
    protected void onDestroy() {
        // 销毁Presenter
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        super.onDestroy();
    }
}
