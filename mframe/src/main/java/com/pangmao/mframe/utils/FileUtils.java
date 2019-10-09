package com.pangmao.mframe.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtils {

    public static boolean write(String content, String path, String fileName) {
        try {
            makeRootDirectory(path);
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(path + fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.write(content.getBytes("utf-8"));
            randomFile.writeBytes("\r\n");
            randomFile.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean write(String content, String path) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(path, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.write(content.getBytes("utf-8"));
            randomFile.writeBytes("\r\n");
            randomFile.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String read(String path) throws IOException {
        /**
         * model各个参数详解
         * r 代表以只读方式打开指定文件
         * rw 以读写方式打开指定文件
         * rws 读写方式打开，并对内容或元数据都同步写入底层存储设备
         * rwd 读写方式打开，对文件内容的更新同步更新至底层存储设备
         *
         * **/
        RandomAccessFile randomFile = new RandomAccessFile(path, "r");
        StringBuilder builder = new StringBuilder();
        byte[]  buff=new byte[1024];
        //用于保存实际读取的字节数
        int hasRead=0;
        //循环读取
        while((hasRead = randomFile.read(buff))>0){
            //打印读取的内容,并将字节转为字符串输入
            builder.append(new String(buff,0, hasRead));
        }
        return builder.toString();
    }

    public static boolean delFile(String filePath){
        boolean flag = false;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                flag = file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean delFile(File file) {
        if (file.isFile()) {
            return file.delete();
        } else {
            String[] filenames = file.list();
            for (String f : filenames) {
                delFile(f);
            }
            return file.delete();
        }
    }

    //生成目录
    private static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // 生成文件
    private File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
