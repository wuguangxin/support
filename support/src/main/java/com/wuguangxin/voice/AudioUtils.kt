package com.wuguangxin.voice

import android.media.MediaPlayer
import kotlin.Throws
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.io.IOException

/**
 * 音频播放工具类
 * <p>Created by wuguangxin on 15/6/9 </p>
 */
object AudioUtils {
    private var mediaPlayer = MediaPlayer() //播放器对象

    /**
     * 播放
     * @param file 文件
     */
    @Throws(IOException::class)
    fun play(file: File) {
        play(file.path)
    }

    /**
     * 播放
     * @param path 文件路径
     */
    @Throws(IOException::class)
    fun play(path: String?) {
        if (TextUtils.isEmpty(path)) {
            Log.e("AudioUtils", "音频文件不存在")
            return
        }
        mediaPlayer.apply {
            reset() // 把各项参数恢复到最初始的状态
            setDataSource(path) // 设置数据源
            prepare() // 准备
            start() // 开始
        }
    }

    /**
     * 初始化
     */
    private fun init() {
        mediaPlayer.setOnCompletionListener {
            it.release() // 当结束时释放资源
        }
        mediaPlayer.setOnPreparedListener {
            it.start() // 开始播放
        }
    }

    /**
     * 暂停播放
     */
    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    /**
     * 停止播放
     */
    fun stop() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }

    /**
     * 重新播放
     */
    fun rePlay() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(0) //从开始位置播放
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        mediaPlayer.release()
    }
}