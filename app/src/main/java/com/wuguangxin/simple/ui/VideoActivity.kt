package com.wuguangxin.simple.ui

import android.os.Bundle
import com.wuguangxin.simple.R
import com.wuguangxin.simple.databinding.ActivityGameBinding
import com.wuguangxin.utils.FileUtils
import org.ar.rtc.IRtcEngineEventHandler
import org.ar.rtc.RtcEngine

/**
 * 视频监控
 *
 * Created by wuguangxin on 2022/9/23.
 */
class VideoActivity : BaseActivity<ActivityGameBinding>() {
    var mRtcEngine: RtcEngine? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_game
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleLayout(R.id.titleLayout)
        setData()

        initAnyRTCEngine();
        mRtcEngine?.enableVideo();
    }

    private fun initAnyRTCEngine() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, "appId", mRtcEventHandler)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 进入频道
     */
    private fun joinChannel() {
        val channelId = ""
        val userId = ""
        mRtcEngine?.joinChannel(getString(R.string.ar_token), channelId,"", userId)
    }

    /**
     * 回调
     */
    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        override fun onWarning(warn: Int) {
            super.onWarning(warn)
        }

        //加入频道成功
        override fun onJoinChannelSuccess(channel: String?, uid: String?, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
        }

        // 远端用户离开频道
        override fun onUserOffline(uid: String?, reason: Int) {
            super.onUserOffline(uid, reason)
        }

        //远端用户视频状态
        override fun onRemoteVideoStateChanged(uid: String?, state: Int, reason: Int, elapsed: Int) {
            super.onRemoteVideoStateChanged(uid, state, reason, elapsed)
        }
    }

    override fun onPause() {
        super.onPause()
        mRtcEngine?.leaveChannel() // 离开频道
    }

    override fun onDestroy() {
        super.onDestroy()
        RtcEngine.destroy() // 释放
    }

    override fun initListener() {

    }

    override fun initData() {

    }

    override fun setData() {

    }
}