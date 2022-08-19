package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.View;

import com.wuguangxin.dialog.XinDialog;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.databinding.ActivityMydialogBinding;

import butterknife.OnClick;

public class MyDialogActivity extends BaseActivity<ActivityMydialogBinding> {
    private XinDialog mDialog;

    @Override
    public int getLayoutId() {
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
    public void onClick(View v) {
        mDialog = XinDialog.with(this, mDialog);
        mDialog.setTitle("标题");
        mDialog.setMessage("这里是内容");
        mDialog.setPositiveButton("确认");
        mDialog.setNegativeButton("取消");
        mDialog.show();
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
