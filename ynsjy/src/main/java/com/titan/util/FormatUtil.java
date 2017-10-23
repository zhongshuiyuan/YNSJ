package com.titan.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by whs on 2017/10/19
 *
 */

public class FormatUtil {
    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
    /**
     * 格式化时间
     * @param time
     * @return
     */
    public static String getDate(long time) {
        Date d = new Date(time);
        return sf.format(d);

    }

    /**
     * 格式化经纬度
     */
    public static String formatLoc(double loc) {
        DecimalFormat df = new DecimalFormat("#.000000");
        return df.format(loc);
    }

}
