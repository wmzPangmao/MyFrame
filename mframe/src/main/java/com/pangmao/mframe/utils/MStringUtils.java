package com.pangmao.mframe.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MStringUtils {

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        return parseByte2HexStr(buf, 0, buf.length);
    }

    public static String parseByte2HexStr(byte buf[], int offset, int lenght) {
        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /***
     * 判断字符串是否是数字字符串
     * @param str 待判断字符串
     * @return int 0.是 1.否
     */
    public static int isNum(String str){
        int ErrorFlag = 0;
        int i = 0;
        if (str.equals("")) {
            ErrorFlag = 1;
        } else {
            for (i = 0; i < str.length(); i++) {
                if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                    ErrorFlag = 1;
                    break;
                }
            }
        }
        return ErrorFlag;
    }

    public static boolean isLeagel(String str){
        if(str != null && str.length() > 0) {
            return true;
        }else {
            return false;
        }
    }

    /***
     * 检查str是否为汉字
     * @param str 待检查字符串
     * @return true-是汉字 false-非汉字
     */
    public static boolean isChineseChar(String str){
        boolean flag = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            flag = true;
        }
        return flag;
    }

    /***
     * 填充字符串
     * @param desc 原数据
     * @param ch 填充字符
     * @param len 填充后长度
     * @param direction 0:左填充 1:右填充
     * @return String
     */
    public static String fillStr(int desc, char ch, int len, int direction){
        String descval = String.valueOf(desc);
        if (len > descval.length()) {
            int s = len - descval.length();
            for (int i = 0; i < s; i++) {
                if (direction == 0) {
                    descval = ch + descval;
                } else {
                    descval = descval + ch;
                }
            }
        }
        return descval;
    }
    public static String fillStrLeft(int desc, char ch, int len){
        return fillStr(desc, ch, len, 0);
    }
    public static String fillStrRight(int desc, char ch, int len){
        return fillStr(desc, ch, len, 1);
    }
    /***
     * 字符串长度不够前面或后面补0
     * @param str 字符串
     * @param strLength 需要长度
     * @param mode 补充模式 0-前补 1-后补
     * @return String
     */
    public static String addZeroForNum(String str, int strLength, int mode){
        int strLen = str.length();
        StringBuffer sb = null;
        while (strLen < strLength) {
            sb = new StringBuffer();
            if (mode == 0) {
                sb.append("0").append(str);
            } else {
                sb.append(str).append("0");
            }
            str = sb.toString();
            strLen = str.length();
        }
        return str;
    }
    public static String addZeroFroNumLeft(String str, int strLength){
        return addZeroForNum(str, strLength, 0);
    }
    public static String addZeroFroNumRight(String str, int strLength){
        return addZeroForNum(str, strLength, 1);
    }

    public static List<String> splitStr(String resource, String split){
        List<String> list = new ArrayList<>(10);
        String str2 = resource;
        int end = 0;
        while (true){
            end = str2.indexOf(split);
            if(end < 0) {
                break;
            }else if(end == 0) {
                str2 = str2.substring(end+1);
                list.add("");
            }else {
                String str3 = str2.substring(0 , end);
                list.add(str3);
                str2 = str2.substring(end+1);
            }
        }
        return list;
    }
}
