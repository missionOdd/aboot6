/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.base;

import com.wteam.utils.BeanUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * 通用数据传输对象
 * @author mission
 * @since 2019/06/14 14:16
 */
@Slf4j
@Getter
@Setter
public class BaseDTO  implements Serializable {

    private Long createdBy;

    private Long updatedBy;

    private Timestamp createdAt;

    private Timestamp updatedAt;



    /**
     * DTO转换Object
     * @param targetClass 转换目标类型
     * @param <T> 转换目标类型
     * @return
     */
    public <T> T convert(Class<T> targetClass) {
        Assert.notNull(targetClass,"targetClass 不能为空");
        try {
            T poObj = targetClass.newInstance();
            BeanUtil.copyPropertiesNotNull(this,poObj);
            return poObj;
        } catch (Exception e) {
            log.error("转换失败:"+e.getMessage(),e);
        }
        return null;
    }

    /**
     * DTO转换Object
     * @param target 转换目标,
     * @param targetClass 转换目标类型
     * @param <T> 转换目标类型
     * @return
     */
    public <T> T convert(T target,Class<T> targetClass) {
        Assert.notNull(target,"target 不能为空");
        Assert.notNull(targetClass,"targetClass 不能为空");
        BeanUtil.copyPropertiesNotNull(this,target);
        return target;
    }

    /**
     * 把object转换成DTO
     * @param source
     */
    public void convertFor(Object source) {
        Assert.notNull(source,"source 不能为空");
        BeanUtil.copyPropertiesNotCover(source,this);
    }


    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                builder.append(f.getName(), f.get(this)).append("\n");
            }
        } catch (Exception e) {
            builder.append("toString builder encounter an error");
        }
        return builder.toString();
    }
}
