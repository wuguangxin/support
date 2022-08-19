package com.wuguangxin.simple.adapter;

import android.content.Context;
import android.view.View;

import com.wuguangxin.adapter.BaseRecyclerAdapter;
import com.wuguangxin.adapter.BaseViewHolder;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.bean.GameRecordBean;
import com.wuguangxin.utils.DateUtils;

import java.util.List;

public class GameRecordAdapter extends BaseRecyclerAdapter<GameRecordBean, BaseViewHolder> {

    public GameRecordAdapter(Context context, List<GameRecordBean> list) {
        super(context, list);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_game_record;
    }

    @Override
    public BaseViewHolder createViewHolder(View view, int position) {
        return new BaseViewHolder(view);
    }

    @Override
    public void bindViewData(BaseViewHolder vewHolder, GameRecordBean bean, int position, int type) {
        vewHolder.setText(R.id.time, DateUtils.formatStringLong(bean.getCreateTime()));
        vewHolder.setText(R.id.textRed, "红:" +bean.getR() + "");
        vewHolder.setText(R.id.textGreen, "绿:" +bean.getG() + "");
        vewHolder.setText(R.id.textBlue, "蓝:" +bean.getB() + "");
        vewHolder.setText(R.id.result, bean.getResult());
    }

}
