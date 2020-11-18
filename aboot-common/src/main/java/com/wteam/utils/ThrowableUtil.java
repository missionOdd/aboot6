/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.utils;

import com.wteam.exception.BadRequestException;
import org.hibernate.exception.ConstraintViolationException;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具
 *
 * @author mission
 * @since 2019/07/07 15:41
 */
public class ThrowableUtil {

    /**
     * 获取关键堆栈信息
     */
    public static String getStackPanic(Throwable throwable){
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            StackTraceElement[] stack =throwable.getStackTrace();
            if (stack.length>3) {
                throwable.setStackTrace(new StackTraceElement[]{stack[0],stack[1],stack[2]});
            }
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    /**
     * 获取堆栈信息
     */
    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    /**
     * 关联报错
     * @param e
     * @param msg
     */
    public static void throwForeignKeyException(Throwable e, String msg){
        Throwable t = e.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException)) {
            t = t.getCause();
        }
        if (t != null) {
            throw new BadRequestException(msg);
        }
        assert false;
        throw new BadRequestException("删除失败：" + t.getMessage());
    }
}
