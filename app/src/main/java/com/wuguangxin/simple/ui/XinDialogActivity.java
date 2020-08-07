package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.View;

import com.wuguangxin.dialog.XinDialog;
import com.wuguangxin.simple.R;

import butterknife.OnClick;

public class XinDialogActivity extends BaseActivity {
    private XinDialog mDialog;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_mydialog;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("对话框");
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.dialog})
    public void onClicked(View v) {
        int id = v.getId();
        if (id == R.id.dialog) {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            mDialog = new XinDialog(this);
            mDialog.setTitle("标题");
            mDialog.setMessage("这里是内容");
            mDialog.setPositiveButton("确认", null);
            mDialog.setNegativeButton("取消", null);
            mDialog.show();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDialog != null){
            mDialog.cancel();
            mDialog = null;
        }
    }
}
