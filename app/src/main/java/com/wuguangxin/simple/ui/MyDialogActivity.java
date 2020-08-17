package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.View;

import com.wuguangxin.dialog.XinDialog;
import com.wuguangxin.simple.R;

import androidx.appcompat.app.AppCompatActivity;

public class MyDialogActivity extends AppCompatActivity implements View.OnClickListener {
    private XinDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydialog);
        setTitle("对话框");
        findViewById(R.id.dialog).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
    protected void onDestroy() {
        super.onDestroy();
        if(mDialog != null){
            mDialog.cancel();
            mDialog = null;
        }
    }
}
