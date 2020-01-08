package com.zww.youquan;


import androidx.multidex.MultiDexApplication;

public class MyApp extends MultiDexApplication {

    private static MyApp myApp;

    public static MyApp getInstance(){
        return myApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApp=this;
    }

}
