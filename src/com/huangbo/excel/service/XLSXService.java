package com.huangbo.excel.service;

import com.huangbo.excel.configs.Config;
import com.huangbo.excel.utils.StringUtil;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.util.Calendar;

/**
 * 工程: excel_parser
 * 包名: com.huangbo.excel.service
 * 创建日期: 2021/11/30
 * 作者: huangbo
 * Company:
 * version: 1.0.1
 * description: 用来解析新版 Excel 文件， 即后缀名为 xlsx 的文件
 **/
public class XLSXService {
    /**
     * 日志对象
     */
    private static final Logger LOG = Logger.getLogger(XLSXService.class);

    /**
     * 解析 xlsx 文件
     *
     * @param inFile  xlsx 文件
     * @param outFile 解析结果文件
     */
    public void parseXlsx(String inFile, String outFile) {
        Workbook wb = getXlsx(inFile);
        ParseService.parseExcel(wb, outFile);
    }

    /**
     * 解析 xlsx 文件
     *
     * @param inFile xlsx 文件
     */
    public void parseXlsx(String inFile) {
        String outFile = inFile.substring(0, inFile.lastIndexOf("."));
        parseXlsx(inFile, outFile);
    }

    /**
     * 获取 xlsx 文件的 WorkBook 对象，由于 xlsx 文件可能非常大，因此需要采取流的方式进行读取
     *
     * @param file 文件
     * @return xlsx 文件对应的 WorkBook 对象
     */
    private Workbook getXlsx(String file) {
        File f = new File(file);
        return StreamingReader.builder().rowCacheSize(100)
                .bufferSize(4096)
                .open(f);
    }
}
