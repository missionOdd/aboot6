/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.utils;

import com.wteam.domain.ColumnInfo;
import com.wteam.domain.GenConfig;
import com.wteam.domain.GenTemplate;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.*;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * sql 字段转java
 * @author mission
 * @since 2019/07/21 8:24
 */
@SuppressWarnings({"unchecked","all"})
public class GenUtil {


    private static final String TIMESTAMP = "Timestamp";

    private static final String LOCALDATE = "LocalDate";

    private static final String LOCALDATETIME = "LocalDateTime";

    private static final String BIGDECIMAL = "BigDecimal";

    public static final String PK = "PRI";

    public static final String UNI = "UNI";

    public static final String EXTRA = "auto_increment";

    /**
     * 获取后端代码模板名称
     * @return
     */
    public static List<String> getAdminTemplateNames(List<GenTemplate> genTemplates) {
        List<String> templateNames = new ArrayList<>();
        for (GenTemplate genTemplate : genTemplates) {
            if (genTemplate.getEnabled() && genTemplate.getType().equals(0)){
                templateNames.add(genTemplate.getName());
            }
        }

        return templateNames;
    }

    /**
     * 获取前端代码模板名称
     * @return
     */
    public static List<String> getFrontTemplateNames(List<GenTemplate> genTemplates) {
        List<String> templateNames = new ArrayList<>();
        for (GenTemplate genTemplate : genTemplates) {
            if (genTemplate.getEnabled() && genTemplate.getType().equals(1)){
                templateNames.add(genTemplate.getName());
            }
        }

        return templateNames;
    }

    /**
     * 预览代码
     * @param columnInfos
     * @param genConfig
     * @return
     */
    public static List<Map<String ,Object>> preview(List<ColumnInfo> columnInfos, GenConfig genConfig){
        Map<String ,Object> genMap =getGenMap(columnInfos,genConfig);
        List<Map<String ,Object>> genList =new ArrayList<>();
        //获取后端模板
        List<String > templates =getAdminTemplateNames(genConfig.getGenTemplates());
        TemplateEngine engine =TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        for (String templateName : templates) {
            Map<String, Object> map =new HashMap<>(1);
            Template template =engine.getTemplate("generator/admin/"+templateName+".ftl");
            map.put("content",template.render(genMap));
            map.put("name",templateName);
            genList.add(map);
        }
        //获取前端模板
        templates =getFrontTemplateNames(genConfig.getGenTemplates());
        for (String templateName : templates) {
            Map<String ,Object> map =new HashMap<>(1);
            Template template = engine.getTemplate("generator/front/"+templateName+".ftl");
            map.put(templateName,template.render(genMap));
            map.put("content",template.render(genMap));
            map.put("name",templateName);
            genList.add(map);
        }
        return genList;
    }


    public static String download(List<ColumnInfo> columns, GenConfig genConfig) throws IOException {
        String tempPath =System.getProperty("java.io.tmpdir")+"aboot-gen-temp"+File.separator+genConfig.getTableName()+File.separator;
        Map<String,Object> genMap =getGenMap(columns,genConfig);
        TemplateEngine engine =TemplateUtil.createEngine(new TemplateConfig("template",TemplateConfig.ResourceMode.CLASSPATH));
        //后端生成代码
        List<String> templates =getAdminTemplateNames(genConfig.getGenTemplates());
        for (String templateName : templates) {
            Template template =engine.getTemplate("generator/admin/"+templateName+".ftl");
            String filePath = getAdminFilePath(templateName,genConfig,genMap.get("className").toString(),tempPath + "aboot" + File.separator);
            Assert.notNull(filePath,"文件路径不能为空");
            File file = new File(filePath);
            //如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                continue;
            }
            //生成代码
            genFile(file,template,genMap);
        }

        //生成前端代码
        templates =getFrontTemplateNames(genConfig.getGenTemplates());
        for (String templateName : templates) {
            Template template =engine.getTemplate("generator/front/"+templateName+".ftl");
            String path =tempPath+"aboot-web"+File.separator;
            String apiPath = path+"src"+File .separator +"api"+ File.separator;
            String srcPath =path +"src"+File.separator +"views"+File.separator+genMap.get("changeClassName").toString()+File.separator;
            genConfig.setPath(srcPath);
            genConfig.setApiPath(apiPath);
            String filePath= getFrontFilePath(templateName,genConfig,genMap.get("changeClassName").toString());
            assert filePath != null;
            File file = new File(filePath);
            //如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                continue;
            }
            //生成代码
            genFile(file,template,genMap);
        }
        return tempPath;
    }
    /**
     * 生成代码
     * @param columnInfos 表元数据
     * @param genConfig 生成代码的参数配置，如包路径，作者
     */
    public static void generatorCode(List<ColumnInfo> columnInfos, GenConfig genConfig) throws IOException {
        Map<String,Object> genMap = getGenMap(columnInfos, genConfig);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));

        // 生成后端代码
        List<String> templates = getAdminTemplateNames(genConfig.getGenTemplates());
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/admin/"+templateName+".ftl");
            String filePath = getAdminFilePath(templateName,genConfig,genMap.get("className").toString(),System.getProperty("user.dir"));

            Assert.notNull(filePath,"文件路径不能为空");
            File file = new File(filePath);

            // 如果非覆盖生成
            if(!genConfig.getCover() && FileUtil.exist(file)){
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }

        // 生成前端代码
        templates = getFrontTemplateNames(genConfig.getGenTemplates());
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/front/"+templateName+".ftl");
            String filePath = getFrontFilePath(templateName,genConfig,genMap.get("changeClassName").toString());

            Assert.notNull(filePath,"文件路径不能为空");
            File file = new File(filePath);

            // 如果非覆盖生成
            if(!genConfig.getCover() && FileUtil.exist(file)){
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
    }

    /**
     * 生成模板代码
     */
    private static Map<String,Object> getGenMap(List<ColumnInfo> columnInfos, GenConfig genConfig){
        //存储模板字段数据
        Map<String,Object> genMap =new HashMap<>(16);
        //接口别名
        genMap.put("apiAlias",genConfig.getApiPath());
        //包名
        genMap.put("package",genConfig.getPack());
        //模块名称
        genMap.put("moduleName",genConfig.getModuleName());
        //作者
        genMap.put("author",genConfig.getAuthor());
        //创建日期
        genMap.put("date", LocalDate.now().toString());
        //表名
        genMap.put("tableName",genConfig.getTableName());
        //表注备
        genMap.put("tableComment",genConfig.getTableComment());
        //转为首字母大写的驼峰命名
        String className = StringUtils.toCapitalizeCamelCase(genConfig.getTableName());
        //转为驼峰命名
        String changeClassName = StringUtils.toCamelCase(genConfig.getTableName());

        // 判断是否去除表前缀
        if (StringUtils.isNotEmpty(genConfig.getPrefix())) {
            className = StringUtils.toCapitalizeCamelCase(StrUtil.removePrefix(genConfig.getTableName(),genConfig.getPrefix()));
            changeClassName = StringUtils.toCamelCase(StrUtil.removePrefix(genConfig.getTableName(),genConfig.getPrefix()));
        }
        // 大写开头的类名
        genMap.put("className", className);
        // 全大写的类名
        genMap.put("upperCaseClassName", className.toUpperCase());
        // 保存小写开头的类名
        genMap.put("changeClassName", changeClassName);
        // 存在 Timestamp 字段
        genMap.put("hasTimestamp",false);
        // 查询类中存在 Timestamp 字段
        genMap.put("queryHasTimestamp",false);
        // 存在 BigDecimal 字段
        genMap.put("hasBigDecimal",false);
        // 查询类中存在 BigDecimal 字段
        genMap.put("queryHasBigDecimal",false);
        // 是否需要创建查询
        genMap.put("hasQuery",false);
        // 自增主键
        genMap.put("auto",false);
        // 存在字典
        genMap.put("hasDict",false);
        // 存在日期注解
        genMap.put("hasDateAnnotation",false);
        // 存在唯一
        genMap.put("hasUNI",false);
        // 保存字段信息
        List<Map<String,Object>> columns = new ArrayList<>();
        // 保存查询字段的信息
        List<Map<String,Object>> queryColumns = new ArrayList<>();
        // 存储字典信息
        List<String> dicts = new ArrayList<>();
        // 存储 between 信息
        List<Map<String,Object>> betweens = new ArrayList<>();
        // 存储不为空的字段信息
        List<Map<String,Object>> isNotNullColumns = new ArrayList<>();

        for (ColumnInfo column : columnInfos) {
            Map<String,Object> listMap = new HashMap<>(16);
            // 字段描述
            listMap.put("remark",column.getRemark());
            // 字段类型
            listMap.put("columnKey",column.getKeyType());
            // 主键类型
            String colType = ColUtil.cloToJava(column.getColumnType());
            // 小写开头的字段名
            String changeColumnName = StringUtils.toCamelCase(column.getColumnName());
            // 大写开头的字段名
            String capitalColumnName = StringUtils.toCapitalizeCamelCase(column.getColumnName());
            if(PK.equals(column.getKeyType())){
                // 存储主键类型
                genMap.put("pkColumnType",colType);
                // 存储小写开头的字段名
                genMap.put("pkChangeColName",changeColumnName);
                // 存储大写开头的字段名
                genMap.put("pkCapitalColName",capitalColumnName);
            }
            // 是否存在 Timestamp 类型的字段
            if(TIMESTAMP.equals(colType)||LOCALDATE.equals(colType)||LOCALDATETIME.equals(colType)){
                genMap.put("hasTimestamp",true);
            }
            // 是否存在 BigDecimal 类型的字段
            if(BIGDECIMAL.equals(colType)){
                genMap.put("hasBigDecimal",true);
            }
            // 主键是否自增
            if(EXTRA.equals(column.getExtra())){
                genMap.put("auto",true);
            }
            // 主键存在字典
            if(StringUtils.isNotBlank(column.getDictName())){
                genMap.put("hasDict",true);
                dicts.add(column.getDictName());
            }

            // 存储字段类型
            listMap.put("columnType",colType);
            // 存储字原始段名称
            listMap.put("columnName",column.getColumnName());
            // 不为空
            listMap.put("istNotNull",column.getNotNull());
            // 字段列表显示
            listMap.put("columnShow",column.getListShow());
            // 表单显示
            listMap.put("formShow",column.getFormShow());
            // 表单组件类型
            listMap.put("formType", StringUtils.isNotBlank(column.getFormType()) ? column.getFormType() : "Input");
            // 小写开头的字段名称
            listMap.put("changeColumnName",changeColumnName);
            //大写开头的字段名称
            listMap.put("capitalColumnName",capitalColumnName);
            // 字典名称
            listMap.put("dictName",column.getDictName());
            // 日期注解
            listMap.put("dateAnnotation",column.getDateAnnotation());
            if(StringUtils.isNotBlank(column.getDateAnnotation())){
                genMap.put("hasDateAnnotation",true);
            }
            // 添加非空字段信息
            if(column.getNotNull()){
                isNotNullColumns.add(listMap);
            }
            // 判断是否有查询，如有则把查询的字段set进columnQuery
            if(!StringUtils.isBlank(column.getQueryType())){
                // 查询类型
                listMap.put("queryType",column.getQueryType());
                // 是否存在查询
                genMap.put("hasQuery",true);
                if(TIMESTAMP.equals(colType)){
                    // 查询中存储 Timestamp 类型
                    genMap.put("queryHasTimestamp",true);
                }
                if(BIGDECIMAL.equals(colType)){
                    // 查询中存储 BigDecimal 类型
                    genMap.put("queryHasBigDecimal",true);
                }
                if("between".equalsIgnoreCase(column.getQueryType())){
                    betweens.add(listMap);
                } else {
                    // 添加到查询列表中
                    queryColumns.add(listMap);
                }
            }
            // 添加到字段列表中
            columns.add(listMap);
        }
        // 保存字段列表
        genMap.put("columns",columns);
        // 保存查询列表
        genMap.put("queryColumns",queryColumns);
        // 保存字段列表
        genMap.put("dicts",dicts);
        // 保存查询列表
        genMap.put("betweens",betweens);
        // 保存非空字段信息
        genMap.put("isNotNullColumns",isNotNullColumns);
        return genMap;
    }

    /**
     * 定义后端文件路径以及名称
     */
    private static String getAdminFilePath(String templateName, GenConfig genConfig, String className, String rootPath) {
        String projectPath = rootPath + File.separator + genConfig.getModuleName();
        String packagePath = projectPath + File.separator + "src" +File.separator+ "main" + File.separator + "java" + File.separator;
        if (!ObjectUtils.isEmpty(genConfig.getPack())) {
            packagePath += genConfig.getPack().replace(".", File.separator) + File.separator;
        }

        if ("Entity".equals(templateName)) {
            return packagePath + "domain" + File.separator + className + ".java";
        }

        if ("Controller".equals(templateName)) {
            return packagePath + "web" + File.separator + className + "Controller.java";
        }

        if ("Service".equals(templateName)) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if ("ServiceImpl".equals(templateName)) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if ("DTO".equals(templateName)) {
            return packagePath + "domain" + File.separator + "dto" + File.separator + className + "DTO.java";
        }

        if ("QueryCriteria".equals(templateName)) {
            return packagePath + "domain" + File.separator + "criteria" + File.separator + className + "QueryCriteria.java";
        }

        if ("Mapper".equals(templateName)) {
            return packagePath + "domain" + File.separator + "mapper" + File.separator + className + "Mapper.java";
        }

        if ("Repository".equals(templateName)) {
            return packagePath + "repository" + File.separator + className + "Repository.java";
        }

        return null;
    }
    /**
     * 定义前端文件路径以及名称
     */
    public static String getFrontFilePath(String templateName, GenConfig genConfig, String apiName) {
        String path = genConfig.getPath();
        String[] js = {"api","operation","property"};
        String[] vue = {"index","form"};
        String[] css = {"style"};

        if (Arrays.asList(js).contains(templateName)) {
            return path + File.separator + templateName + ".js";
        }

        if (Arrays.asList(vue).contains(templateName)) {
            return path  + File.separator + templateName + ".vue";
        }

        if (Arrays.asList(css).contains(templateName)) {
            return path  + File.separator + templateName + ".css";
        }

        return null;
    }

    public static void genFile(File file,Template template,Map<String,Object> map) throws IOException {
        // 生成目标文件
        Writer writer = null;
        try {
            FileUtil.touch(file);
            writer = new FileWriter(file);
            template.render(map, writer);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.close();
        }
    }
}
