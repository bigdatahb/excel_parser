package com.huangbo.excel.configs;

import com.huangbo.excel.utils.StringUtil;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Set;

/**
 * project: excel_parser
 * author: huangbo
 * date: 2021/11/30
 * version: v1.0.0
 * description:
 */
public class Config {

    private static final Logger LOG = Logger.getLogger(Config.class);


    public static String DATA_DIR = System.getProperty("user.dir");

    public static String RESULT_DIR;

    public static boolean RECURSIVE = false;

    public static int BUFFER_SIZE = 10000;

    static {
        Properties pro = new Properties();

        try {
            pro.load(new InputStreamReader(new FileInputStream("config.properties")));

            Set<String> keys = pro.stringPropertyNames();
            for (String key : keys) {
                if("data_dir".equals(key)){
                    DATA_DIR = pro.getProperty(key);
                    continue;
                }
                if("result_dir".equals(key)){
                    RESULT_DIR = pro.getProperty(key);
                    continue;
                }
                if("recursive".equals(key)){
                    String value = pro.getProperty(key);
                    RECURSIVE = "true".equalsIgnoreCase(value);
                    continue;
                }
                if("buffer_size".equals(key)){
                    String value = pro.getProperty(key);
                    if(StringUtil.isNotEmpty(value)) {
                        BUFFER_SIZE = Integer.parseInt(value);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("读取配置文件 config.properties 异常", e);
        }
    }
}
