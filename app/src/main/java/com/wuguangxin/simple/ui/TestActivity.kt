package com.wuguangxin.simple.ui

import com.wuguangxin.simple.R
import android.os.Bundle
import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.util.TimeUtils
import com.wuguangxin.simple.databinding.ActivityTestBinding
import com.wuguangxin.utils.Utils
import java.lang.StringBuilder
import java.util.concurrent.CompletableFuture

class TestActivity : BaseActivity() {
    lateinit var binding: ActivityTestBinding

    override fun getLayoutRes(): Int {
        return R.layout.activity_test
    }

    override fun initListener() {}
    override fun initData() {}
    override fun setData() {}

    override fun initView(savedInstanceState: Bundle?) {
        titleBar.setBackVisibility(false)
        binding = ActivityTestBinding.bind(layoutManager.bodyLayout)

        binding.testGroup.removeAllViews()
        addTest("test1 异步请求", ::test1)// 测试异步执行多个请求
        addTest("test2 事件分发", ::test2) // 测试事件分发机制
        addTest("test3 TimeUtils", ::test3) // 系统TimeUtils
        addTest("test4 布局加载", ::test4) // 测试布局加载方式
        addTest("test5 OutOfMemoryError", ::test5)
        addTest("test6 OutOfMemoryError", ::test6)

        val hashMap = hashMapOf<String, String>()
        hashMap.put("a","b")

    }

    // 测试 OutOfMemoryError 需要分配内存时，内存不足造成
    private fun test5(view: View?) {
        val list = ArrayList<String>()
        for (i in 1..10000000) {
            list.add(i.toString());
        }
    }

    // 测试 StackOverflowError
    // 1.递归太深、死循环都会抛出该异常
    // 2.例如两个对象在实例化时，都在实例化对方
    private fun test6(view: View?) {
        val a = A()
        a.printA();
    }

    class A {
        // val b = B() // 实例化对方
        var i = 0
        fun printA() {
            i++
            println(i)
            printA()
//            println("我是A")
        }
    }

    class B {
        val a = A()  // 实例化对方
        fun printB() = println("我是B")
    }

    // 测试布局加载方式
    private fun test4(view: View) {
        val viewEditor = ViewEditor.with(view)
        val layoutRes = R.layout.test6_layout
        val inflater = LayoutInflater.from(this)
        val rootView: ViewGroup = binding.test6Layout

//        inflater.inflate(layoutRes, rootView)
//        inflater.inflate(layoutRes, rootView, true)

//        val parser = resources.getLayout(layoutRes)
//        inflater.inflate(parser, rootView)
//        inflater.inflate(parser, rootView, false)
        viewEditor.release()
    }


    @SuppressLint("RestrictedApi")
    private fun test3(view: View) {
        val viewEditor = ViewEditor.with(view)
        val stringBuilder = StringBuilder("哈哈")
        println("stringBuilder = $stringBuilder")
        TimeUtils.formatDuration(23423425, stringBuilder)
        println("stringBuilder = $stringBuilder")
        viewEditor.release()
    }

    // 测试事件分发机制
    private fun test2(view: View) {
        openActivity(TestTouchEventActivity::class.java)
    }

    // 测试异步执行多个请求
    private fun test1(view: View) {
        val buttonInfo = ViewEditor.with(view)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return
        }
        val future1 = CompletableFuture.supplyAsync {
            Thread.sleep(2000)
            println("接口1执行结束")
            UserBean("张三")
        }
        val future2 = CompletableFuture.supplyAsync {
            Thread.sleep(1000)
            println("接口2执行结束")
            2
        }
        val future3 = CompletableFuture.supplyAsync {
            Thread.sleep(3000)
            println("接口3执行结束")
            "字符串数据"
        }
        CompletableFuture.allOf(future1, future2, future3).join()
        println("全部执行结束")
        buttonInfo.release()
    }

    data class UserBean(var name: String)

    // 添加测试按钮
    private fun addTest(name: String, listener: View.OnClickListener) {
        val button = View.inflate(this, R.layout.button, null) as Button
        button.text = name
        button.setOnClickListener(listener)
        val width = Utils.dip2px(this, 100f)
        val height = Utils.dip2px(this, 50f)
        println("$name addTest width = $width")
        println("$name addTest height = $height")
        button.layoutParams = ViewGroup.LayoutParams(width, height)
        binding.testGroup.addView(button)
    }


}
