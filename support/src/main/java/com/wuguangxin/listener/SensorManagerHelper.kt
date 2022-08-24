package com.wuguangxin.listener

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.wuguangxin.listener.SensorManagerHelper.OnShakeListener
import android.hardware.SensorEvent
import com.wuguangxin.listener.SensorManagerHelper

/**
 * 实现手机摇晃监听器
 *
 * Created by wuguangxin on 15/6/24
 */
class SensorManagerHelper(private val context: Context) : SensorEventListener {
    private var sensor: Sensor? = null // 传感器
    private var sensorManager: SensorManager? = null // 传感器管理器
    private var onShakeListener: OnShakeListener? = null // 重力感应监听器

    // 手机上一个位置时重力感应坐标
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    // 上次检测时间
    private var lastUpdateTime: Long = 0

    // 构造器
    init {
        // 获得监听对象
        start()
    }

    /**
     * 开始
     */
    fun start() {
        // 获得传感器管理器
        sensorManager = context.applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager != null) {
            // 获得重力传感器
            sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }
        // 注册
        if (sensor != null) {
            sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    /**
     * 停止检测
     */
    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    /**
     * 摇晃监听接口
     */
    interface OnShakeListener {
        /**
         * 重力感应速度值，不要在该回调中做耗时操作。
         * speed≈10±：当手机放着不动时；
         * speed≈500±：当拿起桌面上的手机时；
         * speed≈5000±：当摇晃手机时；
         *
         * @param speed speed
         */
        fun onSpeedChange(speed: Double)
        fun onShake()
    }

    /**
     * 设置重力感应监听器
     *
     * @param listener
     */
    fun setOnShakeListener(listener: OnShakeListener?) {
        onShakeListener = listener
    }

    /**
     * (non-Javadoc) android.hardware.SensorEventListener#onAccuracyChanged(android.hardware .Sensor, int)
     */
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    /**
     * 重力感应器感应获得变化数据 android.hardware.SensorEventListener#onSensorChanged(android.hardware .SensorEvent)
     */
    override fun onSensorChanged(event: SensorEvent) {
        // 现在检测时间
        val currentUpdateTime = System.currentTimeMillis()
        // 两次检测的时间间隔
        val timeInterval = currentUpdateTime - lastUpdateTime
        // 判断是否达到了检测时间间隔
        if (timeInterval < UPDATE_INTERVAL_TIME) {
            return
        }
        // 现在的时间变成last时间
        lastUpdateTime = currentUpdateTime
        // 获得x,y,z坐标
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        // 获得x,y,z的变化值
        val deltaX = x - lastX
        val deltaY = y - lastY
        val deltaZ = z - lastZ
        // 将现在的坐标变成last坐标
        lastX = x
        lastY = y
        lastZ = z
        val speed = Math.sqrt((deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toDouble()) / timeInterval * 10000
        onShakeListener?.let {
            it.onSpeedChange(speed)
            // 达到速度阀值，发出提示
            if (speed >= SPEED_THRESHOLD) {
                it.onShake()
            }
        }
    }

    companion object {
        // 速度阈值，当摇晃速度达到这值后产生作用
        private const val SPEED_THRESHOLD = 5000

        // 两次检测的时间间隔
        private const val UPDATE_INTERVAL_TIME = 50
    }
}