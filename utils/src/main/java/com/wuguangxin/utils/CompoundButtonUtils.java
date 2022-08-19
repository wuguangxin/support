package com.wuguangxin.utils;

import android.widget.CompoundButton;

import java.lang.reflect.Field;

public class CompoundButtonUtils {

    /**
     * 设置
     * @param checkBox
     * @param checked
     */
    public static void setChecked(CompoundButton checkBox, boolean checked) {
        if (checkBox == null || checkBox.isChecked() == checked) {
            return;
        }
        try {
            // 获取class对象
            Class clazz = checkBox.getClass();
            while (!clazz.getSimpleName().equals("CompoundButton")) {
                clazz = clazz.getSuperclass();
            }

            // 获取对象的属性
            Field field = clazz.getDeclaredField("mOnCheckedChangeListener");
            // 对象的属性设置为可访问
            field.setAccessible(true);
            Object value = field.get(checkBox);
            if (value != null) {
                CompoundButton.OnCheckedChangeListener listener = (CompoundButton.OnCheckedChangeListener) value;
                checkBox.setOnCheckedChangeListener(null);
                checkBox.setChecked(checked);
                checkBox.setOnCheckedChangeListener(listener);
            }

//            // 5. 获取所有字段
//            Field[] fields = clazz.getDeclaredFields();
//            for (Field f : fields) {
//                // 设置字段的可见性
//                f.setAccessible(true);
//                String name = f.getName();
//                Object o = f.get(person);
//                System.out.println(name + " - " + o);
//            }
//
//            // 6. 获取所有的方法
//            Method[] methods = clazz.getDeclaredMethods();
//            for (Method m : methods) {
//                m.setAccessible(true);
//                String name = m.getName();
//                Object invoke = m.invoke(person, "张三");
//                System.out.println(name + " = " + invoke);
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}