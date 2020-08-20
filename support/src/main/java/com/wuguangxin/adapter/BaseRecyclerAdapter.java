package com.wuguangxin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wuguangxin.listener.OnItemClickListener;
import com.wuguangxin.utils.ToastUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected Context context;
    /** 数据列表 */
    private List<T> list;
    /** 列表类型（可用于标识列表的类型） */
    private int listType;

    private OnItemClickListener<T> onItemClickListener;

    public BaseRecyclerAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    final public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), getLayoutId(), null);
        return createViewHolder(view, i);
    }

    @Override
    final public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int i) {
        bindViewData(viewHolder, list.get(i), i, getListType());
    }

    public abstract int getLayoutId();

    public abstract void bindViewData(BaseViewHolder vewHolder, T t, int position, int type);

    public abstract BaseViewHolder createViewHolder(View view, int position);

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public Context getContext() {
        return context;
    }

    public List<T> getList() {
        return list;
    }

    public void setListType(int listType) {
        this.listType = listType;
    }

    public int getListType() {
        return listType;
    }

    public T getItem(int position) {
        return list == null ? null : list.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setList(List<T> list) {
        this.list = list;
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
