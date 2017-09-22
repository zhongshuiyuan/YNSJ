package com.titan.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by whs on 2017/9/21
 * app常用方法
 */

public class AppUtil {
    /**
     * 获取应用程序版本号
     * @param context
     * @return
     */
    public static int getAppVersion(Context context) {
        try {
            if(context == null){
                return 1;
            }
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
