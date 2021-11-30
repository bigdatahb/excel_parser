package com.huangbo.excel.utils;

import java.math.BigDecimal;

/**
 * project: excel_parser
 * author: huangbo
 * date: 2021/11/30
 * version: v1.0.0
 * description:
 */
public class StringUtil {

    /**
     * 判断字符串是否为 null 或者空串
     *
     * @param s 要判断的字符串
     * @return 当s为null或者空串时返回 true, 否则返回 false
     */
    public static boolean isEmpty(String s) {
        return null == s || s.length() == 0;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param s 要判断的字符串
     * @return s不为空返回true, 否则返回false
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 科学计数法转普通字串
     *
     * @param value 要转换的值
     * @return 转换结果
     */
    public static String eToPlainString(String value) {
        return new BigDecimal(value).toPlainString();
    }

    /**
     * 去除数值字符串尾部多余的 0
     *
     * @param value 要操作的字符串
     * @return 操作结果
     */
    public static String eraseTailZeros(String value) {
        if (isEmpty(value) || !value.contains(".")) {
            return value;
        }
        String[] vals = value.split("\\.");
        if (vals.length != 2) {
            return value;
        }
        if (allZeros(vals[1])) {
            return vals[0];
        }
        String tail = vals[1];
        while (tail.indexOf('0') == tail.length() - 1) {
            tail = tail.substring(0, tail.length() - 1);
        }
        // 经上述 allZeros 检测 ，尾部字串不可能全为 0, 所以这里不在对 tail 全为 0 的情况进行处理
        return vals[0] + "." + tail;

    }

    /**
     * 将指定字符串转换成 csv 格式
     * 在csv中，对于值里包含","的，整个值需要用双引号包起来
     * 若值中本身包含双引号 ， 则需要在双引号前添加双引号对其进行转义
     *
     * @param value 要转换的字符串
     * @return 转换结果
     */
    public static String textCleanCsv(String value) {
        while (value.contains("\r")) {
            int index = value.indexOf("\r");
            String pre = value.substring(0, index);
            String post = value.substring(index + "\r".length());
            // 去除 "\r"
            value = pre + post;
        }

        while (value.contains("\n")) {
            int index = value.indexOf("\n");
            String pre = value.substring(0, index);
            String post = value.substring(index + "\n".length());
            // 去除 "\n", 转换成系统换行符
            value = pre + System.lineSeparator() + post;
        }

        // 去除空字符
        while (value.indexOf('\0') != -1) {
            int pos = value.indexOf('\0');
            String pre = value.substring(0, pos);
            String post = "";
            if (pos < value.length()) {
                post = value.substring(pos + 1);
            }
            value = pre + post;
        }

        if (isEmpty(value)) {
            return "";
        }

        // 处理双引号 "\""
        if (value.contains("\"")) {
            StringBuilder buf = new StringBuilder(value);
            int pos = 0;
            while ((pos = buf.indexOf("\"", pos)) != -1) {
                buf.insert(pos, "\""); // csv 中需要使用双引号来转义双引号
                pos = buf.indexOf("\"", pos + 2);
                if (pos >= buf.length() || pos == -1) {
                    break;
                }
            }
            value = buf.toString();
        }
        // 处理字段值中的逗号
        if (value.contains(",")) {
            value = "\"" + value + "\"";
        }
        return value;
    }

    /**
     * 判断字符串是否全是 0
     *
     * @param value 要判断的字符串
     * @return 全是0返回 true, 否则返回 false
     */
    private static boolean allZeros(String value) {
        if (isEmpty(value)) {
            return false;
        }
        for (char c : value.toCharArray()) {
            if (c != '0') {
                return false;
            }
        }
        return true;
    }


}
