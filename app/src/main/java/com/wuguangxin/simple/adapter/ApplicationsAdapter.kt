package com.wuguangxin.simple.adapter

import android.content.Context
import com.wuguangxin.simple.utils.AppUtils.getAppIcon
import com.wuguangxin.simple.utils.AppUtils.getAppName
import com.wuguangxin.simple.utils.AppUtils.isSystem
import com.wuguangxin.adapter.BaseRecyclerAdapter
import android.content.pm.PackageInfo
import com.wuguangxin.adapter.BaseViewHolder
import android.graphics.Color
import com.wuguangxin.simple.R

class ApplicationsAdapter(context: Context) : BaseRecyclerAdapter<PackageInfo, BaseViewHolder>(context) {

    override fun getLayoutId(): Int {
        return R.layout.item_applications
    }

    override fun bindViewData(holder: BaseViewHolder, data: PackageInfo, position: Int, type: Int) {
        holder.setIsRecyclable(false) // false: view不重复利用，降低性能
        holder.setImageDrawable(R.id.iv_icon, getAppIcon(context, data)!!)
        holder.setText<Any>(R.id.tv_name, getAppName(context, data))
        holder.setTextColor(R.id.tv_name, if (isSystem(context, data)) Color.RED else Color.BLACK)
        holder.setText<Any>(R.id.tv_package, "包名：" + data.packageName)
        holder.setText<Any>(
            R.id.tv_version,
            "版本：" + data.versionName + "（" + data.versionCode + "）"
        )
    }
}