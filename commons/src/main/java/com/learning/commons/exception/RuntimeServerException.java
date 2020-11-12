package com.learning.commons.exception;

/**
 * @author Wang Xu
 * @date 2020/10/20
 */
public class RuntimeServerException extends RuntimeException {
    private static final String CODE = "SERVER_ERROR";
    private static final String MSG = "服务繁忙!";

    private String code;
    private String msg;
    private Throwable throwable;

    public RuntimeServerException() {
        super();
        this.code = CODE;
        this.msg = MSG;
    }

    public RuntimeServerException(String msg) {
        super(msg);
        this.code = CODE;
        this.msg = msg;
    }

    public RuntimeServerException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public RuntimeServerException(String msg, Throwable e) {
        super(e);
        this.code = CODE;
        this.msg = msg;
        this.throwable = e;
    }

    public RuntimeServerException(Throwable e) {
        super(e);
        this.code = CODE;
        this.msg = MSG;
        this.throwable = e;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}