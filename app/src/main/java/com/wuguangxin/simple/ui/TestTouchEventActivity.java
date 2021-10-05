package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.MotionEvent;

import com.wuguangxin.simple.R;
import com.wuguangxin.simple.databinding.ActivityTestTouchEventBinding;

public class TestTouchEventActivity extends BaseActivity {
    private ActivityTestTouchEventBinding mRootBinding;

    // activity
    private boolean activityDispatchSuper = true;
    private boolean activityDispatchReturn;
    private boolean activityDispatchReturnSuper = true;

    private boolean activityTouchSuper = true;
    private boolean activityTouchReturn;
    private boolean activityTouchReturnSuper = true;

    // ViewGroup
    private boolean groupDispatchSuper = true;
    private boolean groupDispatchReturn;
    private boolean groupDispatchReturnSuper = true;

    private boolean groupInterceptSuper = true;
    private boolean groupInterceptReturn;
    private boolean groupInterceptReturnSuper = true;

    private boolean groupTouchSuper = true;
    private boolean groupTouchReturn;
    private boolean groupTouchReturnSuper = true;

    // View
    private boolean viewDispatchSuper = true;
    private boolean viewDispatchReturn;
    private boolean viewDispatchReturnSuper = true;

    private boolean viewTouchSuper = true;
    private boolean viewTouchReturn;
    private boolean viewTouchReturnSuper = true;

    public TestTouchEventActivity() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_test_touch_event;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setBackVisibility(false);
        mRootBinding = ActivityTestTouchEventBinding.bind(getLayoutManager().getBodyLayout());

        mRootBinding.viewGroup.setOnClickListener(v -> printLogI("点击了 " + v.getTag()));
        mRootBinding.childView.setOnClickListener(v -> printLogI("点击了 " + v.getTag()));

        // activity
        // ----dispatchTouchEvent()
        mRootBinding.activityDispatchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityDispatchSuper = isChecked;
            mRootBinding.activityDispatchReturnSuper.setEnabled(isChecked);
            if (!isChecked) mRootBinding.activityDispatchReturnSuper.setChecked(false);
        });
        mRootBinding.activityDispatchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityDispatchReturnSuper = isChecked;
            if (isChecked) mRootBinding.activityDispatchReturn.setChecked(false);
        });
        mRootBinding.activityDispatchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityDispatchReturn = isChecked;
            if (isChecked) mRootBinding.activityDispatchReturnSuper.setChecked(false);
        });
        // ----onTouchEvent()
        mRootBinding.activityTouchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityTouchSuper = isChecked;
            mRootBinding.activityTouchReturnSuper.setEnabled(isChecked);
            if (!isChecked) mRootBinding.activityTouchReturnSuper.setChecked(false);
        });
        mRootBinding.activityTouchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityTouchReturnSuper = isChecked;
            if (isChecked) mRootBinding.activityTouchReturn.setChecked(false);
        });
        mRootBinding.activityTouchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityTouchReturn = isChecked;
            if (isChecked) mRootBinding.activityTouchReturnSuper.setChecked(false);
        });

        // ViewGroup
        // ----dispatchTouchEvent()
        mRootBinding.groupDispatchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.viewGroup.groupDispatchSuper = isChecked;
            mRootBinding.groupDispatchReturnSuper.setEnabled(isChecked);
            if (!isChecked) mRootBinding.groupDispatchReturnSuper.setChecked(false);
        });
        mRootBinding.groupDispatchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.viewGroup.groupDispatchReturnSuper = isChecked;
            if (isChecked) mRootBinding.groupDispatchReturn.setChecked(false);
        });
        mRootBinding.groupDispatchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.viewGroup.groupDispatchReturn = isChecked;
            if (isChecked) mRootBinding.groupDispatchReturnSuper.setChecked(false);
        });
        // ----onInterceptTouchEvent()
        mRootBinding.groupInterceptSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.viewGroup.groupInterceptSuper = isChecked;
            mRootBinding.groupInterceptReturnSuper.setEnabled(isChecked);
            if (!isChecked) mRootBinding.groupInterceptReturnSuper.setChecked(false);
        });
        mRootBinding.groupInterceptReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.viewGroup.groupInterceptReturnSuper = isChecked;
            if (isChecked) mRootBinding.groupInterceptReturn.setChecked(false);
        });
        mRootBinding.groupInterceptReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.viewGroup.groupInterceptReturn = isChecked;
            if (isChecked) mRootBinding.groupInterceptReturnSuper.setChecked(false);
        });

        // ----onTouchEvent()
        mRootBinding.groupTouchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.viewGroup.groupTouchSuper = isChecked;
            mRootBinding.groupTouchReturnSuper.setEnabled(isChecked);
            if (!isChecked) mRootBinding.groupTouchReturnSuper.setChecked(false);
        });
        mRootBinding.groupTouchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.viewGroup.groupTouchReturn = isChecked;
            if (isChecked) mRootBinding.groupTouchReturnSuper.setChecked(false);
        });
        mRootBinding.groupTouchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.viewGroup.groupTouchReturnSuper = isChecked;
            if (isChecked) mRootBinding.groupTouchReturn.setChecked(false);
        });

        // View

        // ----dispatchTouchEvent()
        mRootBinding.viewDispatchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.childView.viewDispatchSuper = isChecked;
            mRootBinding.viewDispatchReturnSuper.setEnabled(isChecked);
            if (!isChecked) mRootBinding.viewDispatchReturnSuper.setChecked(false);
        });
        mRootBinding.viewDispatchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.childView.viewDispatchReturnSuper = isChecked;
            if (isChecked) mRootBinding.viewDispatchReturn.setChecked(false);
        });
        mRootBinding.viewDispatchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.childView.viewDispatchReturn = isChecked;
            if (isChecked) mRootBinding.viewDispatchReturnSuper.setChecked(false);
        });

        // ----onTouchEvent()
        mRootBinding.viewTouchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.childView.viewTouchSuper = isChecked;
            mRootBinding.viewTouchReturnSuper.setEnabled(isChecked);
            if (!isChecked) mRootBinding.viewTouchReturnSuper.setChecked(false);
        });
        mRootBinding.viewTouchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.childView.viewTouchReturnSuper = isChecked;
            if (isChecked) mRootBinding.viewTouchReturn.setChecked(false);
        });
        mRootBinding.viewTouchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRootBinding.childView.viewTouchReturn = isChecked;
            if (isChecked) mRootBinding.viewTouchReturnSuper.setChecked(false);
        });


        // activity
        mRootBinding.activityDispatchSuper.setChecked(activityDispatchSuper);
        mRootBinding.activityDispatchReturn.setChecked(activityDispatchReturn);
        mRootBinding.activityDispatchReturnSuper.setChecked(activityDispatchReturnSuper);
        mRootBinding.activityTouchSuper.setChecked(activityTouchSuper);
        mRootBinding.activityTouchReturn.setChecked(activityTouchReturn);
        mRootBinding.activityTouchReturnSuper.setChecked(activityTouchReturnSuper);
        // ViewGroup
        mRootBinding.groupDispatchSuper.setChecked(groupDispatchSuper);
        mRootBinding.groupDispatchReturn.setChecked(groupDispatchReturn);
        mRootBinding.groupDispatchReturnSuper.setChecked(groupDispatchReturnSuper);
        mRootBinding.groupInterceptSuper.setChecked(groupInterceptSuper);
        mRootBinding.groupInterceptReturn.setChecked(groupInterceptReturn);
        mRootBinding.groupInterceptReturnSuper.setChecked(groupInterceptReturnSuper);
        mRootBinding.groupTouchSuper.setChecked(groupTouchSuper);
        mRootBinding.groupTouchReturn.setChecked(groupTouchReturn);
        mRootBinding.groupTouchReturnSuper.setChecked(groupTouchReturnSuper);
        // View
        mRootBinding.viewDispatchSuper.setChecked(viewDispatchSuper);
        mRootBinding.viewDispatchReturn.setChecked(viewDispatchReturn);
        mRootBinding.viewDispatchReturnSuper.setChecked(viewDispatchReturnSuper);
        mRootBinding.viewTouchSuper.setChecked(viewTouchSuper);
        mRootBinding.viewTouchReturn.setChecked(viewTouchReturn);
        mRootBinding.viewTouchReturnSuper.setChecked(viewTouchReturnSuper);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (activityDispatchSuper) {
            boolean b = super.dispatchTouchEvent(event);
            println("super.dispatchTouchEvent() " + b);
            if (activityDispatchReturnSuper) {
                activityDispatchReturn = b;
            }
        }
        println("dispatchTouchEvent " + activityDispatchReturn);
        return activityDispatchReturn;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (activityTouchSuper) {
            boolean b = super.onTouchEvent(event);
            println("super.onTouchEvent() " + b);
            if (activityTouchReturnSuper) {
                activityTouchReturn = b;
            }
        }
        println("onTouchEvent " + activityTouchReturn);
        return activityTouchReturn;
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void setData() {
    }
}
