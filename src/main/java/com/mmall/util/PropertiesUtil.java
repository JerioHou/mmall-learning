package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by jerio
 */
public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;
    static {
        String filename = "mmall.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(filename),"UTF-8"));
        }catch (Exception e){
            logger.error("读取属性文件失败",e);
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            return  null;
        }
        return value.trim();
    }
    public static String getProperty(String key,String defaultValue){
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            value =  defaultValue;
        }
        return value.trim();
    }


}
