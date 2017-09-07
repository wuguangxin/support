package com.wuguangxin.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

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
        mItemView.setOnClickListener(this);
        mItemView.getKeyView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点了Key", Toast.LENGTH_SHORT).show();
            }
        });
        mItemView.getValueView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("AAA", "LEFT = " + Gravity.LEFT);
                Log.e("AAA", "TOP = " + Gravity.TOP);
                Log.e("AAA", "RIGHT = " + Gravity.RIGHT);
                Log.e("AAA", "BOTTOM = " + Gravity.BOTTOM);
                Log.e("AAA", "CENTER = " + Gravity.CENTER);
                Log.e("AAA", "CENTER_HORIZONTAL = " + Gravity.CENTER_HORIZONTAL);
                Log.e("AAA", "CENTER_VERTICAL = " + Gravity.CENTER_VERTICAL);
                Toast.makeText(MainActivity.this, "点了Value", Toast.LENGTH_SHORT).show();
//                E/AAA: LEFT = 3
//                E/AAA: TOP = 48
//                E/AAA: RIGHT = 5
//                E/AAA: BOTTOM = 80
//                E/AAA: CENTER = 17
//                E/AAA: CENTER_HORIZONTAL = 1
//                E/AAA: CENTER_VERTICAL = 16


            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.itemView:
            Toast.makeText(MainActivity.this, "点了 ItemView", Toast.LENGTH_SHORT).show();
            break;
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
