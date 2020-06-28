package com.zww.youquan.util;

import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.Logger;

public class ULogger {

    public static void  _e(String format, Object... o){
        if (BuildConfig.DEBUG) {
            Logger.e(format, o);
        }
    }

    public static void _i(String format, Object... o){
        if (BuildConfig.DEBUG){
            Logger.i(format, o);
        }
    }

    public static void _json(String json){
        if (BuildConfig.DEBUG){
            Logger.json(json);
        }
    }
}
