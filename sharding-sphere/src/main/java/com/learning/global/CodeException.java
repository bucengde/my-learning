package com.learning.global;

/**
 * @author Wang Xu
 * @date 2020/10/4
 */
public class CodeException extends Exception {
    private String code;
    private String msg;

    public CodeException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public CodeException(String code, String msg, Throwable throwable) {
        super(throwable);
        this.code = code;
        this.msg = msg;
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
}