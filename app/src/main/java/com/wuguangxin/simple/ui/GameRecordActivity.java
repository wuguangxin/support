package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wuguangxin.simple.AppConfig;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.adapter.GameRecordAdapter;
import com.wuguangxin.simple.bean.GameRecordBean;
import com.wuguangxin.simple.databinding.ActivityGameRecordBinding;
import com.wuguangxin.simple.view.SpacesItemDecoration;
import com.wuguangxin.utils.Utils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * <p>游戏记录
 * <p>Created by wuguangxin on 2021-07-06
 */
public class GameRecordActivity extends BaseActivity<ActivityGameRecordBinding> {
    GameRecordAdapter mGameRecordAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_game_record;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitleLayout(R.id.titleLayout);
        List<GameRecordBean> list = getGameRecordList();
        mGameRecordAdapter = new GameRecordAdapter(list);
        binding.gameRecordRecyclerView.setAdapter(mGameRecordAdapter);
        binding.gameRecordRecyclerView.addItemDecoration(new SpacesItemDecoration(Utils.dip2px(this, 1)));
    }

    private List<GameRecordBean> getGameRecordList() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<GameRecordBean>>() {}.getType();
        // 获取旧数据
        String gameRecordListJson = AppConfig.getInstance().getGameRecordList();
        if (!TextUtils.isEmpty(gameRecordListJson)) {
            return gson.fromJson(gameRecordListJson, type);
        }
        return null;
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void setData() {
    }

}
