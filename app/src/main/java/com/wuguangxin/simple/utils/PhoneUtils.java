package com.wuguangxin.simple.utils;

import com.wuguangxin.utils.Utils;

/**
 * Created by wuguangxin on 2021/9/7.
 */

public class PhoneUtils {

    /**
     * 生成随机手机号码
     * @return
     */
    public static String genPhoneNum() {
        int[] firstNum = {
                130, 131, 132, 133, 134, 135, 136, 137, 138, 139,
                145, 147, 149,
                150, 151, 152, 153, 155, 156, 157, 158, 159,
                166,
                170, 171, 172, 173, 175, 176, 177, 178,
                180, 181, 182, 183, 184, 185, 186, 187, 188, 189,
                198, 199
        };
        int first = firstNum[Utils.getRandom(firstNum.length)];
        StringBuilder sb = new StringBuilder().append(first);
        String random = String.valueOf(Utils.getRandom(0, 99999999));
        for (int i = 0; i < 8 - random.length(); i++) {
            sb.append("0");
        }
        sb.append(random);
        return sb.toString();
    }
}
