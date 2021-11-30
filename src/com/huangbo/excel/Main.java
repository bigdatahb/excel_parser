package com.huangbo.excel;

import com.huangbo.excel.configs.Config;
import com.huangbo.excel.utils.FileUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

/**
 * project: excel_parser
 * author: huangbo
 * date: 2021/11/30
 * version: v1.0.0
 * description:
 */
public class Main {
    /**
     * 日志对象
     */
    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        // 获取数据目录
        File directory = new File(Config.DATA_DIR);

        if (!directory.exists() || !directory.isDirectory()) {
            LOG.error("指定目录 " + directory + " 不存在!");
            System.exit(-1);
        }

        // 获取目录下数据文件
        List<File> fileList;
        if (Config.RECURSIVE) {
            fileList = FileUtil.getAllFiles(Config.DATA_DIR);
        } else {
            fileList = FileUtil.getFiles(Config.DATA_DIR);
        }


    }
}
