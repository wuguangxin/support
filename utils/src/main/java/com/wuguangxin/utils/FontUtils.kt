package com.wuguangxin.utils

import android.widget.TextView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.text.style.ForegroundColorSpan
import android.text.style.BackgroundColorSpan
import android.text.SpannableStringBuilder
import android.app.Activity
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import java.util.regex.Pattern

/**
 * 对TextView中的文字样式设置的工具类。
 *
 * @author Created by wuguangxin on 15/5/12
 */
object FontUtils {
    /**
     * 设置指定String在TextView中的颜色，比如搜索结果高亮搜索关键字
     *
     * @param textView TextView
     * @param keyword  要高亮的文字
     * @param color    高亮颜色
     */
    fun setFontColor(textView: TextView?, keyword: String, color: Int) {
        setFontColor(textView, arrayOf(keyword), color)
    }

    /**
     * 设置指定String在TextView中的颜色，比如搜索结果高亮搜索关键字
     *
     * @param textView TextView
     * @param keywords 关键字数组
     * @param color    颜色
     */
    fun setFontColor(textView: TextView?, keywords: Array<String>, color: Int) {
        textView?.let{ textView.text = formatColor(textView.text, keywords, color) }
    }

    /**
     * 给指定的文字设置字体大小。
     *
     * @param view     TextView
     * @param text     文本
     * @param textSize 文本字体大小（单位dip）（-1不设置）
     */
    fun setFontStyle(view: TextView?, text: String, textSize: Int) {
        view?.let{ view.text = formatStyle(view.text, text, textSize, -1, -1, -1) }
    }

    /**
     * 设置文字样式。
     *
     * @param view      TextView
     * @param text      要改变的文本
     * @param textSize  文本字体大小（单位dip）（-1不设置）
     * @param textStyle 字体样式（如粗体 Typeface.BOLD）（-1不设置）
     */
    fun setFontStyle(view: TextView?, text: String, textSize: Int, textStyle: Int) {
        view?.let{ view.text = formatStyle(view.text, text, textSize, textStyle, -1, -1) }
    }

    /**
     * 设置文字样式。
     *
     * @param view      TextView
     * @param text      要改变的文本
     * @param textSize  文本字体大小（单位dip）（-1不设置）
     * @param textStyle 字体样式（如粗体 Typeface.BOLD）（-1不设置）
     * @param textColor 字体前景颜色（-1不设置）
     */
    fun setFontStyle(view: TextView?, text: String, textSize: Int, textStyle: Int, textColor: Int) {
        view?.let{ view.text = formatStyle(view.text, text, textSize, textStyle, textColor, -1) }
    }

    /**
     * 设置文字样式。可指定字体的前景色和背景色、粗体等。
     *
     * @param view        TextView
     * @param text        要改变的文本
     * @param textSize    文本字体大小（单位dip）（-1不设置）
     * @param textStyle   字体样式（如粗体 Typeface.BOLD）（-1不设置）
     * @param textColor   字体前景颜色（-1不设置）
     * @param textBgColor 字体背景颜色（-1不设置）
     */
    fun setFontStyle(view: TextView?, text: String, textSize: Int, textStyle: Int, textColor: Int, textBgColor: Int) {
        view?.let{ view.text = formatStyle(view.text, text, textSize, textStyle, textColor, textBgColor) }
    }

    /**
     * 构建一个Spannable。
     *
     * @param string   TextView中的文本（CharSequence）
     * @param text     要格式化的文本
     * @param textSize 文本大小（-1不设置）
     * @return Spannable
     */
    fun formatStyle(string: CharSequence, text: CharSequence, textSize: Int): Spannable? {
        return formatStyle(string, text, textSize, -1, -1, -1)
    }

    /**
     * 构建一个Spannable。
     *
     * @param string    TextView中的文本（CharSequence）
     * @param text      要格式化的文本
     * @param textSize  文本大小（-1不设置）
     * @param textStyle 文本粗体/斜体等样式（如粗体 Typeface.BOLD）（-1不设置）
     * @return Spannable
     */
    fun formatStyle(string: CharSequence, text: CharSequence, textSize: Int, textStyle: Int): Spannable? {
        return formatStyle(string, text, textSize, textStyle, -1, -1)
    }

    /**
     * 构建一个Spannable。
     *
     * @param string    TextView中的文本（CharSequence）
     * @param text      要格式化的文本
     * @param textSize  文本大小（-1不设置）
     * @param textStyle 文本粗体/斜体等样式（如粗体 Typeface.BOLD）（-1不设置）
     * @param textColor 字体前景颜色（-1不设置）
     * @return Spannable
     */
    fun formatStyle(string: CharSequence, text: CharSequence, textSize: Int, textStyle: Int, textColor: Int): Spannable? {
        return formatStyle(string, text, textSize, textStyle, textColor, -1)
    }

    /**
     * 构建一个Spannable。
     *
     * @param sourceText  TextView中的源文本（CharSequence）
     * @param keyword     要格式化的文本
     * @param textSize    文本大小（-1不设置）
     * @param textStyle   文本粗体/斜体等样式（如粗体 Typeface.BOLD）（-1不设置）
     * @param textColor   字体前景颜色（-1不设置）
     * @param textBgColor 字体背景颜色（-1不设置）
     * @return Spannable
     */
    fun formatStyle(sourceText: CharSequence, keyword: CharSequence, textSize: Int, textStyle: Int, textColor: Int, textBgColor: Int): Spannable? {
        var word: Spannable? = null
        if (TextUtils.isEmpty(sourceText) || TextUtils.isEmpty(keyword)) {
            return word
        }
        val start = sourceText.toString().indexOf(keyword.toString())
        if (start != -1 && !TextUtils.isEmpty(keyword)) {
            val end = start + keyword.length
            word = SpannableString(sourceText)
            if (textSize != -1) {
                word.setSpan(AbsoluteSizeSpan(textSize, true), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE) // 字体大小（dip）
            }
            if (textStyle != -1) {
                word.setSpan(StyleSpan(textStyle), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE) // 字体粗体样式 Typeface.BOLD
            }
            if (textColor != -1) {
                word.setSpan(ForegroundColorSpan(textColor), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE) // 字体前景色 Color.BLUE
            }
            if (textBgColor != -1) {
                word.setSpan(BackgroundColorSpan(textBgColor), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE) // 字体背景色 Color.GRAY
            }
        }
        return word
    }

    /**
     * 给指定的 CharSequence 中的指定关键字设置颜色。
     *
     * @param text    字符串
     * @param keyword 关键字
     * @param color   要设置的颜色
     * @return Spannable
     */
    fun formatColor(sourceText: CharSequence?, keyword: String, color: Int): CharSequence? {
        return formatColor(sourceText, arrayOf(keyword), color)
    }

    /**
     * 给指定的 CharSequence 中的指定关键字设置颜色。
     *
     * @param sourceText 字符串
     * @param color      要设置的颜色
     * @param keywords   关键字数组
     * @return Spannable
     */
    fun formatColor(sourceText: CharSequence?, keywords: Array<String>, color: Int): CharSequence? {
        if (sourceText.isNullOrEmpty()) {
            return sourceText
        }
        val builder = SpannableStringBuilder(sourceText)
        for (keyword in keywords) {
            if (TextUtils.isEmpty(keyword)) {
                continue
            }
            // m.group()    获取匹配后的结果；
            // m.register() 返回以前匹配的初始索引；
            // m.end()      返回最后匹配字符之后的偏移量
            val m = Pattern.compile(keyword).matcher(sourceText)
            while (m.find()) {
                builder.setSpan(ForegroundColorSpan(color), m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
        }
        return builder
    }

    /**
     * 设置文字字体。
     *
     * @param activity   Activity
     * @param viewId     View的ID
     * @param assetsPath 字体路径(assets中的)
     */
    fun setTypeface(activity: Activity, viewId: Int, assetsPath: String?) {
        val view = activity.findViewById<View>(viewId)
        if (view is TextView) {
            setTypeface(view, assetsPath)
        }
    }

    /**
     * TextView设置字体
     *
     * @param textView   TextView
     * @param assetsPath assets下的目录，例如 "fonts/SIMYOU.TTF"
     */
    fun setTypeface(textView: TextView?, assetsPath: String?) {
        if (textView != null && !TextUtils.isEmpty(assetsPath)) {
            //将字体文件保存在assets/fonts/目录下，创建Typeface对象
            textView.typeface = Typeface.createFromAsset(textView.context.assets, assetsPath)
        }
    }

    /**
     * TextView设置字体
     *
     * @param activity Activity
     * @param viewId   资源ID
     * @param ttf      字体 Typeface
     */
    fun setTypeface(activity: Activity, viewId: Int, ttf: Typeface?) {
        val view = activity.findViewById<View>(viewId)
        if (view is TextView) {
            view.typeface = ttf
        }
    }

    /**
     * TextView设置字体
     *
     * @param textView TextView
     * @param ttf      字体 Typeface
     */
    fun setTypeface(textView: TextView, ttf: Typeface?) {
        if (ttf != null) {
            textView.typeface = ttf
        }
    }

    /**
     * 格式化文本样式
     *
     * @param textView    TextView
     * @param textSize    文本大小（0不设置）
     * @param textStyle   文本粗体/斜体等样式（如粗体 Typeface.BOLD）（0不设置）
     * @param textColor   字体前景颜色（0不设置）
     * @param textBgColor 字体背景颜色（0不设置）
     * @return
     */
    fun setStyle(textView: TextView?, textSize: Int, textStyle: Int, textColor: Int, textBgColor: Int) {
        if (textView == null || textView.text == null) return
        var word: Spannable? = null
        val string = textView.text
        if (string.isNotEmpty()) {
            val start = 0
            val end = string.length
            word = SpannableString(string)
            if (textSize != 0) {
                word.setSpan(AbsoluteSizeSpan(textSize, true), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE) // 字体大小（dip）
            }
            if (textStyle != 0) {
                word.setSpan(StyleSpan(textStyle), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE) // 字体粗体样式 Typeface.BOLD
            }
            if (textColor != 0) {
                word.setSpan(ForegroundColorSpan(textColor), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE) // 字体前景色 Color.BLUE
            }
            if (textBgColor != 0) {
                word.setSpan(BackgroundColorSpan(textBgColor), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE) // 字体背景色 Color.GRAY
            }
        }
        textView.text = word
    }

    /**
     * 设置文字粗体/斜体/正常等
     *
     * @param textView  TextView
     * @param textStyle 文本粗体/斜体等样式（如粗体 Typeface.BOLD）（0不设置）
     * @return
     */
    fun setTextStyle(textView: TextView?, textStyle: Int) {
        setStyle(textView, 0, textStyle, 0, 0)
    }
}