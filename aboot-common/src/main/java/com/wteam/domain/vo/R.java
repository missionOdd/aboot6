/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wteam.utils.HttpHolder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 统一返回结果
 * @author mission
 * @since 2019/07/07 16:03
 */
@Data
@SuppressWarnings("unchecked")
@ApiModel(value = "相应体",description = "通用返回对象")
public class R<T> implements Serializable {

    /**
     * 状态码,可参考org.springframework.http.HttpStatus
     */
    @ApiModelProperty(value = "状态码,调用是否正常,非200即错误")
    private Integer status=200;

    /**
     * 状态信息
     */
    @ApiModelProperty(value = "状态信息,如果state不是200,即为错误信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @ApiModelProperty(value = "当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp=LocalDateTime.now();

    /**
     * 数据
     */
    @ApiModelProperty(value = "返回数据")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /**
     * 字典
     */
    @ApiModelProperty(value = "字典")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String,Object> dict;

    private R() {
    }

    public R(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public R(HttpStatus status) {
        this.status = status.value();
        this.message=status.getReasonPhrase();
    }

    public R(Integer status, String message, T data) {
        this.status = status;
        this.message=message;
        this.data = data;
    }


    public R(HttpStatus status, T data) {
        this.status = status.value();
        this.message=status.getReasonPhrase();
        this.data = data;
    }

    /**
     * 请求成功
     */
    public static<T> R<T> ok(){
        return new R<T>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 请求成功
     */
    public static<T> R<T> ok(T data){
        return new R<>(HttpStatus.OK,data);
    }

    /**
     * 请求成功
     */
    public static<T> R<T> ok(String message, T data){
        return new R<>(HttpStatus.OK.value(),message,data);
    }
    /**
     * 请求成功
     */
    public static<T> R<T> okMsg(String message){
        return new R<>(HttpStatus.OK.value(),message);
    }




    public static<T> R<T> error(String message){
        HttpHolder.getHttpServletResponse().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new R(HttpStatus.INTERNAL_SERVER_ERROR.value(),message);
    }


    /**
     * 自定义返回
     */
    public static<T> R<T> custom(Integer status, String msg){
        HttpHolder.getHttpServletResponse().setStatus(status);
        return new R<>(status,msg);
    }

    /**
     * 自定义返回
     */
    public static<T> R<T> custom(Integer status, String msg, T data){
        HttpHolder.getHttpServletResponse().setStatus(status);
        return new R<>(status,msg,data);
    }

    /**
     * 自定义返回
     */
    public static<T> R<T> custom(HttpStatus httpStatus, T data){
        HttpHolder.getHttpServletResponse().setStatus(httpStatus.value());
        return new R<>(httpStatus, data);
    }

    /**
     * 缺少必要的请求
     */
    public static<T> R<T> bad(){
        HttpHolder.getHttpServletResponse().setStatus(HttpStatus.BAD_REQUEST.value());
        return new R<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 验证不通过
     */
    public static<T> R<T> unAuthorized(){
        HttpHolder.getHttpServletResponse().setStatus(HttpStatus.UNAUTHORIZED.value());
        return new R<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * 空资源
     */
    public static<T> R<T> empty(){
        HttpHolder.getHttpServletResponse().setStatus(HttpStatus.NO_CONTENT.value());
        return new R<>(HttpStatus.NO_CONTENT);
    }

}
