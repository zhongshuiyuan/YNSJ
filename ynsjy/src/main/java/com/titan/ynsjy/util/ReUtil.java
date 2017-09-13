package com.titan.ynsjy.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by whs on 2017/9/13
 * 正则工具类
 */

public class ReUtil {
    /**
     * 判读字符串是否包含中文
     * @param str
     * @return
     */
    public static boolean hasChinese(String str){
        // 创建 Pattern 对象
        String reg = "[\\u4e00-\\u9fa5]+";//表示+表示一个或多个中文，
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str.trim());
        return matcher.matches();

    }



}
