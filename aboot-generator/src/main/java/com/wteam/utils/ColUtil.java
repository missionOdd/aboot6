/*
 * copyleft © 2019-2021
 */

package com.wteam.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 代码生成
 * @author mission
 * @since 2019/07/21 8:24
 */
@Slf4j
public class ColUtil {


    /**
     * 转换mysql数据类型为java数据类型
     * @param type
     * @return
     */
    public static String cloToJava(String type){
        Configuration config = getConfig();
        return config.getString(type,"unknowType");
    }

    /**
     * 获取配置信息
     * @return
     */
    public static PropertiesConfiguration getConfig(){
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
