package com.wuguangxin.simple.ui

import com.wuguangxin.dialog.XinDialog
import com.wuguangxin.simple.R
import android.os.Bundle
import com.wuguangxin.simple.databinding.ActivityMydialogBinding
import org.jetbrains.anko.sdk27.coroutines.onClick


class MyDialogActivity : BaseActivity<ActivityMydialogBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_mydialog
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleLayout(R.id.titleLayout)
    }

    override fun initData() {}

    override fun initListener() {
        binding.dialog.onClick {
            XinDialog.with(this@MyDialogActivity)
                .setTitle("标题")
                .setMessage("这里是内容")
                .setPositiveButton("确认")
                .setNegativeButton("取消")
                .show()
        }
    }

    override fun setData() {}

}