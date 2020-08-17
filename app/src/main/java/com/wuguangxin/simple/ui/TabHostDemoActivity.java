package com.wuguangxin.simple.ui;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuguangxin.dialog.XinDialog;
import com.wuguangxin.simple.R;
import com.wuguangxin.ui.XinTabActivity;
import com.wuguangxin.utils.AndroidUtils;

import java.util.ArrayList;

/**
 * Tab界面，重写父类
 */
public class TabHostDemoActivity extends XinTabActivity {
    private XinDialog mExitDialog;

    @Override
    public void onCreateView(){
        setContentView(R.layout.xin_tab_layout);
    }

    @Override
    public View getItemView(String name, int icon, int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab_layout, null);
        ImageView mIcon = view.findViewById(R.id.tab_icon);
        TextView mName = view.findViewById(R.id.tab_name);
        mIcon.setImageResource(icon);
        mName.setText(name);
        return view;
    }

    @Override
    public ArrayList<Tab> getTabList(){
        ArrayList<Tab> tabList = new ArrayList<>();
        tabList.add(new Tab("首页", R.drawable.main_tab_home, TabHost0_Activity.class));
        tabList.add(new Tab("产品", R.drawable.main_tab_product, TabHost1_Activity.class));
        tabList.add(new Tab("发现", R.drawable.main_tab_product, TabHost1_Activity.class));
        tabList.add(new Tab("我的", R.drawable.main_tab_assets, TabHost2_Activity.class));
        return tabList;
    }

    private void exitAppDialog(){
        if (mExitDialog != null) {
            mExitDialog.dismiss();
        }
        mExitDialog = new XinDialog(this);
        mExitDialog.setCancelable(false);
        mExitDialog.setTitle("提示").setMessage("确定退出应用程序吗？"); //
        mExitDialog.setNegativeButton("取消", null); //
        mExitDialog.setPositiveButton("退出", new XinDialog.OnClickDialogListener(){
            @Override
            public void onClick(View view, XinDialog dialog){
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