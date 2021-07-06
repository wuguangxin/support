package com.wuguangxin.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.wuguangxin.base.FragmentTask;
import com.wuguangxin.base.LayoutManager;
import com.wuguangxin.base.LoadingStatus;
import com.wuguangxin.base.TitleBar;
import com.wuguangxin.dialog.LoadingDialog;
import com.wuguangxin.dialog.XinDialog;
import com.wuguangxin.listener.BaseInterface;
import com.wuguangxin.support.R;
import com.wuguangxin.utils.AndroidUtils;
import com.wuguangxin.utils.AnimUtil;
import com.wuguangxin.utils.Logger;
import com.wuguangxin.utils.SlidingFinishHelper;
import com.wuguangxin.utils.SoftHideKeyBoardUtil;
import com.wuguangxin.utils.StatusBarUtils;
import com.wuguangxin.utils.ToastUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类
 * Created by wuguangxin on 2015/4/1
 */
public abstract class XinBaseActivity extends AppCompatActivity implements BaseInterface {
    protected SmartRefreshLayout mRefreshLayout;
    protected LayoutManager mLayoutManager;
    protected LoadingDialog mLoadingDialog;
    protected XinDialog mDialog;
    protected Context mContext;
    private Unbinder mUnBinder;
    protected String TAG;
    protected int screenWidth;
    private boolean slidingFinish;
    private SlidingFinishHelper mSlidingFinishHelper;

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        mContext = this;
        screenWidth = AndroidUtils.getScreenWidth(this);
        mLoadingDialog = new LoadingDialog(this); // 加载对话框
        mSlidingFinishHelper = new SlidingFinishHelper(this);

        mLayoutManager = new LayoutManager(this, R.layout.activity_base); // 布局管理器
        mLayoutManager.setContentView(getLayoutRes());
        mLayoutManager.setErrorLayoutListener(v -> onClickErrorLayout());

        setContentView(mLayoutManager.getRootLayout());
        mUnBinder = ButterKnife.bind(this, this);

        getTitleBar().setTitle(getClass().getSimpleName());
        SoftHideKeyBoardUtil.assistActivity(this);

        setImmersionStatusBar(this);

        initArguments(getIntent());
        initView(savedInstanceState);
        initListener();
    }

    /**
     * 设置沉浸式状态栏
     *
     * @param activity
     */
    public void setImmersionStatusBar(Activity activity) {
        StatusBarUtils.setImmersionStatusBar(activity, getResources().getColor(R.color.xin_titlebar_background));
        StatusBarUtils.setStatusBarFontColor(activity, false);
    }

    /**
     * 初始化一些传参
     *
     * @param intent
     */
    public void initArguments(Intent intent) {
    }

    /**
     * 是否开启滑动关闭 Activity
     */
    public void setSlidingFinish(boolean slidingFinish) {
        this.slidingFinish = slidingFinish;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (slidingFinish) {
            return mSlidingFinishHelper.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onDestroy() {
        // 关闭所有对话框dismiss
        getTitleBar().setLoadAnimVisible(false);
        dismissDialog();
        // 销毁Presenter
//        if (mPresenter != null) {
//            mPresenter.onDestroy();
//            mPresenter = null;
//        }
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
        if (viewSparse != null) {
            viewSparse.clear();
            viewSparse= null;
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        AnimUtil.animClose(this);
    }

    /**
     * 当点击错误界面时执行的方法。
     * （使用场景：某个界面第一次请求数据并发生网络异常时，显示错误界面，点击错误界面时，执行该方法，例如重新请求数据。）
     */
    public void onClickErrorLayout() {
        initData();
    }

    /**
     * 获取当前使用的 SmartRefreshLayout
     */
    protected SmartRefreshLayout getRefreshLayout() {
        if (mRefreshLayout == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            mRefreshLayout = (SmartRefreshLayout) inflater.inflate(R.layout.xin_def_refresh_layout, null);
        }
        return mRefreshLayout;
    }

    public LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public TitleBar getTitleBar() {
        return getLayoutManager().getTitleBar();
    }

    /**
     * 取消透明状态栏样式
     */
    public void clearImmersionStatusBar() {
        AndroidUtils.clearImmersionStatusBar(this);
    }

    /**
     * 该方法会将传入的layoutRes布局放入到SmartRefreshLayout里，实现下拉刷新，这只是少写一层代码而已。
     *
     * @param layoutRes
     */
    public void setContentViewOnRefreshLayout(int layoutRes) {
        View view = LayoutInflater.from(mContext).inflate(layoutRes, getRefreshLayout());
        setContentView(view);
    }

//    @Override
//    public void setContentView(int layoutRes) {
//        getLayoutManager().setContentView(layoutRes);
//        mUnBinder = ButterKnife.bind(this, this);
//    }

//    @Override
//    public void setContentView(View view) {
//        getLayoutManager().setContentView(view);
//        mUnBinder = ButterKnife.bind(this, this);
//    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        getTitleBar().setTitle(title);
    }

    @Override
    public void setTitle(int title) {
        super.setTitle(title);
        getTitleBar().setTitle(title);
    }

    @Override
    public void setTitleColor(int color) {
        getTitleBar().getTitleView().setTextColor(color);
    }

    protected void setTitleBarVisibility(boolean visibility) {
        getTitleBar().setVisibility(visibility);
    }

    protected void setBodyVisibility(boolean visibility) {
        getLayoutManager().setBodyVisibility(visibility);
    }

    // ============================ BaseViewListener ===============================================
    @Override
    public Context getContext() {
        return mContext;
    }

    private SparseArray<View> viewSparse = new SparseArray<>();

    @Override
    public <T extends View> T findView(int id) {
        View view = viewSparse.get(id);
        if (view == null) {
            view = findViewById(id);
            viewSparse.put(id, view);
        }
        return (T) view;
    }

    @Override
    public void showToast(String text) {
        ToastUtils.showToast(getContext(), text);
    }

    @Override
    public void printLogI(String text) {
        Logger.i(getContext(), text);
    }

    @Override
    public void openActivity(Class<? extends Activity> clazz) {
        openActivity(clazz, null);
    }

    @Override
    public void openActivity(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(getContext(), clazz);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent) {
        // super.startActivity(intent);
        // 换成startActivityForResult打开的Activity切换动画比较好看，并且requestCode=0
        super.startActivityForResult(intent, 0);
    }

    @Override
    public void setLoadingStatus(int loadingStatus, boolean isPull, boolean isCached) {
        switch (loadingStatus) {
        case LoadingStatus.START:
            if (isPull && isCached) {
                setTitleLoadingProgressVisible(true);
            } else {
                setLoadingDialogVisible(true);
            }
            break;
        case LoadingStatus.SUCCESS:
        case LoadingStatus.FAILURE:
            // 是获取数据时，且没有缓存数据，且网获取失败，则可显示网络错误界面
            if (isPull && !isCached && loadingStatus == LoadingStatus.FAILURE) {
                getLayoutManager().setErrorLayoutVisible(true);
            }
            if (isCached) {
                setTitleLoadingProgressVisible(false);
            } else {
                setLoadingDialogVisible(false);
            }
            break;
        case LoadingStatus.CANCEL:
        case LoadingStatus.FINISH:
            setTitleLoadingProgressVisible(false);
            setLoadingDialogVisible(false);
            break;
        }
    }

    @Override
    public void setLoadingDialogVisible(boolean visible) {
        if (mLoadingDialog != null) {
            mLoadingDialog.setVisible(visible);
        }
    }

    @Override
    public void setTitleLoadingProgressVisible(boolean isStart) {
        getTitleBar().setLoadAnimVisible(isStart);
    }

    @Override
    public void dismissDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void dismissDialog(Dialog... dialogs) {
        for (Dialog dialog : dialogs) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 让最后打开的那个fragment执行
        Fragment topFragment = FragmentTask.getInstance().getTopTask();
        if (topFragment != null) {
            topFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
