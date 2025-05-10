package com.wuguangxin.simple.adapter

import com.wuguangxin.simple.utils.AppUtils.getAppIcon
import com.wuguangxin.simple.utils.AppUtils.getAppName
import com.wuguangxin.simple.utils.AppUtils.isSystem
import android.content.pm.PackageInfo
import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wuguangxin.simple.R

class ApplicationsAdapter : BaseQuickAdapter<PackageInfo, BaseViewHolder>(R.layout.item_applications) {

    override fun convert(holder: BaseViewHolder, item: PackageInfo) {
        getAppIcon(holder.itemView.context, item)?.let {
            holder.setImageDrawable(R.id.iv_icon, it)
        }
        holder.setText(R.id.tv_name, getAppName(context, item))
        holder.setTextColor(R.id.tv_name, if (isSystem(context, item)) Color.RED else Color.BLACK)
        holder.setText(R.id.tv_package, "包名：" + item.packageName)
        holder.setText(R.id.tv_version, "版本：${item.versionName}（${item.versionCode}）")
    }
}
