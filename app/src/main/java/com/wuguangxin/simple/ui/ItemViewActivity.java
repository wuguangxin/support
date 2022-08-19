package com.wuguangxin.simple.ui;

import android.os.Bundle;

import com.wuguangxin.simple.R;
import com.wuguangxin.simple.databinding.ActivityItemviewBinding;

public class ItemViewActivity extends BaseActivity<ActivityItemviewBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_itemview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("列表Item组件");
    }

    @Override
    public void initListener() {
        binding.itemView.getKeyView().setOnClickListener(v -> showToast("点了 Key"));
        binding.itemView.getValueView().setOnClickListener(v -> showToast("点了 Value"));
        binding.itemView.setOnClickListener(v -> showToast("点了 ItemView"));
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }
}
