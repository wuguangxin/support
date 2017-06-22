package com.wuguangxin.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wuguangxin.dialog.MyDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.ToastUtils).setOnClickListener(this);
        findViewById(R.id.MyDialog).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.ToastUtils:
            openActivity(BActivity.class);
            break;
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

    public void openActivity(Class<? extends Activity> clazz){
        startActivity(new Intent(this, clazz));
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
