package com.wuguangxin.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.wuguangxin.dialog.LoadingDialog;
import com.wuguangxin.dialog.XinDialog;
import com.wuguangxin.support.R;
import com.wuguangxin.utils.AndroidUtils;
import com.wuguangxin.utils.AnimUtil;
import com.wuguangxin.utils.Logger;
import com.wuguangxin.utils.PermissionUtils;
import com.wuguangxin.utils.SlidingFinishHelper;
import com.wuguangxin.utils.SoftHideKeyBoardUtil;
import com.wuguangxin.utils.StatusBarUtils;
import com.wuguangxin.utils.ToastUtils;
import com.wuguangxin.utils.ViewUtils;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类
 * Created by wuguangxin on 2015/4/1
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseInterface {
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
    private int REQUEST_PERMISSIONS = 100;

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        mContext = this;
        screenWidth = AndroidUtils.getScreenWidth(this);
        mLayoutManager = new LayoutManager(this, R.layout.activity_base); // 布局管理器
        mLayoutManager.setContentView(getLayoutRes());

        setContentView(mLayoutManager.getRootLayout());
        mUnBinder = ButterKnife.bind(this, this);

        mLayoutManager.setErrorLayoutListener(v -> onClickErrorLayout());

        mLoadingDialog = new LoadingDialog(this); // 加载对话框
        mSlidingFinishHelper = new SlidingFinishHelper(this);

        setTitle(getClass().getSimpleName());

        StatusBarUtils.setImmersionStatusBar(this, getResources().getColor(R.color.xin_titlebar_background));
        StatusBarUtils.setStatusBarFontColor(this, false);
        SoftHideKeyBoardUtil.assistActivity(this);

        initArguments(getIntent());
        initView(savedInstanceState);
        initListener();
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
//        super.startActivity(intent);
        // 所有使用startActivity打开的，都换成startActivityForResult打开的Activity切换动画比较好看，并且requestCode=0
        super.startActivityForResult(intent, 0);
    }

    @Override
    public void openWeb(String title, String url) {
//        WebConfig.create().setTitle(title).setBaseUrl(url).open(this);
    }

    /**
     * 状态。
     */
    public interface State {
        /** 开始 */
        int START = 100;
        /** 成功 */
        int SUCCESS = 200;
        /** 取消 */
        int CANCEL = 300;
        /** 失败 */
        int FAILURE = 400;
        /** 完成 */
        int FINISH = 900;
    }

    @Override
    public void setLoadingStatus(int state, boolean isCache) {
        switch (state) {
        case State.START:
            if (isCache) {
                setTitleLoadingProgressVisible(true);
            } else {
                setLoadingDialogVisible(true);
            }
            break;
        case State.SUCCESS:
        case State.FAILURE:
            getLayoutManager().setErrorLayoutVisible(!isCache && state != State.SUCCESS);
            if (isCache) {
                setTitleLoadingProgressVisible(false);
            } else {
                setLoadingDialogVisible(false);
            }
            break;
        case State.CANCEL:
        case State.FINISH:
            setTitleLoadingProgressVisible(false);
            setLoadingDialogVisible(false);
            break;
        }
    }

    public void setLoadingStatus(int httpState, boolean isGet, boolean isCache) {
        switch (httpState) {
        case State.START:
            if (isGet && isCache) {
                setTitleLoadingProgressVisible(true);
            } else {
                setLoadingDialogVisible(true);
            }
            break;
        case State.SUCCESS:
        case State.FAILURE:
            if (isGet) {
                getLayoutManager().setErrorLayoutVisible(!isCache && httpState != State.SUCCESS);
            }
        case State.CANCEL:
        case State.FINISH:
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

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public void setSoftInputVisible(Activity activity) {
        if (activity != null) {
            View currentFocusView = activity.getCurrentFocus();
            if (currentFocusView != null) {
                ((InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(currentFocusView.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
            if (currentFocusView instanceof EditText) {
                ViewUtils.setFocusPosition((EditText) currentFocusView);
            }
        }
    }

    /**
     * 计算LayoutParams的leftMargin值
     *
     * @param menuCountWidth menu父布局的宽度
     * @param menuSize menu数量
     * @param nextPosition 将要获取焦点的menu位置
     * @param position 当前menu位置
     * @param offset 偏移量
     * @return
     */
    public int getLeftMargin(int menuCountWidth, int menuSize, int nextPosition, int position, float offset) {
        int width = menuCountWidth / menuSize; // 计算每个menu的宽度
        int curLeft = nextPosition * width; // 计算当前距左的距离
        for (int i = 0; i < menuSize; i++) {
            if (nextPosition == i && position == i) { // 左 > 右 滑
                return (int) (offset * width + curLeft);
            } else if (nextPosition == i + 1 && position == i) { // 右 > 左 滑
                return (int) (-(1 - offset) * width + curLeft);
            }
        }
        return 0;
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


    //======================权限============================================

    /**
     * 6.0以上系统检查是否已全部授权
     *
     * @param permission 权限数组
     * @return true已全部授权，false有未授权的
     */
    public boolean checkPermission(String... permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            // 检查未授权的权限
            String[] permissions = PermissionUtils.checkUnAcceptPermission(this, permission);
            if (permissions != null && permissions.length > 0) {
                printLogI("未获取的权限 = " + Arrays.toString(permissions));
                PermissionUtils.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (Build.VERSION.SDK_INT >= 23) {
                // 获得被拒绝的权限
                String deniedPermission = null;
                for (int i = 0; i < permissions.length; i++) {
                    printLogI("permissions: " + permissions[i] + " = " + grantResults[i]);
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        deniedPermission = permissions[i];
                    }
                }
                if (TextUtils.isEmpty(deniedPermission)) {
                    checkPermission();
                } else {
                    // 这个方法大概意思是当请求一个权限而被用户拒绝并且勾选不再提示时，返回false（当然返回false的情况也有多种）
                    boolean flag = shouldShowRequestPermissionRationale(deniedPermission);
                    if (!flag) {
                        // 被用户拒绝后系统不再提示，这里由APP弹出提示去设置
                        showPermissionDialog(mContext, "未授予必须权限，请先去授权");
                    } else {
//                        finish();
                    }
                }
            }
        }
    }

    /**
     * 弹出提示设置权限对话框
     *
     * @param context 上下文
     * @param msg 消息
     */
    public void showPermissionDialog(final Context context, final String msg) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = new XinDialog(context);
        mDialog.setTitle("提示");
//        StringBuilder sb = new StringBuilder("需要以下权限：\n");
//        for (String permission : permissions) {
//            sb.append("【").append(Constants.getPermissionDesc(permission)).append("】\n");
//        }
        mDialog.setMessage(msg);
        // 给权限名称加红色
//        for (String permission : permissions) {
//            FontUtils.setFontColor(mDialog.getMessageView(), Constants.getPermissionDesc(permission), Color.RED);
//        }
        mDialog.setMessageTextSize(14);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessageTextColor(0xff555555);
        mDialog.getMessageView().setLineSpacing(1F, 1.3F);
        mDialog.setNegativeButton("取消", new XinDialog.OnClickDialogListener() {
            @Override
            public void onClick(View view, XinDialog dialog) {
                dialog.dismiss();
            }
        });
        mDialog.setPositiveButton("去授权", new XinDialog.OnClickDialogListener() {
            @Override
            public void onClick(View view, XinDialog dialog) {
                dialog.dismiss();
                getAppDetailSettingIntent(context);
                finish();
            }
        });
        mDialog.show();
    }

    /**
     * 根据手机品牌，跳转到对应的权限设置界面
     *
     * @param context 上下文
     */
    private void getAppDetailSettingIntent(Context context) {
        // vivo
        // 点击设置图标>加速白名单>我的app
        // 点击软件管理>软件管理权限>软件>我的app>信任该软件
        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (appIntent != null) {
            context.startActivity(appIntent);
            return;
        }

        // oppo
        // 点击设置图标>应用权限管理>按应用程序管理>我的app>我信任该应用
        // 点击权限隐私>自启动管理>我的app
        appIntent = context.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");
        if (appIntent != null) {
            context.startActivity(appIntent);
            return;
        }

        // 默认品牌
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }
}
