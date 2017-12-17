package com.wuguangxin.simple;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuguangxin.dialog.MyDialog;
import com.wuguangxin.ui.XinTabActivity;
import com.wuguangxin.utils.AndroidUtils;

import java.util.ArrayList;

/**
 * Tab界面，重写父类
 */
public class TabHostDemoActivity extends XinTabActivity {
    private MyDialog mExitDialog;

    @Override
    public void onCreateView(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xin_tab_layout);
    }

    @Override
    public View getItemView(String name, int icon, int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab_layout, null);
        ImageView mIcon = (ImageView) view.findViewById(R.id.tab_icon);
        TextView mName = (TextView) view.findViewById(R.id.tab_name);
        mIcon.setImageResource(icon);
        mName.setText(name);
        return view;
    }

    @Override
    public ArrayList<Tab> getTabList(){
        ArrayList<Tab> tabList = new ArrayList<>();
        tabList.add(new Tab("首页", R.drawable.main_tab_home, TabHost0_Activity.class));
        tabList.add(new Tab("标的", R.drawable.main_tab_product, TabHost1_Activity.class));
        tabList.add(new Tab("发现", R.drawable.main_tab_product, TabHost1_Activity.class));
        tabList.add(new Tab("我", R.drawable.main_tab_assets, TabHost2_Activity.class));
        return tabList;
    }

    private void exitAppDialog(){
        if (mExitDialog != null) {
            mExitDialog.dismiss();
        }
        mExitDialog = new MyDialog(this);
        mExitDialog.setCancelable(false);
        mExitDialog.setTitle("提示").setMessage("确定退出应用程序吗？"); //
        mExitDialog.setNegativeButton("取消", null); //
        mExitDialog.setPositiveButton("退出", new MyDialog.OnClickDialogListener(){
            @Override
            public void onClick(View view, MyDialog dialog){
                dialog.dismiss();
            }
        });
        mExitDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (AndroidUtils.isPressedKeycodeBack(keyCode, event)) {
            exitAppDialog();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mExitDialog != null) {
            mExitDialog.dismiss();
            mExitDialog = null;
        }
    }
}