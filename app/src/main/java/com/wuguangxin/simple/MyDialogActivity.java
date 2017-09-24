package com.wuguangxin.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wuguangxin.dialog.MyDialog;

public class MyDialogActivity extends AppCompatActivity implements View.OnClickListener {
    private MyDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydialog);
        setTitle("对话框");
        findViewById(R.id.MyDialog).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.MyDialog:
            if (mDialog != null) {
                mDialog.dismiss();
            }
            mDialog = new MyDialog(this);
            mDialog.setTitle("标题");
            mDialog.setMessage("这里是内容");
            mDialog.setPositiveButton("确认", null);
            mDialog.setNegativeButton("取消", null);
            mDialog.show();
            break;
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
