package com.pangmao.mframe.base;

import android.app.Application;

import com.pangmao.mframe.MFrame;


public class MApplication extends Application {
    private static MApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MFrame.init(this);
    }


    public static MApplication getInstance() {
        return instance;
    }


}
