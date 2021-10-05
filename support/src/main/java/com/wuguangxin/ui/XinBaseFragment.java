package com.wuguangxin.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuguangxin.base.FragmentTask;
import com.wuguangxin.base.LayoutManager;
import com.wuguangxin.base.TitleBar;
import com.wuguangxin.dialog.XinDialog;
import com.wuguangxin.listener.BaseInterface;
import com.wuguangxin.utils.Logger;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基础Fragment
 * Created by wuguangxin on 16/8/26
 */
public abstract class XinBaseFragment extends Fragment implements BaseInterface {
    protected String TAG;
    protected XinBaseActivity mActivity;
    protected Context mContext;
    protected XinDialog mDialog;
    protected ViewGroup mRootView; // 根布局
    private Unbinder mUnBinder;
    private boolean isVisibleToUser; // 是否是用户可见的
    private SparseArray<View> viewSparse = new SparseArray<>();

    public XinBaseFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity = (XinBaseActivity) getActivity();
        this.mContext = context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (mRootView != null && mActivity != null) {
            if (isVisibleToUser) {
                onResume();
            } else {
                onPause();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // super.onCreateView(inflater, container, savedInstanceState);
        this.mRootView = container;
        this.TAG = getClass().getSimpleName();
        mRootView = (ViewGroup) inflater.inflate(getLayoutRes(), null);
        mUnBinder = ButterKnife.bind(this, mRootView); //用Uninder来解绑

        initArguments(getArgumentsByBase());     // 接收参数
        initView(savedInstanceState);            // 初始化界面
        initListener();                          // 设置监听器
        setErrorLayoutListener();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 初始化错误布局点击监听器
     */
    public void setErrorLayoutListener() {
        getLayoutManager().setErrorLayoutListener(v -> {
            Fragment fragment = FragmentTask.getInstance().getTopTask();
            if (fragment instanceof XinBaseFragment) {
                XinBaseFragment baseFragment = (XinBaseFragment) fragment;
                baseFragment.initData();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisibleToUser()) {
            FragmentTask.getInstance().inTask(this);
            initData(); // 初始化数据
        }
    }

    @Override
    public void onDestroy() {
        dismissDialog();
        getTitleBar().setLoadAnimVisible(false);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
        if (viewSparse != null) {
            viewSparse.clear();
            viewSparse = null;
        }
        FragmentTask.getInstance().outTask(this);
        super.onDestroyView();
    }

    /**
     * 在初始化View之前处理通过 Bundle 传递过来的参数。
     *
     * @param arguments Bundle
     */
    public void initArguments(Bundle arguments) {
    }

    public boolean isVisibleToUser() {
        return isVisibleToUser;
    }

    public LayoutManager getLayoutManager() {
        return mActivity == null ? null : mActivity.getLayoutManager();
    }

    public TitleBar getTitleBar() {
        return mActivity == null ? null : mActivity.getTitleBar();
    }

    public ViewGroup getRootView() {
        return mRootView;
    }

    /**
     * 关闭Activity
     */
    public void finish() {
        if (mActivity != null) mActivity.finish();
    }

    /**
     * 获取传递的参数，如果没有设置参数，则返回空的 Bundle
     *
     * @return
     */
    public Bundle getArgumentsByBase() {
        Bundle bundle = getArguments();
        return bundle == null ? Bundle.EMPTY : bundle;
    }

    @Override
    public <T extends View> T findView(@IdRes int id) {
        View view = viewSparse.get(id);
        if (view == null) {
            view = mRootView.findViewById(id);
            viewSparse.put(id, view);
        }
        return (T) view;
    }

    @Override
    public void showToast(String msg) {
        if (mActivity != null) mActivity.showToast(msg);
    }

    @Override
    public void printLogI(String msg) {
        Logger.i(TAG, msg);
    }

    @Override
    public void openActivity(Class<? extends Activity> clazz) {
        if (mActivity != null) mActivity.openActivity(clazz);
    }

    @Override
    public void openActivity(Class<? extends Activity> clazz, Bundle bundle) {
        if (mActivity != null) mActivity.openActivity(clazz, bundle);
    }

    //========================== BaseListener ==================================================

    //========================== LoadingListener ===============================================

    @Override
    public void setLoadingStatus(int loadingStatus, boolean isPull, boolean isCached) {
        if (mActivity != null) mActivity.setLoadingStatus(loadingStatus, isPull, isCached);
    }

    @Override
    public void setLoadingDialogVisible(boolean visible) {
        if (mActivity != null) mActivity.setLoadingDialogVisible(visible);
    }

    @Override
    public void setTitleLoadingProgressVisible(boolean visible) {
        if (mActivity != null) mActivity.setTitleLoadingProgressVisible(visible);
    }

    @Override
    public void dismissDialog() {
        if (mActivity != null) mActivity.dismissDialog(mDialog);
    }

    @Override
    public void dismissDialog(Dialog... dialogs) {
        if (mActivity != null) mActivity.dismissDialog(dialogs);
    }
    //========================== LoadingListener end ===============================================


    final public void setResult(int resultCode) {
        setResult(resultCode, null);
    }

    final public void setResult(int resultCode, Intent data) {
        if (mActivity != null) mActivity.setResult(resultCode, data);
    }

}