package com.pangmao.mframe;

import android.content.Context;
import android.content.res.Resources;

public class MFrame {
    private static Context context;

    // #log
    public static String tag = "MFrame";
    public static boolean isDebug = true;

    public static void init(Context context) {
        MFrame.context = context;
    }

    public static Context getContext() {
        return context;
    }

    public static Resources getResources() {
        return MFrame.getContext().getResources();
    }
}
