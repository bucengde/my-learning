package com.learning.global;

/**
 * @author Wang Xu
 * @date 2020/10/4
 */
@SuppressWarnings({"unused"})
public class RespDTO<T> {
    private T data;
    private String msg;
    /** 0 - 成功，1 - 失败 */
    private int status;

    public RespDTO(T data, String msg, int status) {
        this.data = data;
        this.msg = msg;
        this.status = status;
    }

    public static <T> RespDTO<T> success(T data) {
        return new RespDTO<>(data, RespStatusEnum.SUCCESS.desc, RespStatusEnum.SUCCESS.code);
    }

    public static <T> RespDTO<T> fail(String msg) {
        return new RespDTO<>(null, msg == null || msg.length() == 0 ? RespStatusEnum.FAIL.desc : msg, RespStatusEnum.FAIL.code);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RespDTO{" +
                "data=" + data +
                ", msg='" + msg + '\'' +
                ", status=" + status +
                '}';
    }

    /**
     * 响应状态枚举类
     */
    public enum RespStatusEnum {
        /** 请求成功 */
        SUCCESS(0, "请求成功"),
        /** 请求失败 */
        FAIL(1, "请求失败"),

        ;

        RespStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /** code 值，0 - 成功，1 - 失败 */
        private final int code;
        /** 描述 */
        private final String desc;
    }
}