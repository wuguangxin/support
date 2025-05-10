package com.wuguangxin.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.SizeUtils
import com.wuguangxin.support.R
import com.wuguangxin.support.databinding.XinCommonDialogBinding
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor

/**
 * 简易对话框
 *
 * Created by wuguangxin on 2021/12/21.
 */
open class CommonDialog(context: Context) : Dialog(context) {

    private var binding: XinCommonDialogBinding

    private var dismissOnConfirm = false // 当点击"确认"时关闭对话框
    private var finishOnConfirm = false // 当点击"确认"时关闭界面
    private var finishOnCancel = false // 当点击"取消"时关闭界面

    init {
        val view = View.inflate(context, R.layout.xin_common_dialog, null)
        window?.decorView?.let {
            it.backgroundColor = Color.TRANSPARENT
            // 某些机型对话框横向填满无边距
            if (it.paddingLeft == 0) {
                val margin = SizeUtils.dp2px(25f)
                it.setPadding(margin, margin, margin, margin)
            }
        }
        super.setContentView(view)
        super.setCancelable(false)

        binding = DataBindingUtil.bind(view)!!
        binding.tvCancel.visibility = View.GONE // 默认隐藏取消按钮

        binding.tvConfirm.onClick {
            confirmListener.invoke()
            if (finishOnConfirm) {
                if (context is Activity) {
                    dismiss()
                    context.finish()
                }
            }
            if (dismissOnConfirm) {
                dismiss()
            }
        }
        binding.tvCancel.onClick {
            cancelListener.invoke()
            dismiss()
        }
    }

    // 一些快捷生成对象的方法
    companion object {
        fun with(context: Context): CommonDialog {
            return this.with(context, null, null)
        }

        fun with(context: Context, titleId: Int, contentId: Int): CommonDialog {
            return this.with(context, context.getText(titleId), context.getText(contentId))
        }

        fun with(context: Context, titleId: Int, content: CharSequence?): CommonDialog {
            return this.with(context, context.getText(titleId), content)
        }

        fun with(context: Context, title: String?, contentId: Int): CommonDialog {
            return this.with(context, title, context.getText(contentId))
        }

        fun with(context: Context, title: CharSequence?, content: CharSequence?): CommonDialog {
            val dialog = CommonDialog(context)
            content?.let { dialog.setContent(content) }
            title?.let { dialog.setTitle(title) }
            return dialog
        }
    }

    fun setView(view: View?) {
        if (view != null) {
            val lp = view.layoutParams ?: ViewGroup.LayoutParams(-1, -2)
            binding.llContent.removeAllViews()
            binding.llContent.addView(view, lp)
        }
    }

    fun getContentView(): RelativeLayout {
        return binding.llContent
    }

    // 按钮监听
    private var confirmListener: () -> Unit = { }
    fun onConfirm(e: () -> Unit): CommonDialog {
        return onConfirm(null, e)
    }

    fun onConfirm(textId: Int, e: () -> Unit): CommonDialog {
        return onConfirm(context.getText(textId), e)
    }

    fun onConfirm(text: CharSequence?, e: () -> Unit): CommonDialog {
        if (text != null) {
            setConfirmText(text)
        }
        this.confirmListener = e
        return this
    }

    private var cancelListener: () -> Unit = { }
    fun onCancel(e: () -> Unit): CommonDialog {
        return onCancel(null, e)
    }

    fun onCancel(textId: Int, e: () -> Unit): CommonDialog {
        return onCancel(context.getText(textId), e)
    }

    fun onCancel(text: CharSequence?, e: () -> Unit): CommonDialog {
        if (text != null) {
            setCancelText(text)
        }
        this.cancelListener = e
        setCancelVisible(true)
        return this
    }

    // 标题
    override fun setTitle(titleId: Int) {
        //super.setTitle(titleId)
        this.setTitle(context.getText(titleId))
    }

    override fun setTitle(title: CharSequence?) {
        //super.setTitle(title)
        binding.tvTitle.text = title
        setTitleVisible(true)
    }

    fun setTitle(title: String?): CommonDialog {
        binding.tvTitle.text = title
        setTitleVisible(true)
        return this
    }

    fun setTitleColor(titleColor: Int): CommonDialog {
        binding.tvTitle.textColor = titleColor
        return this
    }

    fun setTitleSize(textSize: Float): CommonDialog {
        binding.tvTitle.textSize = textSize
        return this
    }

    fun setTitleStyle(gravity: Int): CommonDialog {
        binding.tvTitle.gravity = gravity
        return this
    }

    fun setTitleVisible(visible: Boolean): CommonDialog {
        binding.tvTitle.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    // 内容
    fun setContent(contentId: Int): CommonDialog {
        binding.tvContent.text = context.getText(contentId)
        return this
    }

    fun setContent(content: CharSequence?): CommonDialog {
        binding.tvContent.text = content
        return this
    }

    fun setContentColor(titleColor: Int): CommonDialog {
        binding.tvContent.setTextColor(titleColor)
        return this
    }

    fun setContentSize(textSize: Float): CommonDialog {
        binding.tvContent.textSize = textSize
        return this
    }

    fun setContentStyle(gravity: Int): CommonDialog {
        binding.tvContent.gravity = gravity
        return this
    }

    // 确定按钮
    fun setConfirmText(text: CharSequence?): CommonDialog {
        binding.tvConfirm.text = text
        return this
    }

    fun setConfirmText(textId: Int): CommonDialog {
        binding.tvConfirm.text = context.getText(textId)
        return this
    }

    fun setConfirmColor(textColor: Int): CommonDialog {
        binding.tvConfirm.textColor = textColor
        return this
    }

    fun setConfirmSize(textSize: Float): CommonDialog {
        binding.tvConfirm.textSize = textSize
        return this
    }

    fun setConfirmStyle(gravity: Int): CommonDialog {
        binding.tvConfirm.gravity = gravity
        return this
    }

    fun setConfirmBackground(background: Drawable): CommonDialog {
        binding.tvConfirm.background = background
        return this
    }

    // 取消按钮
    fun setCancelText(text: CharSequence?): CommonDialog {
        binding.tvCancel.text = text
        return this
    }

    fun setCancelText(textId: Int): CommonDialog {
        binding.tvCancel.text = context.getText(textId)
        return this
    }

    fun setCancelColor(textColor: Int): CommonDialog {
        binding.tvCancel.textColor = textColor
        return this
    }

    fun setCancelSize(textSize: Float): CommonDialog {
        binding.tvCancel.textSize = textSize
        return this
    }

    fun setCancelStyle(gravity: Int): CommonDialog {
        binding.tvCancel.gravity = gravity
        return this
    }

    fun setCancelBackground(background: Drawable): CommonDialog {
        binding.tvCancel.background = background
        return this
    }

    fun setCancelVisible(visible: Boolean): CommonDialog {
        binding.tvCancel.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun dismissOnConfirm(): CommonDialog {
        dismissOnConfirm = true
        return this
    }

    // 原则上是调用 setCancelVisible(true)
    fun dismissOnCancel(): CommonDialog {
        setCancelVisible(true)
        return this
    }

    // 当点击"确认"或"取消"按钮时，都关闭对话框。该方法会显示"取消"按钮
    fun dismissOnClick(): CommonDialog {
        return dismissOnConfirm().dismissOnCancel()
    }

    fun finishOnConfirm(): CommonDialog {
        finishOnConfirm = true
        return this
    }

    fun finishOnCancel(): CommonDialog {
        setCancelVisible(true)
        finishOnCancel = true
        return this
    }

    fun show(confirmListener: () -> Unit): CommonDialog {
        super.show()
        return onConfirm(null, confirmListener)
    }
}