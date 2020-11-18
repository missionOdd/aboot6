/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.exception.handler;


import com.wteam.domain.vo.R;
import com.wteam.exception.BadRequestException;
import com.wteam.exception.CustomException;
import com.wteam.exception.EntityExistException;
import com.wteam.exception.EntityNotFoundException;
import com.wteam.utils.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

/**
 * 全局异常处理
 * @author mission
 * @since 2019/07/07 15:30
 */
@Slf4j
@SuppressWarnings({"unchecked","rawtypes"})
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<R> handleException(Throwable e) {
        //打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        R apiError=new R(BAD_REQUEST.value(), String.format("系统错误,请稍后再试 {%s}", e.getMessage()));
        return buildResponseEntity(apiError);
    }

    /**
     * 处理账号密码错误
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<R> handleBadCredentialsException(AuthenticationException e) {
        String message = "坏的凭证".equals(e.getMessage()) ? "用户名或密码不正确" : e.getMessage();
        //打印堆栈信息
        log.error(message);
        if (message.contains("could not extract ResultSet")) {
            message=String.format("可能包含特殊符号，请修改重试 {%s}", e.getMessage());
        }
        R apiError=new R(BAD_REQUEST.value(),message);
        return buildResponseEntity(apiError);
    }

    /**
     * 处理账号密码错误
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler({JpaSystemException.class,SQLException.class, JDBCException.class})
    public ResponseEntity<R> handleJpaSystemException(Throwable e) {
        String message = ThrowableUtil.getStackTrace(e);
        //打印堆栈信息
        log.error(message);
        if (message.contains("Incorrect string value")||message.contains("utf8")) {
            message=String.format("可能包含特殊符号，请修改重试 {%s}", e.getMessage());
        }
        R apiError=new R(BAD_REQUEST.value(),message);
        return buildResponseEntity(apiError);
    }

    /**
     * 处理 接口无权访问异常AccessDeniedException
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<R> handleAccessDeniedException(AccessDeniedException e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackPanic(e));
        R apiError = new R(FORBIDDEN.value(),"无权限操作");
        return buildResponseEntity(apiError);
    }

    /**
     * 处理自定义异常
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<R> badRequestException(BadRequestException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackPanic(e));
        R apiError = new R(e.getStatus(),e.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * 处理 EntityExist
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(value = EntityExistException.class)
    public ResponseEntity<R> entityExistException(EntityExistException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackPanic(e));
        R apiError = new R(BAD_REQUEST.value(),e.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * 处理 EntityNotFound
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<R> entityNotFoundException(EntityNotFoundException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackPanic(e));
        R apiError = new R(NOT_FOUND.value(),e.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * 处理 CustomException
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<R> handleCustomException(CustomException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackPanic(e));
        R apiError = new R(e.getStatus(),e.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * 处理所有接口数据验证异常
     * @param e /
     * @returns ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<R> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackPanic(e));
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if(msg.equals(message)){
            message = str[1] + ":" + message;
        }
        R apiError = new R(BAD_REQUEST.value(),message);
        return buildResponseEntity(apiError);
    }
    /**
     * 处理所有接口数据验证异常
     * @param e /
     * @returns ResponseEntity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<R> handleMethodArgumentNotValidException(ConstraintViolationException e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackPanic(e));
        String message = e.getConstraintViolations().iterator().next().getMessage();
        R apiError = new R(BAD_REQUEST.value(),message);
        return buildResponseEntity(apiError);
    }

    /**
     * 处理 IllegalArgumentException
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<R> IllegalArgumentException(IllegalArgumentException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackPanic(e));
        R apiError = new R(BAD_REQUEST.value(),e.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * 处理 DuplicateKeyException
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(value = DuplicateKeyException.class)
    public ResponseEntity<R> IllegalArgumentException(DuplicateKeyException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        R apiError = new R(BAD_REQUEST.value(),"数据重复，请检查后提交");
        return buildResponseEntity(apiError);
    }

    /**
     * 处理 HttpRequestMethodNotSupportedException
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<R> handlerMethodNotSupported(Exception e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        R apiError = new R(BAD_REQUEST.value(), String.format("请求方法不支持 {%s}", e.getMessage()));
        return buildResponseEntity(apiError);
    }


    /**
     * 处理 HttpMessageNotReadableException
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<R> handlerMessageNotReadable(Exception e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        R apiError = new R(BAD_REQUEST.value(), String.format("请求数据格式有误 {%s}", e.getMessage()));
        return buildResponseEntity(apiError);
    }

    /**
     * 处理 MethodArgumentTypeMismatchException
     * @param e /
     * @return ResponseEntity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<R> handlerMethodArgumentTypeMismatch(Exception e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        R apiError = new R(BAD_REQUEST.value(), String.format("请求参数类型有误 {%s}", e.getMessage()));
        return buildResponseEntity(apiError);
    }


    /**
     * 处理 MissingServletRequestParameterException
     * @param e /
     * @return /
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<R> handlerMissingRequestParameter(Exception e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        R apiError = new R(BAD_REQUEST.value(), String.format("请求参数缺失 {%s}", e.getMessage()));
        return buildResponseEntity(apiError);
    }

    /**
     * 统一返回
     * @param apiError /
     * @return ResponseEntity
     */
    private ResponseEntity<R> buildResponseEntity(R apiError) {
        return new ResponseEntity(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }
}
