package com.wuguangxin.simple.ui;

import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class ViewEditor {
    private final static String TAG = ViewEditor.class.getSimpleName();
    private final Button button;
    private final CharSequence oldText;
    private final long startTime;

    private ViewEditor(View view, long delayMillis) {
        this.button = (Button) view;
        this.oldText = button.getText();
        this.button.setText("执行中...");
        this.button.setEnabled(false);
        this.startTime = System.currentTimeMillis();

        if (delayMillis > 0) {
            Handler handler = new Handler();
            handler.postDelayed(this::release, delayMillis);
        }
    }

    public static ViewEditor with(View view) {
        return with(view, 0);
    }
    public static ViewEditor with(View view, long delayMillis) {
        return new ViewEditor(view, delayMillis);
    }

    public void release() {
        if (!this.button.isEnabled()) {
            this.button.setText(oldText);
            this.button.setEnabled(true);
            long total = System.currentTimeMillis() - startTime;
            System.out.println(TAG + ": " + oldText + " 执行耗时：" + total);
        }
    }
}