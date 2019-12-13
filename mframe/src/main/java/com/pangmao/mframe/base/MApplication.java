package com.pangmao.mframe.base;

import com.pangmao.mframe.MFrame;

import org.litepal.LitePalApplication;


public class MApplication extends LitePalApplication {
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
