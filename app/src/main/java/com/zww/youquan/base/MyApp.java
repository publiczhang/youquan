package com.zww.youquan.base;

import android.app.Application;

public class MyApp extends Application {

    private static MyApp myApp;

    private MyApp(){

    }

    public static MyApp getInstance(){
        return myApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApp=this;
    }
}
