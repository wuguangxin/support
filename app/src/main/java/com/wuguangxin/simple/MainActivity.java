package com.wuguangxin.simple;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wuguangxin.dialog.MyDialog;

public class MainActivity extends AppCompatActivity {

    private MyDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = new TextView(this);
        textView.setText("TEST");
        textView.setBackgroundColor(getResources().getColor(R.color.red));

        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = new MyDialog(this, AlertDialog.THEME_HOLO_LIGHT);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
//        mDialog.setContentView(textView);
        mDialog.setView(textView);
        mDialog.show();
    }
}
