package com.wuguangxin.base;

import android.content.Intent;

import com.wuguangxin.mvp.BaseModel;
import com.wuguangxin.mvp.BaseView;
import com.wuguangxin.mvp.presenter.AbsBasePresenter;

/**
 * MVP 中 Activity基类
 * Created by wuguangxin on 2015/4/1
 */
public abstract class BaseMvpActivity<P extends AbsBasePresenter<? extends BaseModel, ? extends BaseView>> extends BaseActivity {
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

    public P getPresenter() {
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
