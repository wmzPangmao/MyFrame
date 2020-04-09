package com.pangmao.mframe.utils.log;

import android.os.Environment;
import android.util.Log;

import com.pangmao.mframe.MFrame;
import com.pangmao.mframe.pojo.DateDifference;
import com.pangmao.mframe.utils.MDateUtils;
import com.pangmao.mframe.utils.MFileUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

public final class MLog {

    private static final Printer printer = new LoggerPrinter();

    private MLog() {

    }

    public static MLogConfig init() {
        return printer.init();
    }

    public static String getFormatLog() {
        return printer.getFormatLog();
    }

    public static void d(String message, Object... args) {
        printer.d(message, args);
    }

    public static void e(String message, Object... args) {
        printer.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        printer.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        printer.i(message, args);
    }

    public static void v(String message, Object... args) {
        printer.v(message, args);
    }

    public static void w(String message, Object... args) {
        printer.w(message, args);
    }

    public static void wtf(String message, Object... args) {
        printer.wtf(message, args);
    }

    public static void json(String json) {
        printer.json(json);
    }

    public static void xml(String xml) {
        printer.xml(xml);
    }

    public static void map(Map map) {
        printer.map(map);
    }

    public static void list(List list) {
        printer.list(list);
    }

    /**
     * 日志文件保存的路径
     */
    private static String path =
            Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mispos/fslog/";
    private static String fileName = "jwLog";

    public static void logger(String msg){
        Log.d(MFrame.tag, msg);
        if (MFrame.isWrite){
            String dataTime = MDateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss");
            MFileUtils.write(dataTime + ": " + msg, path,
                    fileName + MDateUtils.getCurrentDate("yyyyMMdd") + ".log");
        }
    }

    public static void clearLog(){
        File file = new File(path);
        String pattern = "yyyy-MM-dd";
        String nowDate = MDateUtils.getCurrentDate(pattern);
        if (!file.exists()) {
            return;
        }

        if (!file.isFile()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String name = f.getName();
                if(name.length() == 17) {
                    DateDifference dateDifference = MDateUtils.getTwoDataDifference(MDateUtils.string2Date(name.substring(5, 13), pattern));
                    boolean flag = fileName.equalsIgnoreCase(name.substring(0, 5))
                            && dateDifference.getDay() >= 10;
                    if (flag) {
                        logger("删除的文件:" + name);
                        MFileUtils.delFile(f);
                    }
                }
            }
        }
    }
}
