/*
 * copyleft © 2019-2021
 */
package com.wteam.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;

/**
 * 自定义异常
 *
 * @author mission
 * @since 2019/07/07 15:55
 */

@Getter
public class CustomException extends RuntimeException{

    private Integer status = EXPECTATION_FAILED.value();

    public CustomException(String msg) {
        super(msg);
    }

    public CustomException(HttpStatus status, String msg) {
        super(msg);
        this.status = status.value();
    }
}
