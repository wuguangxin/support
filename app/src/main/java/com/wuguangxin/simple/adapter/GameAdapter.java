package com.wuguangxin.simple.adapter;

import android.content.Context;
import android.view.View;

import com.wuguangxin.adapter.BaseRecyclerAdapter;
import com.wuguangxin.adapter.BaseViewHolder;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.bean.GameDataBean;
import com.wuguangxin.simple.view.SquareCheckBox;

import java.util.LinkedList;
import java.util.List;

public class GameAdapter extends BaseRecyclerAdapter<GameDataBean, BaseViewHolder> {

    private LinkedList<GameDataBean> resultData = new LinkedList<>();
    private int grayColor;
    boolean full = false;

    public GameAdapter(Context context, List<GameDataBean> list) {
        super(context, list);
        grayColor = context.getResources().getColor(R.color.gray);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_game;
    }

    @Override
    public BaseViewHolder createViewHolder(View view, int position) {
        return new BaseViewHolder(view);
    }

    public List<GameDataBean> getResultData() {
        return resultData;
    }

    @Override
    public void setList(List<GameDataBean> list) {
        full = false;
        resultData.clear();
        resultData = new LinkedList<>();
        super.setList(list);
    }

    @Override
    public void bindViewData(BaseViewHolder vewHolder, GameDataBean gameDataBean, int position, int type) {

        SquareCheckBox checkBox = (SquareCheckBox) vewHolder.getItemView();
        if (gameDataBean.checked) {
            checkBox.setBackgroundColor(gameDataBean.color);
            checkBox.setOnCheckedChangeListener(null);
        } else if (!gameDataBean.enable) {
            checkBox.setBackgroundColor(grayColor);
            checkBox.setOnCheckedChangeListener(null);
        } else {
            checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
                if (checked) {
                    if (full) {
                        return;
                    }
                    resultData.add(gameDataBean);
                    gameDataBean.checked = true;
                    gameDataBean.enable = false;
                    checkBox.setBackgroundColor(gameDataBean.color);
                    checkBox.setEnabled(gameDataBean.enable);
                    if (onStatusListener != null) {
                        onStatusListener.onChecked(gameDataBean);
                    }
                    if (resultData.size() >= 12) {
                        int itemCount = getItemCount();
                        for (int i = 0; i < itemCount; i++) {
                            getItem(i).enable = false;
                        }
                        full = true;
                        notifyDataSetChanged();
                    }
                }
            });
        }

        checkBox.setChecked(gameDataBean.checked);
        checkBox.setEnabled(gameDataBean.enable || resultData.size() >= 12);
        checkBox.setText(gameDataBean.text);
    }

    private OnStatusListener onStatusListener;

    public void setOnStatusListener(OnStatusListener listener) {
        this.onStatusListener = listener;
    }

    public interface OnStatusListener {
        void onChecked(GameDataBean gameDataBean);
    }


}
