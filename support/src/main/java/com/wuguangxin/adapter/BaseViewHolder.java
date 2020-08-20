package com.wuguangxin.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

/**
 * 基础ViewHolder，为了统一加入 ButterKnife.bind(this, view);
 * Created by wuguangxin on 2018/10/16.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> sparseArray = new SparseArray<>();
    protected View itemView;

    public BaseViewHolder(View view) {
        super(view);
        this.itemView = view;
        ButterKnife.bind(this, view);
    }

    public View getItemView() {
        return itemView;
    }

    public <T> T findView(int id) {
        View view = sparseArray.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            sparseArray.put(id, view);
        }
        return (T) view;
    }

    public void setText(int id, String content) {
        ((TextView) findView(id)).setText(content);
    }
}
