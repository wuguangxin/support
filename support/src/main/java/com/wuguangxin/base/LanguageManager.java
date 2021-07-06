package com.wuguangxin.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * APP多语言切换管理器
 * 
 * Created by wuguangxin on 2020/8/6.
 */
public class LanguageManager {

    /**
     * 设置当前语言
     * @param context
     * @param lang
     */
    public static void setLanguage(Context context, Lang lang) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = lang.locale;
        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }

    public static Context attachBaseContext(Context context, Lang lang) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, lang);
        }
        return context;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Lang lang) {
        Resources resources = context.getResources();
        Locale locale = lang.locale;
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));

//        return context.createConfigurationContext(configuration);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    public enum Lang {

        ENGLISH("en", Locale.ENGLISH, "+44", "英语"),
        CHINA("zh", Locale.SIMPLIFIED_CHINESE, "+86", "简体中文"),
        CHINA_TW("zh_TW", Locale.TAIWAN, "+886", "繁体中文"),
        KOREA("ko_KR", Locale.KOREA, "+82", "韩语");

        public String name;
        public Locale locale;
        public String countryCode;
        public String desc;

        Lang(String name, Locale locale, String countryCode, String desc) {
            this.name = name;
            this.locale = locale;
            this.countryCode = countryCode;
            this.desc = desc;
        }

        public static Lang getByCountryCode(String countryCode) {
            if (TextUtils.equals(countryCode, ENGLISH.countryCode)) return ENGLISH;
            else if (TextUtils.equals(countryCode, CHINA.countryCode)) return CHINA;
            else if (TextUtils.equals(countryCode, CHINA_TW.countryCode)) return CHINA_TW;
            else if (TextUtils.equals(countryCode, KOREA.countryCode)) return KOREA;
            else return ENGLISH;
        }

        public static Lang getByName(String name) {
            if (TextUtils.equals(name, ENGLISH.name)) return ENGLISH;
            else if (TextUtils.equals(name, CHINA.name)) return CHINA;
            else if (TextUtils.equals(name, CHINA_TW.name)) return CHINA_TW;
            else if (TextUtils.equals(name, KOREA.name)) return KOREA;
            else return ENGLISH;
        }

        public static Lang getByName(Locale locale) {
            if (TextUtils.equals(locale.toString(), ENGLISH.locale.toString())) return ENGLISH;
            else if (TextUtils.equals(locale.toString(), CHINA.locale.toString())) return CHINA;
            else if (TextUtils.equals(locale.toString(), CHINA_TW.locale.toString())) return CHINA_TW;
            else if (TextUtils.equals(locale.toString(), KOREA.locale.toString())) return KOREA;
            else return ENGLISH;
        }

        public static Lang getByDesc(String desc) {
            if (TextUtils.equals(desc, ENGLISH.desc)) return ENGLISH;
            else if (TextUtils.equals(desc, CHINA.desc)) return CHINA;
            else if (TextUtils.equals(desc, CHINA_TW.desc)) return CHINA_TW;
            else if (TextUtils.equals(desc, KOREA.desc)) return KOREA;
            else return ENGLISH;
        }
    }

}