package com.wuguangxin.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 带有上分割线、下分割线的 TextView。
 * <p>Created by wuguangxin on 19/12/20 </p>
 */
public class ItemEditText extends AppCompatEditText {

    private final ItemBuilder<AppCompatEditText> itemBuilder;

    public ItemEditText(Context context) {
        this(context, null);
    }

    public ItemEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ItemEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        itemBuilder = new ItemBuilder<>(this, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        itemBuilder.draw(canvas);
    }

    public ItemBuilder<AppCompatEditText> getItemBuilder() {
        return itemBuilder;
    }

}




