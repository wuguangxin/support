package com.wuguangxin.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

/**
 * 自定义的 CheckBox，解决了在已设置 OnCheckedChangeListener 的情况下，
 * 在 onCheckedChanged() 里做了耗时操作得到结果后再调用 setChecked()来改变值，导致的问题。
 *
 * 例如添加自选和取消自选必须把结果提交到服务器，服务器得到响应后再改变CheckBox的选中状态。
 *
 * Created by wuguangxin on 2020-01-02.
 */
public class XinCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {
	private OnCheckedChangeListener listener;

	public XinCheckBox(Context context) {
		super(context);
	}

	public XinCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public XinCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
		super.setOnCheckedChangeListener(listener);
		this.listener = listener;
	}

	@Override
	public void setChecked(boolean checked) {
		if (listener != null) {
			super.setOnCheckedChangeListener(null);
			super.setChecked(checked);
			super.setOnCheckedChangeListener(listener);
			return;
		}
		super.setChecked(checked);
	}
}