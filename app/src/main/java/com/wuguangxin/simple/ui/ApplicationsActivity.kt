package com.wuguangxin.simple.ui

import android.Manifest
import com.wuguangxin.simple.R
import android.os.Bundle
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.wuguangxin.simple.adapter.ApplicationsAdapter
import com.wuguangxin.simple.databinding.ActivityApplicationBinding
import com.wuguangxin.simple.utils.AppUtils
import com.wuguangxin.utils.AndroidUtils
import com.wuguangxin.utils.PermissionUtils
import java.util.*

class ApplicationsActivity : BaseActivity<ActivityApplicationBinding>() {

    private val packageInfoList = mutableListOf<PackageInfo>()
    private val searchList = mutableListOf<PackageInfo>()

    private var appsAdapter: ApplicationsAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_application
    }

    override fun initListener() {}
    override fun initData() {}
    override fun setData() {}

    override fun initView(savedInstanceState: Bundle?) {
        title = "应用列表"
        titleBar.setBackVisibility(false)

        binding.etKeyword.doOnTextChanged { text, start, before, count ->
            search(text)
        }

        initViewPager()

        applicationList()
    }

    private fun initViewPager() {
        appsAdapter = ApplicationsAdapter(this)
        binding.rvApps.adapter = appsAdapter
        binding.rvApps.layoutManager = LinearLayoutManager(getContext())
        appsAdapter?.setOnItemClickListener { view, item, position, type ->
            com.blankj.utilcode.util.AppUtils.launchApp(item.packageName)
//            AppUtils.startApp(getContext(), item.packageName)
        }
    }

    private fun applicationList() {
        val apps = AppUtils.getInstalledPackages(this, 0)
        packageInfoList.clear()
        packageInfoList.let {
            packageInfoList.addAll(apps)
        }
        appsAdapter?.list = packageInfoList
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
            appsAdapter?.list = searchList
        } else {
            appsAdapter?.list = packageInfoList
        }
    }
}
