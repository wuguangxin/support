package com.wuguangxin.simple.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import com.wuguangxin.simple.R
import com.wuguangxin.simple.databinding.ActivityRetrofitBinding
import com.wuguangxin.simple.utils.PhoneUtils

/**
 * Created by wuguangxin on 2021/9/7.
 */
class RetrofitActivity : BaseActivity() {
    lateinit var bind: ActivityRetrofitBinding;

    override fun getLayoutRes(): Int {
        return R.layout.activity_retrofit
    }

    override fun initView(savedInstanceState: Bundle?) {
        bind = ActivityRetrofitBinding.bind(layoutManager.bodyLayout);

        println(cal3(listOf(1, 2, 3, 4)))
        val countries = mutableListOf<Country>();
        countries.add(Country("中国", "CN", 100_000_000 * 14))
        countries.add(Country("美国", "EU", 100_000_000 * 10))
        countries.add(Country("英国", "EU", 100_000_000 * 20))

        // 过滤EU的国家
        val euCountries = filterCounties(countries)
        for (c in euCountries) println(c.toString())

        // 取出集合元素的name的列表
        println(countries.map(Country::name))

        // 把构造函数当成变量引用
        val createC = ::Country
        val createC1 = createC("aa", "bb", 12);


        val filterCountry1 = filterCountry(countries, this::isBigEuropeanCountry)
        println("filterCountry1:---$filterCountry1")

        // 将isBigEuropeanCountry作为匿名函数
        val filterCountry2 = filterCountry(countries, fun(country: Country): Boolean {
            return country.continent == "EU" && country.population > 10000
        })
        println("filterCountry2:---$filterCountry2")

        // lambda表达式
        val filterCountry3 = filterCountry(countries) { country ->
            country.continent == "EU" && country.population > 10000
        }
        println("filterCountry2:---$filterCountry3")

        //
        fun foo(int : Int) = { println(int) }
        listOf(1, 2, 3).forEach { foo(it)() }

        //
        fun sum(x: Int, y: Int, z: Int) = x + y + z
        println("sum=" + sum(1, 2, 3))

        //柯里化
        fun sum(x: Int) = { y: Int ->
            { z: Int -> z + y + z }
        }
        println("sum柯里化" + sum(1) (2) (3))

        // 给任意一个View扩展函数
        bind.btnQuery.changeText("牛逼啊")

        //
        val ok = true
        var result = if (ok) "成功" else "失败";
        println(result)

        // 类似三元运算符
        var name: String? = null //"2"
        name = name ?: "1"
        println(name)

        // when
        for (i in 0..10) testWhen(i)

        // vararg 可变参数
        testVararg("a", "b", "c", name = "吴光新")
        // 可使用*代替可变参数
        val arr = arrayOf("a", "b", "c");
        testVararg(*arr, name = "吴光新")

        // infix 中缀表达式
        textInfix()

        // 原生字符串
        testString()

        // 构造函数
        testConstruction()
    }

    private fun testConstruction() {
        val user1 = User("李四", 20, "上海")
        val user11 = User("李四", 20)
        // val user12 = User("李四", "广州") // 错误，不按顺序，或者不显示指明字段名称
        val user2 = User(name = "吴光新", age = 1, city = "北京")
        val user3 = User(name = "张三", age = 1)
    }

    class User(val name: String = "", val age: Int = 0, val city: String = "")

    class User2(name: String = "", age: Int = 0, city: String = "") {
        val name: String = name
        val age: Int = age
        val city: String = city
    }

    class User3(name: String , age: Int  , city: String) {
        val name: String
        val age: Int
        val city: String
        init {
            this.name = "李四"
            this.age = 19
            this.city = "广州"
        }
    }

    private fun testString() {
        val html = """
            <html> 
                <body>
                    <b>hello kotlin</b>
                </body>
            </html>
        """
        println(html)
    }

    // infix 中缀表达式
    private fun textInfix() {
        val p = Person()
        p called "吴光新1"
        p.called("吴光新2")
        // 以上两种写法都可以
    }

    class Person {
        infix fun called(name: String) {
            println("我的名字叫 $name")
        }
    }

    private fun testVararg(vararg args: String, name: String) {
        println("$name args are ")
        for(arg in args) println(arg)
    }

    private fun testWhen(day: Int) {
        when (day) {
//        when {     // when中的参数可省略，但左侧必须返回布尔值，否则报错
//            true -> println("哈哈")
            0 -> println("周一")
            1 -> println("周二")
            2 -> println("周三")
            3 -> println("周四")
            4 -> println("周五")
            5 -> println("周六")
            6 -> println("周日")
            else -> println("错误了$day")
        }
    }

    private fun TextView.changeText(text: String) {
        this.text = text
    }

    private fun filterCountry(countries : List<Country>, test: ((Country) -> Boolean)) : List<Country> {
        val res = mutableListOf<Country>();
        for (c in countries) {
            if (test(c)) {
                res.add(c)
            }
        }
        return res
    }

    fun filterCounties(countries: List<Country>): List<Country> {
        val res = mutableListOf<Country>();
        for (c in countries) {
            if (c.continent == "EU") {
                res.add(c)
            }
        }
        return res
    }

    private fun isBigEuropeanCountry(country: Country): Boolean {
        return country.continent == "EU" && country.population > 10000
    }



    data class Country(var name: String, var continent: String, var population: Int)


    private fun cal3(list: List<Int>): Int {
        return list.fold(0) { res, el -> res + el }
    }

    override fun initListener() {
        bind.btnRandom.setOnClickListener { bind.etPhone.setText(PhoneUtils.genPhoneNum()) }
        bind.btnQuery.setOnClickListener {
            getPhoneInfo()
        }
    }

    override fun initData() {
    }

    override fun setData() {
    }

    private fun phoneInfo() {
//            val params = Params()
//            params["format"] = "geojson"
//            params["eventtype"] = "earthquake"
//            params["orderby"] = "time"
//            params["minmag"] = "6"
//            params["limit"] = "20"
//            getService().query(params).enqueue(object: BaseCallback<JSONObject?>() {
//                override fun onSuccess(response: JSONObject?) {
//                    printLogI(response.toString())
//                }
//                override fun onError(errorCode: Int, errorMsg: String) {
//                    super.onError(errorCode, errorMsg)
//                    showToast(String.format("%s %s", errorCode, errorMsg))
//                }
//            })
    }

    // 需要查询的手机号码或手机号码前7位
    // 返回数据的格式,xml或json，默认json
    private fun getPhoneInfo() {
        val phone = bind.etPhone!!.text.toString()
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能为空")
            return
        }
        val params: MutableMap<String, String> = HashMap()
        params["phone"] = phone // 需要查询的手机号码或手机号码前7位
        params["dtype"] = "json" // 返回数据的格式,xml或json，默认json
        params["key"] = "42557097337ce434436c9940b5ebbbf0"

        val call = getService().getPhoneInfo(params);
//        call.enqueue(object : BaseCallback<Result<PhoneInfoBean>> {
//            override fun onSuccess(response: Result<PhoneInfoBean>) {
//                printLogI(response.toString())
//            }
//        })
    }
}
