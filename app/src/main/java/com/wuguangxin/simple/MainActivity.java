package com.wuguangxin.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wuguangxin.dialog.MyDialog;

public class MainActivity extends AppCompatActivity {

    private MyDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = new MyDialog(this);
        mDialog.setTitle("提示");
        mDialog.setMessage("欢迎使用");
        mDialog.setPositiveButton("确认", null);
        mDialog.setNegativeButton("取消", null);
        mDialog.show();
    }
}
