package com.pangmao.mframe.utils;

import com.pangmao.mframe.MFrame;
import com.pangmao.mframe.utils.log.MLog;

/**
 * 此类用于框架系统打印输出控制，使用者用XLog格式化体验更好。
 *
 */
public class MPrintUtils {

    private MPrintUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static String tag = MFrame.tag;
    private static boolean log = MFrame.isWrite;

    public static void setLog(boolean log) {
        MPrintUtils.log = log;
    }

    public static void i(String msg) {
        if (log) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (log) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void d(String msg) {
        if (log) {
            MLog.d(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (log) {
            MLog.d(tag, msg);
        }
    }

    public static void w(String msg) {
        if (log) {
            MLog.w(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (log) {
            MLog.w(tag, msg);
        }
    }

    public static void v(String msg) {
        if (log) {
            MLog.v(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (log) {
            MLog.v(tag, msg);
        }
    }

    public static void e(String msg) {
        MLog.e(tag, msg);
    }

    public static void e(String tag, String msg) {
        MLog.e(tag, msg);
    }
}
