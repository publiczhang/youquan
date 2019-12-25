package com.zww.youquan.base;

import android.app.Application;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;

public class UmengUtil {

    public static void reportError(Application ctx, String stringFormate, Object ... o){
        String str = String.format(stringFormate,o);
        if (TextUtils.isEmpty(str)){
            return;
        }
        MobclickAgent.reportError(ctx,str);
    }
}
