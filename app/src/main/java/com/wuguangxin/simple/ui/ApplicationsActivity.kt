package com.wuguangxin.simple.ui

import com.wuguangxin.simple.R
import android.os.Bundle
import android.content.pm.PackageInfo
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.wuguangxin.simple.adapter.ApplicationsAdapter
import com.wuguangxin.simple.databinding.ActivityApplicationBinding
import com.wuguangxin.simple.utils.AppUtils

class ApplicationsActivity : BaseActivity<ActivityApplicationBinding>() {

    private val packageInfoList = mutableListOf<PackageInfo>()
    private val searchList = mutableListOf<PackageInfo>()

    private val appsAdapter by lazy { ApplicationsAdapter() }

    override fun getLayoutId(): Int {
        return R.layout.activity_application
    }

    override fun initListener() {}
    override fun initData() {}
    override fun setData() {}

    override fun initView(savedInstanceState: Bundle?) {
        setTitleLayout(R.id.titleLayout)
//        title = "应用列表"
//        titleBar.setBackVisibility(false)

        binding.etKeyword.doOnTextChanged { text, start, before, count ->
            search(text)
        }

        initViewPager()

        applicationList()
    }

    private fun initViewPager() {
        binding.rvApps.adapter = appsAdapter
        binding.rvApps.layoutManager = LinearLayoutManager(getContext())
        appsAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position) as PackageInfo
            com.blankj.utilcode.util.AppUtils.launchApp(item.packageName)
            // AppUtils.startApp(getContext(), item.packageName)
        }
    }

    private fun applicationList() {
        val apps = AppUtils.getInstalledPackages(this, 0)
        packageInfoList.clear()
        packageInfoList.addAll(apps)
        appsAdapter.setList(packageInfoList)
    }

    private fun search(text: CharSequence?) {
        log("keyword：$text")
        searchList.clear()
        if (!text.isNullOrEmpty()) {
            packageInfoList.forEach {
                val nameContains = AppUtils.getAppName(this, it)?.contains(text, true) == true
                val packageContains = it.packageName.contains(text, true)
                if (nameContains || packageContains) {
                    searchList.add(it)
                }
            }
            appsAdapter.setList(searchList)
        } else {
            appsAdapter.setList(packageInfoList)
        }
    }
}
