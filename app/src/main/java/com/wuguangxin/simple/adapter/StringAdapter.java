package com.wuguangxin.simple.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wuguangxin.adapter.BaseRecyclerAdapter;
import com.wuguangxin.adapter.BaseViewHolder;
import com.wuguangxin.simple.R;

import java.util.List;

/**
 * Created by wuguangxin on 2021/6/30.
 */
public class StringAdapter extends BaseRecyclerAdapter<String, StringAdapter.MyViewHolder> {

    public StringAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_simple_layout;
    }

    @Override
    public MyViewHolder createViewHolder(View view, int position) {
        return new MyViewHolder(view);
    }

    @Override
    public void bindViewData(MyViewHolder vewHolder, String s, int position, int type) {
        vewHolder.textView.setText(s);
    }

    public static class MyViewHolder extends BaseViewHolder {
        public TextView textView;

        public MyViewHolder(View view) {
            super(view);
          textView = findView(R.id.textview);
        }
    }

}
