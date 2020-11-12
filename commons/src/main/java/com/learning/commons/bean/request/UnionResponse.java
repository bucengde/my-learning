package com.learning.commons.bean.request;

import com.learning.commons.bean.nil.NilObject;

import java.io.Serializable;

/**
 * @author Wang Xu
 * @date 2020/10/21
 */
public class UnionResponse<T> implements BaseResponse, Serializable {

    private static final long serialVersionUID = 2423222182285147063L;

    private int status;
    private String msg;
    private T data;

    public UnionResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 判断请求是否成功
     */
    public boolean isSuccess() {
        return ResponseStatusEnum.isSuccess(this.status);
    }

    /**
     * 返回成功请求
     * @param data 成功响应的实际数据
     * @return com.learning.commons.bean.request.UnionResponse
     */
    public static <T> UnionResponse<T> success(T data) {
        return new UnionResponse<T>(ResponseStatusEnum.SUCCESS.status, ResponseStatusEnum.SUCCESS.desc, data);
    }

    /**
     * 返回失败请求
     * @param msg 失败响应的message
     * @return com.learning.commons.bean.request.UnionResponse
     */
    public static UnionResponse<NilObject> fail(String msg) {
        return new UnionResponse<NilObject>(ResponseStatusEnum.FAIL.status, msg, NilObject.getInstance());
    }


    enum ResponseStatusEnum {
        SUCCESS(0, "请求成功"),
        FAIL(1, "请求失败"),
        TIMOUT(2, "请求超时"),
        FORBIDDEN(3, "禁止访问"),

        ;

        ResponseStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        /** 状态值 */
        private int status;
        /** 状态值对应描述 */
        private String desc;

        static boolean isSuccess(int status) {
            return SUCCESS.status == status;
        }
    }
}