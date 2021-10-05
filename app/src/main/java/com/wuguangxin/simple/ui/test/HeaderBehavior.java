package com.wuguangxin.simple.ui.test;

public abstract class HeaderBehavior {
    OverScroller scroller;
    private int age = 100;

    public HeaderBehavior() {
        this.scroller = new OverScroller() {
            @Override
            public void abortAnimation() {
                super.abortAnimation();
            }
        };
    }

}
