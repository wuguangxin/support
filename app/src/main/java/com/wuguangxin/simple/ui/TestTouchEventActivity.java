package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.MotionEvent;

import com.wuguangxin.simple.R;
import com.wuguangxin.simple.databinding.ActivityTestTouchEventBinding;

public class TestTouchEventActivity extends BaseActivity<ActivityTestTouchEventBinding> {
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
    public int getLayoutId() {
        return R.layout.activity_test_touch_event;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitleLayout(R.id.titleLayout);

        binding.viewGroup.setOnClickListener(v -> log("点击了 " + v.getTag()));
        binding.childView.setOnClickListener(v -> log("点击了 " + v.getTag()));

        // activity
        // ----dispatchTouchEvent()
        binding.activityDispatchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityDispatchSuper = isChecked;
            binding.activityDispatchReturnSuper.setEnabled(isChecked);
            if (!isChecked) binding.activityDispatchReturnSuper.setChecked(false);
        });
        binding.activityDispatchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityDispatchReturnSuper = isChecked;
            if (isChecked) binding.activityDispatchReturn.setChecked(false);
        });
        binding.activityDispatchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityDispatchReturn = isChecked;
            if (isChecked) binding.activityDispatchReturnSuper.setChecked(false);
        });
        // ----onTouchEvent()
        binding.activityTouchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityTouchSuper = isChecked;
            binding.activityTouchReturnSuper.setEnabled(isChecked);
            if (!isChecked) binding.activityTouchReturnSuper.setChecked(false);
        });
        binding.activityTouchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityTouchReturnSuper = isChecked;
            if (isChecked) binding.activityTouchReturn.setChecked(false);
        });
        binding.activityTouchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityTouchReturn = isChecked;
            if (isChecked) binding.activityTouchReturnSuper.setChecked(false);
        });

        // ViewGroup
        // ----dispatchTouchEvent()
        binding.groupDispatchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.viewGroup.groupDispatchSuper = isChecked;
            binding.groupDispatchReturnSuper.setEnabled(isChecked);
            if (!isChecked) binding.groupDispatchReturnSuper.setChecked(false);
        });
        binding.groupDispatchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.viewGroup.groupDispatchReturnSuper = isChecked;
            if (isChecked) binding.groupDispatchReturn.setChecked(false);
        });
        binding.groupDispatchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.viewGroup.groupDispatchReturn = isChecked;
            if (isChecked) binding.groupDispatchReturnSuper.setChecked(false);
        });
        // ----onInterceptTouchEvent()
        binding.groupInterceptSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.viewGroup.groupInterceptSuper = isChecked;
            binding.groupInterceptReturnSuper.setEnabled(isChecked);
            if (!isChecked) binding.groupInterceptReturnSuper.setChecked(false);
        });
        binding.groupInterceptReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.viewGroup.groupInterceptReturnSuper = isChecked;
            if (isChecked) binding.groupInterceptReturn.setChecked(false);
        });
        binding.groupInterceptReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.viewGroup.groupInterceptReturn = isChecked;
            if (isChecked) binding.groupInterceptReturnSuper.setChecked(false);
        });

        // ----onTouchEvent()
        binding.groupTouchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.viewGroup.groupTouchSuper = isChecked;
            binding.groupTouchReturnSuper.setEnabled(isChecked);
            if (!isChecked) binding.groupTouchReturnSuper.setChecked(false);
        });
        binding.groupTouchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.viewGroup.groupTouchReturn = isChecked;
            if (isChecked) binding.groupTouchReturnSuper.setChecked(false);
        });
        binding.groupTouchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.viewGroup.groupTouchReturnSuper = isChecked;
            if (isChecked) binding.groupTouchReturn.setChecked(false);
        });

        // View

        // ----dispatchTouchEvent()
        binding.viewDispatchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.childView.viewDispatchSuper = isChecked;
            binding.viewDispatchReturnSuper.setEnabled(isChecked);
            if (!isChecked) binding.viewDispatchReturnSuper.setChecked(false);
        });
        binding.viewDispatchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.childView.viewDispatchReturnSuper = isChecked;
            if (isChecked) binding.viewDispatchReturn.setChecked(false);
        });
        binding.viewDispatchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.childView.viewDispatchReturn = isChecked;
            if (isChecked) binding.viewDispatchReturnSuper.setChecked(false);
        });

        // ----onTouchEvent()
        binding.viewTouchSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.childView.viewTouchSuper = isChecked;
            binding.viewTouchReturnSuper.setEnabled(isChecked);
            if (!isChecked) binding.viewTouchReturnSuper.setChecked(false);
        });
        binding.viewTouchReturnSuper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.childView.viewTouchReturnSuper = isChecked;
            if (isChecked) binding.viewTouchReturn.setChecked(false);
        });
        binding.viewTouchReturn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.childView.viewTouchReturn = isChecked;
            if (isChecked) binding.viewTouchReturnSuper.setChecked(false);
        });


        // activity
        binding.activityDispatchSuper.setChecked(activityDispatchSuper);
        binding.activityDispatchReturn.setChecked(activityDispatchReturn);
        binding.activityDispatchReturnSuper.setChecked(activityDispatchReturnSuper);
        binding.activityTouchSuper.setChecked(activityTouchSuper);
        binding.activityTouchReturn.setChecked(activityTouchReturn);
        binding.activityTouchReturnSuper.setChecked(activityTouchReturnSuper);
        // ViewGroup
        binding.groupDispatchSuper.setChecked(groupDispatchSuper);
        binding.groupDispatchReturn.setChecked(groupDispatchReturn);
        binding.groupDispatchReturnSuper.setChecked(groupDispatchReturnSuper);
        binding.groupInterceptSuper.setChecked(groupInterceptSuper);
        binding.groupInterceptReturn.setChecked(groupInterceptReturn);
        binding.groupInterceptReturnSuper.setChecked(groupInterceptReturnSuper);
        binding.groupTouchSuper.setChecked(groupTouchSuper);
        binding.groupTouchReturn.setChecked(groupTouchReturn);
        binding.groupTouchReturnSuper.setChecked(groupTouchReturnSuper);
        // View
        binding.viewDispatchSuper.setChecked(viewDispatchSuper);
        binding.viewDispatchReturn.setChecked(viewDispatchReturn);
        binding.viewDispatchReturnSuper.setChecked(viewDispatchReturnSuper);
        binding.viewTouchSuper.setChecked(viewTouchSuper);
        binding.viewTouchReturn.setChecked(viewTouchReturn);
        binding.viewTouchReturnSuper.setChecked(viewTouchReturnSuper);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (activityDispatchSuper) {
            boolean b = super.dispatchTouchEvent(event);
            log("super.dispatchTouchEvent() " + b);
            if (activityDispatchReturnSuper) {
                activityDispatchReturn = b;
            }
        }
        log("dispatchTouchEvent " + activityDispatchReturn);
        return activityDispatchReturn;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (activityTouchSuper) {
            boolean b = super.onTouchEvent(event);
            log("super.onTouchEvent() " + b);
            if (activityTouchReturnSuper) {
                activityTouchReturn = b;
            }
        }
        log("onTouchEvent " + activityTouchReturn);
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
