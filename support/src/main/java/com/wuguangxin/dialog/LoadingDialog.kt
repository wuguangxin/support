package com.wuguangxin.dialog

import android.app.Dialog
import android.content.Context
import com.wuguangxin.support.R
import android.graphics.drawable.AnimationDrawable
import android.view.animation.AlphaAnimation
import android.view.WindowManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView

/**
 * 耗时操作时显示的对话框
 *
 * Created by wuguangxin on 14/8/29
 */
class LoadingDialog(context: Context)
    : Dialog(context, R.style.xin_loading_dialog) {

    private val DEF_ALPHA: Float = 0.0f
    private var mView: View? = null
    private var mLoadingImage: ImageView? = null
    private var anim: AnimationDrawable? = null
    private var alphaAnimation: AlphaAnimation? = null
    private var lp: WindowManager.LayoutParams? = null

    init {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.xin_dialog_loading, null)?.let {
            mLoadingImage = it.findViewById(R.id.loading_image)
            mLoadingImage?.setImageResource(R.drawable.loading) //  R.drawable.xin_loading_anim 普通转圈
            anim = mLoadingImage?.drawable as AnimationDrawable
            alphaAnimation = AlphaAnimation(0f, 1f)
            alphaAnimation!!.duration = 500
            setCancelable(false)
            setContentView(it)
            mView = it
        }
    }

    private fun startAnim() {
        // 如果未转动，就转动
        if (anim?.isRunning == false) {
            anim?.start()
        }
    }

    private fun stopAnim() {
        // 如果正在播放 停止
        if (anim?.isRunning == true) {
            anim?.stop()
            mView?.clearAnimation()
        }
    }

    override fun show() {
        setAlpha(DEF_ALPHA)
        startAnim()
        super.show()
    }

    fun setVisible(visible: Boolean) {
        if (visible) {
            show()
        } else {
            dismiss()
        }
    }

    /**
     * 设置窗体透明度（默认0.8f）
     * @param alpha 透明度
     */
    fun setAlpha(alpha: Float) {
        window?.let { win ->
            win.attributes?.let { attr ->
                attr.alpha = alpha
                win.attributes = attr
                lp = attr
            }
        }
    }

    override fun hide() {
        stopAnim()
        super.hide()
    }

    override fun dismiss() {
        stopAnim()
        super.dismiss()
    }

    override fun cancel() {
        stopAnim()
        super.cancel()
    }
}