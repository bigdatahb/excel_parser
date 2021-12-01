package com.huangbo.excel;

import com.huangbo.excel.configs.Config;
import com.huangbo.excel.service.XLSService;
import com.huangbo.excel.service.XLSXService;
import com.huangbo.excel.utils.FileUtil;
import com.huangbo.excel.utils.StringUtil;
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
        List<File> fileList ;
        if (Config.RECURSIVE) {
            fileList = FileUtil.getAllFiles(Config.DATA_DIR);
        } else {
            fileList = FileUtil.getFiles(Config.DATA_DIR);
        }

        if(fileList.size() == 0){
            LOG.info("指定数据目录" + Config.DATA_DIR + " 下没有要解析的文件");
            System.exit(-2);
        }

        // 获取输出目录
        String outDir = Config.RESULT_DIR;
        if(StringUtil.isEmpty(outDir)){
            outDir = Config.DATA_DIR;
        }

        XLSService xlsService = new XLSService();
        XLSXService xlsxService = new XLSXService();

        for(File f : fileList){
            String file = f.getAbsolutePath();
            String fileName = file.substring(file.lastIndexOf(System.getProperty("file.separator")) + 1);
            if(!fileName.contains(".")){
//                LOG.info("文件 " + file + " 不是excel文件");
                continue;
            }

            // 获取文件类型
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if(!"xls".equals(suffix) && !"xlsx".equals(suffix)){
                continue;
            }
            LOG.info("开始解析文件 " + file + " ...");
            String outFile = outDir + System.getProperty("file.separator") + fileName + ".csv";
            if("xls".equalsIgnoreCase(suffix)){
                // xls 文件
                xlsService.parseXls(file, outFile);
            }
            if("xlsx".equalsIgnoreCase(suffix)){
                // xlsx 文件
                xlsxService.parseXlsx(file, outFile);
            }
            LOG.info("文件 " + fileName + " 解析完毕!");

        }
    }
}
