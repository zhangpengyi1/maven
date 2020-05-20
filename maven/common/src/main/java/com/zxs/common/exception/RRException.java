package com.zxs.common.exception;

import org.apache.http.HttpStatus;

import lombok.Data;

/**
 * 自定义异常
 *
 * @author dxyun.com
 * @date 2018-05-11
 */
@Data
public class RRException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;
    private int code = HttpStatus.SC_INTERNAL_SERVER_ERROR;

    public RRException(String message) {
        super(message);
        this.message = message;
    }

    public RRException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public RRException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public RRException(String message, int code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }


}
