package com.wuguangxin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wuguangxin.listener.OnItemClickListener;
import com.wuguangxin.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerAdapter<T, V extends BaseViewHolder> extends RecyclerView.Adapter<V> {
    protected Context context;
    /** 数据列表 */
    private final List<T> mData = new ArrayList<>();
    /** 列表类型（可用于标识列表的类型） */
    private int listType;

    private OnItemClickListener<T> onItemClickListener;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
    }

    public BaseRecyclerAdapter(Context context, List<T> data) {
        this.context = context;
        this.mData.clear();
        if (data != null) {
            this.mData.addAll(data);
        }
    }

    @NonNull
    @Override
    final public V onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = View.inflate(viewGroup.getContext(), getLayoutId(), null);
        view.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getItem(position), position, listType);
            }
        });
        return createViewHolder(view, position);
    }

    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        bindViewData(holder, mData.get(position), position, getListType());
    }

    public abstract int getLayoutId();

    public V createViewHolder(View view, int position) {
        return (V) new BaseViewHolder(view);
    }

    public abstract void bindViewData(V holder, T data, int position, int type);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Context getContext() {
        return context;
    }

    public List<T> getList() {
        return mData;
    }

    public void setListType(int listType) {
        this.listType = listType;
    }

    public int getListType() {
        return listType;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setList(List<T> data) {
        this.mData.clear();
        if (data != null) {
            this.mData.addAll(data);
        }
        notifyAdapter();
    }

    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    public void showToast(String text) {
        if (context == null) return;
        ToastUtils.showToast(context, text);
    }
}
