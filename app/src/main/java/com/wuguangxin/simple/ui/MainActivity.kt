package com.wuguangxin.simple.ui

import android.app.Activity
import com.wuguangxin.simple.R
import android.os.Bundle
import android.view.View
import com.wuguangxin.simple.databinding.ActivityMainBinding
import java.util.HashMap

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val activityMap by lazy {
        val map = mutableMapOf<Int, Class<out Activity?>>()
        map[R.id.widget] = WidgetActivity::class.java
        map[R.id.mvp] = MvpActivity::class.java
        map[R.id.game] = GameActivity::class.java
        map[R.id.test1] = TestActivity::class.java
        map[R.id.apps] = ApplicationsActivity::class.java
        map
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleLayout(R.id.titleLayout)
    }

    override fun initListener() {}

    fun onClick(v: View) {
        activityMap[v.id]?.let {
            openActivity(it)
        }
    }

    override fun initData() {}

    override fun setData() {}
}