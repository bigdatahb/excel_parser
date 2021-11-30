package com.huangbo.excel.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 工程: excel_parser
 * 包名: com.huangbo.excel.utils
 * 创建日期: 2021/11/30
 * 作者: huangbo
 * Company:
 * version: 1.0.1
 * description:
 **/
public class FileUtil {
    /**
     * 日志对象
     */
    private static final Logger LOG = Logger.getLogger(FileUtil.class);

    /**
     * 获取指定目录下的所有文件
     *
     * @param dir 指定目录
     * @return 该目录下的所有文件(包含子目录中的文件)
     */
    public static List<File> getAllFiles(String dir) {
        List<File> list = new ArrayList<>();
        File[] files = ls(dir);
        if (null == files) {
            return list;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                list.addAll(getAllFiles(f.getAbsolutePath()));
            } else {
                list.add(f);
            }
        }
        return list;
    }

    /**
     * 获取指定目录下的文件， 不包含子目录下的文件
     *
     * @param dir 指定目录
     * @return 指定目录下的文件, 不包含目录
     */
    public static List<File> getFiles(String dir) {
        List<File> list = new ArrayList<>();

        File[] files = ls(dir);
        if (null == files) {
            return list;
        }
        for (File f : files) {
            if (!f.isDirectory()) {
                list.add(f);
            }
        }
        return list;
    }

    /**
     * 创建文件夹
     *
     * @param dir 文件夹
     */
    public static void createDirIfNotExists(String dir) {
        File dirF = new File(dir);
        if (!dirF.exists() || !dirF.isDirectory()) {
            if (dirF.mkdirs()) {
                LOG.info("创建目录 " + dir + " 成功!");
            } else {
                LOG.info("创建目录 " + dir + " 失败!");
            }
        }
    }

    /**
     * 列出目录下的文件, 同 linux ls 命令
     *
     * @param dir 目录名
     * @return 文件数组
     */
    public static File[] ls(String dir) {
        File dirF = new File(dir);
        if (!dirF.exists() || !dirF.isDirectory()) {
            LOG.error("指定目录 " + dir + " 不存在");
            return null;
        }
        File[] files = dirF.listFiles();
        if (null == files || files.length == 0) {
            return null;
        }
        return files;
    }
}
