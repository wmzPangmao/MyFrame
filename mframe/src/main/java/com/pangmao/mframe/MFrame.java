package com.pangmao.mframe;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.pangmao.mframe.utils.MOutdatedUtils;
import com.pangmao.mframe.utils.http.IHttpEngine;
import com.pangmao.mframe.utils.http.MHttp;
import com.pangmao.mframe.utils.log.MLog;
import com.pangmao.mframe.utils.log.MLogConfig;
import com.pangmao.mframe.widget.loadingview.MLoadingView;
import com.pangmao.mframe.widget.loadingview.MLoadingViewConfig;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

public class MFrame {
    private static Context context;

    // #log
    public static String tag = "MFrame";
    public static boolean isDebug = true;

    public static void init(Context context) {
        MFrame.context = context;
    }

    public static MLogConfig initXLog() {
        return MLog.init();
    }

    public static void initXHttp(IHttpEngine httpEngine) {
        MHttp.init(httpEngine);
    }

    public static MLoadingViewConfig initXStatusView() {
        return MLoadingView.init();
    }

    public static Context getContext() {
        return context;
    }

    public static String getStrResources(@StringRes int id) {
        return getResources().getString(id);
    }

    public static Resources.Theme getTheme() {
        return MFrame.getContext().getTheme();
    }

    public static Resources getResources() {
        return MFrame.getContext().getResources();
    }

    public static AssetManager getAssets() {
        return MFrame.getContext().getAssets();
    }

    public static int getColor( @ColorRes int id) {
        return MOutdatedUtils.getColor(id);
    }
}
