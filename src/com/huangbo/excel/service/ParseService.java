package com.huangbo.excel.service;

import com.huangbo.excel.configs.Config;
import com.huangbo.excel.utils.StringUtil;
import org.apache.log4j.Logger;
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
 * description:
 **/
public class ParseService {

    private static final Logger LOG = Logger.getLogger(ParseService.class);

    /**
     * 解析 excel 文件中的一个 sheet 页
     *
     * @param sheet   sheet 页对象
     * @param outFile 解析结果文件
     */
    private static void parseSheet(Sheet sheet, String outFile) {
        //获取页名
        String sheetName = sheet.getSheetName();

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("utf-8")));
            //建立数据缓冲区
            StringBuilder buffer = new StringBuilder();

            int cnt = 0; // 用来记录解析的行数
            for (Row row : sheet) {
                ++cnt;
                int colNum = row.getPhysicalNumberOfCells(); // 获取当前行列数
                for (int index = 0; index < colNum; ++index) {
                    Cell c = row.getCell(index);
                    if (null == c) {
                        buffer.append(",");
                        continue;
                    }
                    String value = "";
                    CellType type = c.getCellType();
                    if (CellType.NUMERIC.equals(type)) {
                        // numeric 类型也有可能是日期 , HSSFDateUtil 已过期，使用 DateUtil
                        if (DateUtil.isCellDateFormatted(c)) {
                            // 获取单元格日期
                            Calendar cal = DateUtil.getJavaCalendar(c.getNumericCellValue());
                            // 获取年月日、时分秒
                            int year = cal.get(Calendar.YEAR);
                            int month = cal.get(Calendar.MONTH) + 1;
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            int hour = cal.get(Calendar.HOUR_OF_DAY);
                            int minute = cal.get(Calendar.MINUTE);
                            int second = cal.get(Calendar.SECOND);
                            String dateStr;
                            if (year == 1899 && month == 12 && day == 31) {
                                dateStr = "\"" + hour + ":" + minute + ":" + second + "\"";
                            } else {
                                dateStr = "\"" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "\"";
                            }
                            buffer.append(dateStr).append(",");
                            continue;
                        } else {
                            value += c.getNumericCellValue();
                            if (value.contains("E")) {
                                // 科学计数法， 需要转换成普通数值字串
                                value = StringUtil.eToPlainString(value);
                                value = StringUtil.eraseTailZeros(value);
                            }
                        }
                    } else if (CellType.FORMULA.equals(type)) {
                        // 若是公式类型, 获取字符串将得到公式的表达式， 因此这里需要直接获取单元格的数值
                        value = c.getNumericCellValue() + "";
                        value = StringUtil.eraseTailZeros(value);
                    } else {
                        value = c.getStringCellValue();
                    }
                    value = value.trim();
                    // 转换成csv格式
                    value = StringUtil.textCleanCsv(value);
                    buffer.append(value).append(",");
                }
                // 删除最后一个逗号
                if (buffer.length() > 0) {
                    buffer.delete(buffer.lastIndexOf(","), buffer.lastIndexOf(",") + 1);
                }
                buffer.append(System.lineSeparator()); // 添加换行符

                if (cnt % Config.BUFFER_SIZE == 0) {
                    // 缓冲区满, 写入数据至文件

                    // 删除最后一个换行符
                    buffer.delete(buffer.lastIndexOf(System.lineSeparator()), buffer.length());
                    // 写入
                    bw.write(buffer.toString());
                    // 清空缓冲区
                    buffer.delete(0, buffer.length());
                    //往缓冲区添加换行符
                    buffer.append(System.lineSeparator());
                }
            }
            if (buffer.length() > 0) {
                buffer.delete(buffer.lastIndexOf(System.lineSeparator()), buffer.length());
                bw.write(buffer.toString());
                buffer.delete(0, buffer.length());
            }
            LOG.info("页 " + sheetName + " 解析完毕, " + "共解析 " + cnt + " 行记录.");

            // 若是文件大小为0， 删除文件
            if(0 == new File(outFile).length()){
                // 文件大小为 0 删除文件
                new File(outFile).delete();
            }
        } catch (FileNotFoundException e) {
            LOG.error("打开文件 " + outFile + " 失败", e);
        } catch (IOException e) {
            LOG.error("写入文件  " + outFile + " 异常", e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    LOG.error("文件流关闭异常", e);
                }
            }
        }
    }


    /**
     * 解析 excel 文件
     * @param workbook excel文件对应的 WorkBook 对象
     * @param outFile 解析结果文件名
     */
      static void parseExcel(Workbook workbook, String outFile){
        if (null == workbook) {
            return;
        }
        // 获取页数
        int sheetNum = workbook.getNumberOfSheets();

        String baseName;
        String suffix;
        if (outFile.contains(".")) {
            baseName = outFile.substring(0, outFile.lastIndexOf("."));
            suffix = outFile.substring(outFile.lastIndexOf("."));
        } else {
            baseName = outFile;
            suffix = ".csv";
        }

        for(int page = 0; page < sheetNum; ++page){
            // 处理第 page + 1 页
            Sheet sheet = workbook.getSheetAt(page);
            // 页名
            String sheetName = sheet.getSheetName();
            String out =  baseName + "_" + sheetName + suffix;
            ParseService.parseSheet(sheet, out);
        }
    }
}
