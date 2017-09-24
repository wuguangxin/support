package com.wuguangxin.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.wuguangxin.view.ItemView;

public class ItemViewActivity extends AppCompatActivity implements View.OnClickListener{
    private ItemView mItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemview);
        setTitle("列表Item组件");

        mItemView = (ItemView) findViewById(R.id.itemView);
        mItemView.setOnClickListener(this);

        mItemView.getKeyView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ItemViewActivity.this, "点了 Key", Toast.LENGTH_SHORT).show();
            }
        });
        mItemView.getValueView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ItemViewActivity.this, "点了 Value", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.itemView:
            Toast.makeText(this, "点了 ItemView", Toast.LENGTH_SHORT).show();
            break;
        }
    }
}
