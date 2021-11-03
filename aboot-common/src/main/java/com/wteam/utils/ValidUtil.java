/*
 * copyleft © 2019-2021
 */
package com.wteam.utils;

import cn.hutool.core.util.ObjectUtil;
import com.wteam.exception.BadRequestException;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

import java.util.Optional;

/**
 * 验证工具
 * @author Zheng Jie
 * @date 2018-11-23
 */
public class ValidUtil {


    /**
     * 验证空
     * @param obj /
     * @param entity /
     * @param parameter /
     * @param value /
     */
    public static void notNull(Object obj, String entity, String parameter, Object value){
        assert obj != null;
        if (ObjectUtil.isNull(obj)){
            String msg= entity + "不存在 "
                    +"{ \""+ parameter +"\":\""+ value.toString() +"\" }";
            throw new BadRequestException(msg);
        }else if (obj instanceof Optional){
            if (!((Optional) obj).isPresent()) {
                String msg= entity + "不存在 "
                        +"{ \""+ parameter +"\":\""+ value.toString() +"\" }";
                throw new BadRequestException(msg);
            }
        }
    }


    /**
     * 验证是否为邮箱
     * @param email /
     * @return /
     */
    public static boolean isEmail(String email){
        return new EmailValidator().isValid(email, null);
    }
}
