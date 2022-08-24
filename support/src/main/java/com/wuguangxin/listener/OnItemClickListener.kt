package com.wuguangxin.listener;

/**
 * item点击监听器
 * Created by wuguangxin on 17/4/15.
 */
public interface OnItemClickListener<T> {
    /**
     * @param view 被点击的item View
     * @param t item对应的数据
     * @param position 点击的item位置
     * @param type 数据类型 (可以对应viewPager的currentItem)
     */
    void onItemClick(android.view.View view, T t, int position, int type);
}
