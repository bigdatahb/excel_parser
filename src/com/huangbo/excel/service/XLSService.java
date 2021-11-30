package com.huangbo.excel.service;

import com.huangbo.excel.configs.Config;
import com.huangbo.excel.utils.StringUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Calendar;

/**
 * 工程: excel_parser
 * 包名: com.huangbo.excel.service
 * 创建日期: 2021/11/30
 * 作者: huangbo
 * Company:
 * version: 1.0.1
 * description: 用来解析老版的 Excel 文件, 即后缀名为 xls 的文件
 **/
public class XLSService {

    /**
     * 日志对象
     */
    private static final Logger LOG = Logger.getLogger(XLSService.class);

    /**
     * 解析 xls 文件
     *
     * @param inFile  xls 文件
     * @param outFile 解析结果文件
     */
    public void parseXls(String inFile, String outFile) {
        HSSFWorkbook hwb = getXls(inFile);
        ParseService.parseExcel(hwb, outFile);
    }

    /**
     * 解析 xls 文件
     *
     * @param inFile xls 文件
     */
    public void parseXls(String inFile) {
        String outFile = inFile.substring(0, inFile.lastIndexOf(".")) + ".csv";
        parseXls(inFile, outFile);
    }


    private HSSFWorkbook getXls(String file) {
        try {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new File(file));
            return new HSSFWorkbook(poifsFileSystem);
        } catch (IOException e) {
            LOG.error("读取文件 " + file + " 异常", e);
        }
        return null;
    }

}
