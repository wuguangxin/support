package com.wuguangxin.simple.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wuguangxin.simple.AppConfig;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.adapter.GameAdapter;
import com.wuguangxin.simple.bean.GameDataBean;
import com.wuguangxin.simple.bean.GameRecordBean;
import com.wuguangxin.simple.view.SpacesItemDecoration;
import com.wuguangxin.ui.XinBaseActivity;
import com.wuguangxin.utils.Utils;
import com.wuguangxin.utils.mmkv.MmkvUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * <p>抖音上看到的一个地摊小游戏
 * <p>Created by wuguangxin on 2021-07-05
 */
public class GameActivity extends XinBaseActivity {
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.text1) TextView mText1;
    @BindView(R.id.text2) TextView mText2;
    @BindView(R.id.text3) TextView mText3;
    @BindView(R.id.result) TextView mResult;
    @BindView(R.id.game_over) Button mGameOver;
    @BindView(R.id.game_replay) View mGameReplay;
    int red, green, blue;
    GameAdapter mGameAdapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_game;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("抖音小游戏");
        mGameOver.setVisibility(View.GONE);
        setData();
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(Utils.dip2px(this, 5)));
    }

    private void updateResult() {
        mText1.setText("红：" + red);
        mText2.setText("绿：" + green);
        mText3.setText("蓝：" + blue);
        int count = red+green+blue;
        if (count >= 12) {
            Integer[] arr = {red, green, blue};
            Arrays.sort(arr);
            StringBuilder sb = new StringBuilder();
            for(int num : arr) sb.append(num);
            String result = sb.reverse().toString();
            mResult.setText(result);
            mGameOver.setVisibility(View.VISIBLE);

            Gson gson = new Gson();
            Type type = new TypeToken<LinkedList<GameRecordBean>>() {}.getType();

            // 获取旧数据
            LinkedList<GameRecordBean> gameRecordList = null;
            String gameRecordListJson = AppConfig.getInstance().getGameRecordList();
            if (!TextUtils.isEmpty(gameRecordListJson)) {
                gameRecordList = gson.fromJson(gameRecordListJson, type);
                if (gameRecordList == null) {
                    gameRecordList = new LinkedList<>();
                }
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
        mText1.setText("红：0");
        mText2.setText("绿：0");
        mText3.setText("蓝：0");
        mResult.setText("");
        mGameOver.setVisibility(View.GONE);
        List<GameDataBean> list = sort(sort(createData())); // 两次随机排序
        mGameAdapter.setList(list);
//        setData();
    }

    @Override
    public void initListener() {
        mGameReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replay();
            }
        });
        getTitleBar().setSettingListener("记录", new View.OnClickListener() {
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
        mGameAdapter = new GameAdapter(this, list);
        mGameAdapter.setOnStatusListener(new GameAdapter.OnStatusListener() {
            @Override
            public void onChecked(GameDataBean gameDataBean) {
                switch (gameDataBean.text) {
                    case "红": red++; break;
                    case "绿": green++; break;
                    case "蓝": blue++; break;
                }
                updateResult();
            }
        });

        mRecyclerView.setAdapter(mGameAdapter);
    }

    private List<GameDataBean> createData() {
        List<GameDataBean> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(new GameDataBean("红", Color.RED));
            list.add(new GameDataBean("绿", Color.GREEN));
            list.add(new GameDataBean("蓝", Color.BLUE));
        }
        return list;
    }

    // 随机排序
    private List<GameDataBean> sort(List<GameDataBean> list) {
        Collections.sort(list, new Comparator<GameDataBean>(){
            private final int[] vs = {-1,0,1};
            private final Random rnd = new Random(System.currentTimeMillis());
            @Override
            public int compare(GameDataBean o, GameDataBean t1) {
                return vs[rnd.nextInt(vs.length)];
            }
        });
        return list;
    }
}
