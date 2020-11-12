package com.learning.vps.bean.log;

import lombok.Data;

import java.util.Date;

/**
 * @author Wang Xu
 * @date 2020/11/11
 */
@Data
public class MediaLog {
    private Long id;

    /** 签名信息 */
    private String signInfo;

    /** 请求内部流水号 */
    private String internalSerialNumber;

    /** 类型 */
    private Integer mediaType;

    /** 请求入参信息 json */
    private String requestParam;

    /** 服务响应结果 json */
    private String serverResponse;

    /** 计数使用，如短信收费，70字符为一条短信，超过70则为多条短信 */
    private Integer feeCount;

    /** 业务人员名称 */
    private String creator;

    /** 调用系统名称 */
    private String systemName;

    /** 服务方标识 */
    private String serverFlag;

    /** 用户端设备IP */
    private String deviceIp;

    /** 请求状态，true - 成功，false - 失败 */
    private Boolean respStatus;

    /** 创建时间 */
    private Date created;

}