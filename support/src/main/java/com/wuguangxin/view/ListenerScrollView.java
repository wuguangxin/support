package com.wuguangxin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class ListenerScrollView extends ScrollView {
    private final ArrayList<OnScrollChangeListener> listenerList = new ArrayList<>();

    public ListenerScrollView(Context context) {
        this(context, null);
    }

    public ListenerScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListenerScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalFadingEdgeEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (!listenerList.isEmpty()) {
            for (OnScrollChangeListener listener : listenerList) {
                listener.onScrollChange(l, t, oldl, oldt);
            }
        }
    }

    public void addOnScrollListener(OnScrollChangeListener l) {
        if (!listenerList.contains(l)) {
            listenerList.add(l);
        }
    }

    public interface OnScrollChangeListener {
        void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}


