package com.wuguangxin.simple.ui;

import android.os.Bundle;

import com.wuguangxin.listener.TextChangeListener;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.databinding.ActivityItemviewBinding;

public class ItemViewActivity extends BaseActivity<ActivityItemviewBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_itemview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitleLayout(R.id.titleLayout);
    }

    @Override
    public void initListener() {
        binding.itemView.getKeyView().setOnClickListener(v -> showToast("点了 Key"));
        binding.itemView.getValueView().setOnClickListener(v -> showToast("点了 Value"));
        binding.itemView.setOnClickListener(v -> showToast("点了 ItemView"));

        binding.etPhone.addTextChangedListener(new TextChangeListener(binding.etPhone, 0, TextChangeListener.TextType.INSTANCE.getPHONE()));
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }
}
