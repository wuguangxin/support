package com.wuguangxin.base

import android.os.Build
import android.annotation.TargetApi
import android.content.Context
import android.os.LocaleList
import java.util.*

/**
 * APP多语言切换管理器
 *
 * Created by wuguangxin on 2020/8/6.
 */
object LanguageManager {
    /**
     * 设置当前语言
     * @param context
     * @param lang
     */
    fun setLanguage(context: Context, lang: Lang) {
        val resources = context.resources
        val configuration = resources.configuration
        val locale = lang.locale
        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }
        val dm = resources.displayMetrics
        resources.updateConfiguration(configuration, dm)
    }

    fun attachBaseContext(context: Context, lang: Lang): Context {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, lang)
        } else context
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, lang: Lang): Context {
        val resources = context.resources
        val locale = lang.locale
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList(locale))

        //return context.createConfigurationContext(configuration);
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    enum class Lang(
        var langName: String,
        val locale: Locale,
        val countryCode: String,
        val desc: String
    ) {
        ENGLISH("en", Locale.ENGLISH, "+44", "英语"),
        CHINA("zh", Locale.SIMPLIFIED_CHINESE, "+86", "简体中文"),
        CHINA_TW("zh_TW", Locale.TAIWAN, "+886", "繁体中文"),
        KOREA("ko_KR", Locale.KOREA, "+82", "韩语");

        companion object {
            fun getByCountryCode(countryCode: String?): Lang = when (countryCode) {
                ENGLISH.countryCode -> ENGLISH
                KOREA.countryCode -> KOREA
                CHINA.countryCode -> CHINA
                CHINA_TW.countryCode -> CHINA_TW
                else -> ENGLISH
            }

            fun getByLangName(langName: String?): Lang = when(langName) {
                ENGLISH.langName -> ENGLISH
                CHINA.langName -> CHINA
                CHINA_TW.langName -> CHINA_TW
                KOREA.langName -> KOREA
                else -> ENGLISH
            }

            fun getByName(locale: Locale): Lang = when(locale) {
                ENGLISH.locale -> ENGLISH
                CHINA.locale -> CHINA
                CHINA_TW.locale -> CHINA_TW
                KOREA.locale -> KOREA
                else -> ENGLISH
            }

            fun getByDesc(desc: String?): Lang = when(desc) {
                ENGLISH.desc -> ENGLISH
                CHINA.desc -> CHINA
                CHINA_TW.desc -> CHINA_TW
                KOREA.desc -> KOREA
                else -> ENGLISH
            }
        }
    }
}