package com.wuguangxin.simple.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wuguangxin.simple.AppConfig;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.adapter.GameAdapter;
import com.wuguangxin.simple.bean.GameDataBean;
import com.wuguangxin.simple.bean.GameRecordBean;
import com.wuguangxin.simple.databinding.ActivityGameBinding;
import com.wuguangxin.simple.view.SpacesItemDecoration;
import com.wuguangxin.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * <p>抖音上看到的一个地摊小游戏
 * <p>Created by wuguangxin on 2021-07-05
 */
public class GameActivity extends BaseActivity<ActivityGameBinding> {
    int red, green, blue;
    GameAdapter mGameAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_game;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitleLayout(R.id.titleLayout);
        setData();
        binding.gameOver.setVisibility(View.GONE);
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration(Utils.dip2px(this, 5)));
    }

    private void updateResult() {
        binding.text1.setText("红：" + red);
        binding.text2.setText("绿：" + green);
        binding.text3.setText("蓝：" + blue);
        int count = red+green+blue;
        if (count >= 12) {
            Integer[] arr = {red, green, blue};
            Arrays.sort(arr);
            StringBuilder sb = new StringBuilder();
            for(int num : arr) sb.append(num);
            String result = sb.reverse().toString();
            binding.result.setText(result);
            binding.gameOver.setVisibility(View.VISIBLE);

            Gson gson = new Gson();
            Type type = new TypeToken<LinkedList<GameRecordBean>>() {}.getType();

            // 获取旧数据
            LinkedList<GameRecordBean> gameRecordList = null;
            String gameRecordListJson = AppConfig.getInstance().getGameRecordList();
            if (!TextUtils.isEmpty(gameRecordListJson)) {
                gameRecordList = gson.fromJson(gameRecordListJson, type);
            }
            if (gameRecordList == null) {
                gameRecordList = new LinkedList<>();
            }

            // 保存新数据
            GameRecordBean gameRecordBean = new GameRecordBean(red, green, blue, result);
            gameRecordList.addFirst(gameRecordBean);
            String json = gson.toJson(gameRecordList, type);
            AppConfig.getInstance().setGameRecordList(json);
        }
    }

    /**
     * 重置游戏
     */
    private void replay() {
        red = 0;
        green = 0;
        blue = 0;
        binding.text1.setText("红：0");
        binding.text2.setText("绿：0");
        binding.text3.setText("蓝：0");
        binding.result.setText("");
        binding.gameOver.setVisibility(View.GONE);
        mGameAdapter.reset();
        mGameAdapter.setList(sort(sort(createData())));// 两次随机排序
//        setData();
    }

    @Override
    public void initListener() {
        binding.gameReplay.setOnClickListener(view -> replay());

        getTitleLayout().setMenuListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(GameRecordActivity.class);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {
        List<GameDataBean> list = sort(sort(createData())); // 两次随机排序
        mGameAdapter = new GameAdapter(list);
        mGameAdapter.setOnStatusListener( bean -> {
            if (bean != null) {
                switch (bean.getText()) {
                case "红": red++; break;
                case "绿": green++; break;
                case "蓝": blue++; break;
                }
            }
            updateResult();
        });

        binding.recyclerView.setAdapter(mGameAdapter);
    }

    private List<GameDataBean> createData() {
        List<GameDataBean> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(new GameDataBean("红", Color.RED));
            list.add(new GameDataBean("绿", Color.GREEN));
            list.add(new GameDataBean("蓝", Color.BLUE));
        }
        list.add(new GameDataBean("蓝", Color.BLUE));
        return list;
    }

    // 随机排序
    private List<GameDataBean> sort(List<GameDataBean> list) {
        Collections.sort(list, new Comparator<>() {
            private final int[] vs = {-1, 0, 1};
            private final Random rnd = new Random(System.currentTimeMillis());

            @Override
            public int compare(GameDataBean o, GameDataBean t1) {
                return vs[rnd.nextInt(vs.length)];
            }
        });
        return list;
    }
}
