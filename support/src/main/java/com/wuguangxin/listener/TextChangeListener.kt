package com.wuguangxin.listener

import kotlin.jvm.JvmOverloads
import android.widget.EditText
import android.text.TextWatcher
import android.view.View.OnFocusChangeListener
import android.view.animation.Animation
import android.text.InputFilter.LengthFilter
import android.text.Editable
import android.text.TextUtils
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import android.view.animation.AnimationUtils
import com.wuguangxin.support.R
import java.lang.Exception
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * EditText内容变化监听类，当文本框获得输入焦点并输入了内容时，显示你定义的清除内容的按钮，
 * 当失去输入焦点，则隐藏按钮，当点击清除按钮时，清空文本框的内容
 *
 * Created by wuguangxin on 15/4/14
 */
class TextChangeListener @JvmOverloads constructor(
    editText: EditText?, // 要监听的EditText
    clearBtn: View?, // 清除按钮View
    textType: Int,
    precision: Int,
    callBack: EditTextCallBack? = null
) : TextWatcher, View.OnClickListener, OnFocusChangeListener {

    var precision = DEF_PRECISION // 小数点位数
        private set
    private var mEditText: EditText? = null
    private var mClearBtn: View? = null
    private var callBack: EditTextCallBack? = null

    /**
     * 输入的数据类型
     */
    private var textType = 0
    private var stringBuffer: StringBuffer? = null
    private var bankNumberSpace = 4
    private var context: Context? = null
    private var fade_in: Animation? = null
    private var fade_out: Animation? = null
    private var maxNumber = BigDecimal.valueOf(Double.MAX_VALUE) // 允许输入的最大数

    /**
     * @param editText 要监听的EditText
     */
    @JvmOverloads
    constructor(editText: EditText? = null, clearViewId: Int = 0)
            : this(editText, findViewById(getActivityFromView(editText), clearViewId))

    /**
     * @param editText 要监听的EditText
     * @param clearViewId 清除按钮ViewId
     */
    constructor(editText: EditText?, clearViewId: View?)
            : this(editText, clearViewId, TextType.TEXT)

    /**
     * @param editText 要监听的EditText
     * @param clearViewId 清除按钮ViewId
     * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
     */
    constructor(editText: EditText?, clearViewId: Int, textType: Int) :
            this(editText, findViewById(getActivityFromView(editText), clearViewId), textType, null)

    /**
     * @param editText 要监听的EditText
     * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
     * @param callBack 回调
     */
    constructor(editText: EditText?, textType: Int, callBack: EditTextCallBack?)
            : this(editText, null, textType, DEF_PRECISION, callBack)

    /**
     * @param editText 要监听的EditText
     * @param clearBtn 清除按钮
     * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
     * @param callBack 回调
     */
    @JvmOverloads
    constructor(editText: EditText?, clearBtn: View?, textType: Int, callBack: EditTextCallBack? = null)
            : this(editText, clearBtn, textType, DEF_PRECISION, callBack)


    /**
     * @param editText 要监听的EditText
     * @param clearBtn 清除按钮
     * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
     * @param precision 小数位数最大长多
     * @param callBack 回调
     */
    init {
        context = getActivityFromView(editText)
        mEditText = editText
        mClearBtn = clearBtn
        this.textType = textType
        this.precision = precision
        this.callBack = callBack
        if (clearBtn != null && editText?.isEnabled == true) {
            mClearBtn?.setOnClickListener(this)
        }
        mEditText?.onFocusChangeListener = this
        if (textType < 0) {
            this.textType = TextType.TEXT
        }

        mClearBtn?.let {
            fade_in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
            fade_out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        }

        //限定最大输入字符数
        if (textType == TextType.PHONE) {
            setMaxLength(13)
        } else if (textType == TextType.BANK_CARD) {
            setMaxLength(22)
        }
    }

    fun getMaxNumber(): BigDecimal? {
        return maxNumber
    }

    /**
     * 设置允许输入的最大数值（生效于类型：[TextType.NUMBER]）
     * @param maxNumber
     */
    fun setMaxNumber(maxNumber: BigDecimal?) {
        this.maxNumber = maxNumber
        if (textType == TextType.NUMBER && mEditText != null && maxNumber != null) {
            formatNumber(mEditText?.text)
        }
    }

    /**
     * 设置EditText最大长度
     * @param maxLength
     */
    fun setMaxLength(maxLength: Int) {
        mEditText?.let { edit ->
            //mEditText?.filters = arrayOf<InputFilter>(LengthFilter(maxLength))

            val filters = mEditText?.filters
            // 删除存在的
            val oldArr = filters?.toMutableList()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                oldArr?.removeIf {
                    it is LengthFilter
                }
            }
            // 新增
            oldArr?.add(LengthFilter(maxLength))
            edit.filters = oldArr?.toTypedArray()
        }
    }

    override fun onClick(v: View) {
        mEditText?.let {
            it.setText("")
            it.requestFocus()
        }
        setClearViewVisibility(View.GONE)
        bankNumberSpace = 0
    }

    override fun afterTextChanged(s: Editable) {
        stringBuffer?.let {
            it.delete(0, it.length - 1)
        }
        stringBuffer = null
        //如果是数字类型
        if (textType == TextType.NUMBER) {
            formatNumber(s)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        setClearViewVisibility(if (TextUtils.isEmpty(s)) View.GONE else View.VISIBLE)
        if (textType == TextType.PHONE) {
            // 格式化手机号码
            formatPhone(s, start, count)
        } else if (textType == TextType.BANK_CARD) {
            // 银行卡
            formatBankCard(s, start, count)
        }
        callBack?.onTextSet(s.toString())
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        setClearViewVisibility(if (hasFocus) View.VISIBLE else View.GONE)
        if (!hasFocus) {
            mEditText?.let { et ->
                if (textType == TextType.NUMBER && mEditText != null) {
                    val money = et.text.toString().trim { it <= ' ' }
                    if (!TextUtils.isEmpty(money)) {
                        if (money.endsWith(".")) {
                            et.setText(formatMoney(money))
                        } else {
                            et.setText(money)
                        }
                        et.setSelection(et.text.length)
                    }
                }
            }
        }
    }

    /**
     * 控制清除内容的按钮显示状态，失去输入焦点，隐藏按钮
     * @param visibility
     */
    private fun setClearViewVisibility(visibility: Int) {
        var visible = visibility
        mClearBtn?.let { clearView ->
            // 如果输入框不可用，则不显示清除按钮
            val editEnable = mEditText?.isEnabled == true
            if (!editEnable) {
                visible = View.GONE
            }

            if (visible == View.GONE) {
                if (clearView.visibility != visible) {
                    clearView.visibility = visible
                    clearView.startAnimation(fade_out)
                } else {

                }
            } else {
                // 不可用，则不显示清空按钮
                if (!editEnable) {
                    return
                }
                mEditText?.let { editView ->
                    // 如果已获得焦点、不是显示的并且内容不为空，则显示清除按钮
                    if (editView.isFocused && clearView.visibility != visible && !TextUtils.isEmpty(editView.text.toString().trim { it <= ' ' })) {
                        clearView.visibility = visible
                        clearView.startAnimation(fade_in)
                    }
                }
            }
        }
    }

    private fun formatPhone(s: CharSequence, start: Int, count: Int) {
        if (count == 1) { //输入
            stringBuffer = StringBuffer(s)
            if (start == 3 || start == 8) {
                setValue(formatPhoneNumber(s.toString()))
            }
        } else { //删除
            if (s.toString().endsWith(" ")) {
                setValue(formatPhoneNumber(s.toString().trim { it <= ' ' }))
            }
        }
    }

    /**
     * 格式化为数字
     * @param s
     */
    private fun formatNumber(s: Editable?) {
        if (s == null) {
            return
        }
        var number = s.toString()
        if (!TextUtils.isEmpty(number)) {
            if (maxNumber != null) {
                val bigDecimal = toBigDecimal(number)
                if (bigDecimal.compareTo(maxNumber) > 0) {
                    number = bigDecimal.toString()
                    val textWatcher: TextWatcher = this
                    mEditText?.removeTextChangedListener(this)
                    s.clear()
                    s.append(String.format("%." + precision + "f", maxNumber))
                    mEditText?.addTextChangedListener(textWatcher)
                }
            }
            val dotPosition = number.indexOf(".") // 点的位置
            if (dotPosition == 0) {
                s.delete(0, 1)
            }
            if (dotPosition > 0) { // 0.123456789
                val length = number.subSequence(dotPosition + 1, number.length).toString().length
                if (precision == 0) {
                    s.delete(dotPosition + precision, number.length)
                } else if (length > precision) {
                    s.delete(dotPosition + 1 + precision, number.length)
                }
            }
            if (number.length >= 2) {
                if (number.length == 2 && number.subSequence(0, 1) == "0" && "0." != number) {
                    s.delete(0, 1)
                } else if (number.subSequence(0, 2) == "00") { // =00
                    s.delete(1, 2)
                } else if (number.subSequence(0, 1).toString().contains("0") && !number.subSequence(1, 2).toString().contains(".")
                ) { // =0.
                    s.delete(1, 2)
                }
            }
        }
    }

    private fun toBigDecimal(text: String): BigDecimal {
        var value = text
        if (TextUtils.isEmpty(value)) {
            return BigDecimal.ZERO
        }
        value = value.trim { it <= ' ' }
        value = value.replace(",", "")
        return try {
            BigDecimal(value)
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }

    /**
     * 格式化为手机号
     * @param string
     * @return
     */
    private fun formatPhoneNumber(string: String): String {
        val sb = StringBuffer(string.replace(" ".toRegex(), ""))
        for (i in string.indices) {
            if (i == 3 || i == 8) {
                sb.insert(i, " ")
            }
        }
        return sb.toString()
    }

    /**
     * 格式化为银行卡
     * @return
     */
    private fun formatBankCard(s: CharSequence, start: Int, count: Int) {
        if (count == 1) {
            stringBuffer = StringBuffer(s)
            if (start > 0 && start % bankNumberSpace == 0) {
                for (i in 0 until stringBuffer!!.length) {
                    if (i > 0 && i % 4 == 0) {
                        stringBuffer?.insert(i, " ")
                        setValue(stringBuffer.toString())
                    }
                }
            }
        } else {
            if (s.toString().endsWith(" ")) {
                setValue(s.toString().trim { it <= ' ' })
            }
        }
    }

    fun setPrecision(precision: Int): TextChangeListener {
        this.precision = precision
        return this
    }

    private fun setValue(s: String) {
        mEditText?.let {
            it.setText(s)
            it.setSelection(it.text.toString().length)
        }
    }

    interface EditTextCallBack {
        fun onTextSet(str: String?)
    }

    /**
     * EditText输入的文本内容类型
     *
     *
     * Created by wuguangxin on 14/9/25
     */
    object TextType {
        /**
         * 普通文本类型（默认）
         */
        var TEXT = 0

        /**
         * 数字(如 100.01)（默认保留2位小数）
         */
        var NUMBER = 1

        /**
         * 手机号码类型。
         * 将格式化为如 186 1111 2222样式，并在输入过程中自动格式化；
         * 当调用getText()时，务必调用.replaceAll(" ", "")，去掉所有空格。
         */
        var PHONE = 2

        /**
         * 银行卡号码 （4位空一格，如xxxx xxxx xxxx xxxx xxx）
         */
        var BANK_CARD = 3

        /**
         * 身份证号码 （如xxxxxx xxxx xxxx xxxx )
         */
        var ID_CARD = 4
    }

    companion object {
        private const val DEF_PRECISION = 2
        fun withText(editText: EditText?): TextChangeListener {
            return TextChangeListener(editText, null, TextType.TEXT, null)
        }

        fun withNumber(editText: EditText?): TextChangeListener {
            return TextChangeListener(editText, null, TextType.NUMBER, null)
        }

        fun withText(editText: EditText?, callBack: EditTextCallBack?): TextChangeListener {
            return TextChangeListener(editText, null, TextType.TEXT, callBack)
        }

        fun withNumber(editText: EditText?, callBack: EditTextCallBack?): TextChangeListener {
            return TextChangeListener(editText, null, TextType.NUMBER, callBack)
        }

        fun getActivityFromView(view: View?): Activity? {
            var context = view!!.context
            while (context is ContextWrapper) {
                if (context is Activity) {
                    return context
                }
                context = context.baseContext
            }
            return null
        }

        private fun findViewById(activity: Activity?, clearViewId: Int): View? {
            if (clearViewId == 0 || clearViewId == -1 || activity == null) {
                return null
            }
            try {
                return activity.findViewById(clearViewId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 格式化金额
         */
        fun formatMoney(value: String): String {
            if (TextUtils.isEmpty(value)) return ""
            val bigDecimal = BigDecimal(value.replace(",".toRegex(), ""))
            return DecimalFormat("#######.######").format(bigDecimal.toString())
        }
    }
}