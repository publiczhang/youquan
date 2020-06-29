package com.zww.youquan;


import androidx.multidex.MultiDexApplication;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * MyApp
 *
 * @author zww
 */
public class MyApp extends MultiDexApplication {

    private static MyApp myApp;

    public static MyApp getInstance() {
        return myApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        initLogger();
    }

    private void initLogger() {
        FormatStrategy strategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(2)
                .tag("test")
                .methodOffset(5)
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(strategy));
    }
}
