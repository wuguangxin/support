package com.wuguangxin.simple.ui;

import android.os.Bundle;

import com.wuguangxin.base.BaseActivity;
import com.wuguangxin.simple.R;
import com.wuguangxin.view.ItemView;

import butterknife.BindView;

public class ItemViewActivity extends BaseActivity {
    @BindView(R.id.itemView) ItemView mItemView;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_itemview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("列表Item组件");
    }

    @Override
    public void initListener() {
        mItemView.getKeyView().setOnClickListener(v -> showToast("点了 Key"));
        mItemView.getValueView().setOnClickListener(v -> showToast("点了 Value"));
        mItemView.setOnClickListener(v -> showToast("点了 ItemView"));
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }
}
