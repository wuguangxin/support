package com.wuguangxin.ui.test.tabhost;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.wuguangxin.R;
import com.wuguangxin.dialog.MyDialog;
import com.wuguangxin.ui.XinTabActivity;
import com.wuguangxin.utils.AndroidUtils;

import java.util.ArrayList;

/**
 * TabHost Demo
 * 
 * @author wuguangxin
 * @date: 2015-7-31 下午3:49:15
 */
public class TabHostDemoActivity extends XinTabActivity{
	private MyDialog mExitDialog;

	@Override
	public void onCreateView(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xin_tabhost_layout);
	}

	@Override
	public ArrayList<Tab> getTabList(){
		ArrayList<Tab> tabList = new ArrayList<XinTabActivity.Tab>();
		tabList.add(new Tab("Item1", android.R.drawable.sym_def_app_icon, Demo1Activity.class));
		tabList.add(new Tab("Item2", android.R.drawable.sym_def_app_icon, Demo2Activity.class));
		tabList.add(new Tab("Item3", android.R.drawable.sym_def_app_icon, Demo3Activity.class));
		tabList.add(new Tab("Item4", android.R.drawable.sym_def_app_icon, Demo4Activity.class));
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