package com.pangmao.mframe.utils.log;


import android.text.TextUtils;

import com.pangmao.mframe.MFrame;


public class MLogConfig {

    private boolean showThreadInfo = true;
    private boolean debug = MFrame.isDebug;
    private String tag = MFrame.tag;


    public MLogConfig setTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            this.tag = tag;
        }
        return this;
    }

    public MLogConfig setShowThreadInfo(boolean showThreadInfo) {
        this.showThreadInfo = showThreadInfo;
        return this;
    }


    public MLogConfig setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }
}
