/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 文件路径工具
 * @author mission
 * @since 2019/07/07 19:47
 */
@Slf4j
@Configuration
public class PathUtil {

    //虚拟目录根路径
    private static String basePath;
    //虚拟目录静态路径
    private static String urlPrefix;
    /**
     * 获得基本路径
     */
    public static String basePath(){
        return basePath;
    }
    /**
     * 注入根路径
     * @param path
     */
    @Value("${file.base-path}")
    private void setBasePath(String path){
        if (StringUtils.isEmpty(path)){
            //使用根路径
            basePath=getProjectPath();

        }else if (!path.endsWith("/")||!path.endsWith("\\")) {
            path=path+"/";
        }
        path=path.replace("/",File.separator);
        log.info("资源存储路径---"+path);
        File tmpFile = new File(path);
        if (!tmpFile.exists()&&tmpFile.mkdirs()){
            log.info("初始创建----"+path);
        }
        basePath= path;
    }

    /**
     * 获取项目不同模式下的根路径
     */
    public static String getProjectPath(){
        String filePath = PathUtil.class.getResource("").getPath();
        String projectPath = PathUtil.class.getClassLoader().getResource("").getPath();
        StringBuilder path = new StringBuilder();

        if(!filePath.startsWith("file:/")){
            // 开发模式下根路径
            char[] filePathArray = filePath.toCharArray();
            char[] projectPathArray = projectPath.toCharArray();
            for (int i = 0; i < filePathArray.length; i++) {
                if(projectPathArray.length > i && filePathArray[i] == projectPathArray[i]){
                    path.append(filePathArray[i]);
                }else {
                    break;
                }
            }
        }else if(!projectPath.startsWith("file:/")){
            // 部署服务器模式下根路径
            projectPath = projectPath.replace("/WEB-INF/classes/", "");
            projectPath = projectPath.replace("/target/classes/", "");
            try {
                path.append(URLDecoder.decode(projectPath,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return projectPath;
            }
        }else {
            // jar包启动模式下根路径
            String property = System.getProperty("java.class.path");
            int firstIndex = property.lastIndexOf(System.getProperty("path.separator")) + 1;
            int lastIndex = property.lastIndexOf(File.separator) + 1;
            path.append(property, firstIndex, lastIndex);
        }

        File file = new File(path.toString());
        String rootPath = "/";
        try {
            rootPath = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rootPath.replaceAll("\\\\","/");
    }


    /**
     * url路径
     */
    public static String fileUrlPrefix() {
        return urlPrefix;
    }
    @Value("${file.url-prefix}")
    public void setUrlPrefix(String urlPrefix) {
        PathUtil.urlPrefix = urlPrefix;
    }

    /**
     * 获得文件相对路径
     */
    public static String toFilepath(String path){
        return path.replace("/",File.separator);
    }
}
