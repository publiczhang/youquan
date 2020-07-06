package com.zww.youquan.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * AppUtil
 *
 * @author Administrator
 */
public class AppUtil {
    public static String getVersion(Context context) {
        String appVersion;
        try {
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            appVersion = "x.x.x";
        }
        return appVersion;
    }
}
