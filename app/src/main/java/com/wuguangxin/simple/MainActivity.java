package com.wuguangxin.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.wuguangxin.dialog.MyDialog;
import com.wuguangxin.view.ItemView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyDialog mDialog;
    private ItemView mItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.ToastUtils).setOnClickListener(this);
        findViewById(R.id.MyDialog).setOnClickListener(this);

        mItemView = (ItemView) findViewById(R.id.itemView);
        mItemView.setGravity(Gravity.CENTER);

        Log.e("aa", "Gravity.CENTER===" + Gravity.CENTER);

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
