package com.wuguangxin.utils;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对TextView中的文字样式设置的工具类
 *  http://blog.csdn.net/wgxhxh/article/details/45668921
 *  http://www.jb51.net/article/37229.htm
 *
 * <p>Created by wuguangxin on 15/5/12 </p>
 */
public class FontUtils{
	/**
	 * 设置指定String在TextView中的颜色，比如搜索结果高亮搜索关键字
	 * @param textView TextView
	 * @param keyword 要高亮的文字
	 * @param color 高亮颜色
	 */
	public static void setFontColor(TextView textView, String keyword, int color){
		if (textView != null) {
			textView.setText(formatColor(textView.getText(), keyword, color));
		}
	}
	
	/**
	 * 
	 * @param view TextView
	 * @param text 要改变的文本
	 * @param textSize 文本字体大小（单位dip）（-1不设置）
	 */
	public static void setFontStyle(TextView view, String text, int textSize){
		if(view != null){
			view.setText(formatStyle(view.getText(), text, textSize, -1, -1, -1));
		}
	}
	
	/**
	 * 
	 * @param view TextView
	 * @param text 要改变的文本
	 * @param textSize 文本字体大小（单位dip）（-1不设置）
	 * @param textStyle 字体样式（如粗体 Typeface.BOLD）（-1不设置）
	 */
	public static void setFontStyle(TextView view, String text, int textSize, int textStyle){
		if(view != null){
			view.setText(formatStyle(view.getText(), text, textSize, textStyle, -1, -1));
		}
	}
	
	/**
	 * 
	 * @param view TextView
	 * @param text 要改变的文本
	 * @param textSize 文本字体大小（单位dip）（-1不设置）
	 * @param textStyle 字体样式（如粗体 Typeface.BOLD）（-1不设置）
	 * @param textColor 字体前景颜色（-1不设置）
	 */
	public static void setFontStyle(TextView view, String text, int textSize, int textStyle, int textColor){
		if(view != null){
			view.setText(formatStyle(view.getText(), text, textSize, textStyle, textColor, -1));
		}
	}
	
	/**
	 * 
	 * @param view TextView
	 * @param text 要改变的文本
	 * @param textSize 文本字体大小（单位dip）（-1不设置）
	 * @param textStyle 字体样式（如粗体 Typeface.BOLD）（-1不设置）
	 * @param textColor 字体前景颜色（-1不设置）
	 * @param textBgColor 字体背景颜色（-1不设置）
	 */
	public static void setFontStyle(TextView view, String text, int textSize, int textStyle, int textColor, int textBgColor){
		if(view != null){
			view.setText(formatStyle(view.getText(), text, textSize, textStyle, textColor, textBgColor));
		}
	}
	
	/**
	 * 格式化文本样式
	 * @param string TextView中的文本（CharSequence）
	 * @param text 要格式化的文本
	 * @param textSize 文本大小（-1不设置）
	 * @return
	 */
	public static Spannable formatStyle(CharSequence string, CharSequence text, int textSize){
		return formatStyle(string, text, textSize, -1, -1, -1);
	}
	
	/**
	 * 格式化文本样式
	 * @param string TextView中的文本（CharSequence）
	 * @param text 要格式化的文本
	 * @param textSize 文本大小（-1不设置）
	 * @param textStyle 文本粗体/斜体等样式（如粗体 Typeface.BOLD）（-1不设置）
	 * @return
	 */
	public static Spannable formatStyle(CharSequence string, CharSequence text, int textSize, int textStyle){
		return formatStyle(string, text, textSize, textStyle, -1, -1);
	}
	
	/**
	 * 格式化文本样式
	 * @param string TextView中的文本（CharSequence）
	 * @param text 要格式化的文本
	 * @param textSize 文本大小（-1不设置）
	 * @param textStyle 文本粗体/斜体等样式（如粗体 Typeface.BOLD）（-1不设置）
	 * @param textColor 字体前景颜色（-1不设置）
	 * @return
	 */
	public static Spannable formatStyle(CharSequence string, CharSequence text, int textSize, int textStyle, int textColor){
		return formatStyle(string, text, textSize, textStyle, textColor, -1);
	}
	
	/**
	 * 格式化文本样式
	 * @param string TextView中的文本（CharSequence）
	 * @param text 要格式化的文本
	 * @param textSize 文本大小（-1不设置）
	 * @param textStyle 文本粗体/斜体等样式（如粗体 Typeface.BOLD）（-1不设置）
	 * @param textColor 字体前景颜色（-1不设置）
	 * @param textBgColor 字体背景颜色（-1不设置）
	 * @return
	 */
	public static Spannable formatStyle(CharSequence string, CharSequence text, int textSize, int textStyle, int textColor, int textBgColor){
		Spannable word = null;
		if(!TextUtils.isEmpty(string) && !TextUtils.isEmpty(text)){
			int start = string.toString().indexOf(text.toString());
			if(start != -1){
				if(!TextUtils.isEmpty(text)){
					int end = start + text.length();
					word = new SpannableString(string);
					if(textSize != -1){
						word.setSpan(new AbsoluteSizeSpan(textSize, true), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);		// 字体大小（dip）
					}
					if(textStyle != -1){
						word.setSpan(new StyleSpan(textStyle), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);				// 字体粗体样式 Typeface.BOLD
					}
					if(textColor != -1){
						word.setSpan(new ForegroundColorSpan(textColor), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 	// 字体前景色 Color.BLUE
					}
					if(textBgColor != -1){
						word.setSpan(new BackgroundColorSpan(textBgColor), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 	// 字体背景色 Color.GRAY
					}
				}
			}
		}
		return word;
	}

	/**
	 * 格式化字符串text中所有包含的关键字keyword的颜色为color并返回
	 * @param text 字符串
	 * @param keyword 关键字
	 * @param color 要设置的颜色
	 * @return
	 */
	public static CharSequence formatColor(CharSequence text, String keyword, int color){
		if (TextUtils.isEmpty(text)) {
			return text;
		}
		Matcher m = Pattern.compile(keyword).matcher(text);
		// m.group() 获取匹配后的结果；m.register() 返回以前匹配的初始索引；m.end()返回最后匹配字符之后的偏移量
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		while (m.find()) {
			builder.setSpan(new ForegroundColorSpan(color), m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		}
		return builder;
	}

	/**
	 * TextView设置字体
	 * @param activity
	 * @param viewId
	 * @param assetsPath
     */
	public static void setTypeface(Activity activity, int viewId, String assetsPath){
		View view = activity.findViewById(viewId);
		if(view instanceof TextView){
			setTypeface(((TextView) view), assetsPath);
		}
	}

	/**
	 * TextView设置字体
	 * @param textView
	 * @param assetsPath assets下的目录，例如 "fonts/SIMYOU.TTF"
     */
	public static void setTypeface(TextView textView, String assetsPath){
		if (textView != null && !TextUtils.isEmpty(assetsPath)) {
			//将字体文件保存在assets/fonts/目录下，创建Typeface对象
			AssetManager manager = textView.getContext().getAssets();
			Typeface ttf = Typeface.createFromAsset(manager, assetsPath);
			textView.setTypeface(ttf);
		}
	}

	/**
	 * TextView设置字体
	 * @param activity
	 * @param viewId 资源ID
	 * @param ttf
     */
	public static void setTypeface(Activity activity, int viewId, Typeface ttf){
		View view = activity.findViewById(viewId);
		if(view instanceof TextView){
			((TextView) view).setTypeface(ttf);
		}
	}

	/**
	 * TextView设置字体
	 * @param textView
	 * @param ttf
     */
	public static void setTypeface(TextView textView, Typeface ttf){
		if (ttf != null) {
			textView.setTypeface(ttf);
		}
	}
}
