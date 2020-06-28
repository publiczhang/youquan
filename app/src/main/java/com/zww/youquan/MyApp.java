package com.zww.youquan;


import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class MyApp extends MultiDexApplication {

    private static MyApp myApp;

    public static MyApp getInstance(){
        return myApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApp=this;
        initBaiChuanSDK();
    }

    private void initLogger() {
        //logger
        FormatStrategy strategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(2)
                .tag("test")
                .methodOffset(5)
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(strategy));
    }

    private void initBaiChuanSDK(){
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                Log.d("MyApp","初始化成功");
                Toast.makeText(MyApp.this, "初始化成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.d("MyApp","初始化失败:"+"code---->"+code+",msg--->"+msg);
                Toast.makeText(MyApp.this, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
