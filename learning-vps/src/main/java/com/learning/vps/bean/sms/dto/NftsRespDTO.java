package com.learning.vps.bean.sms.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Wang Xu
 * @date 2020/11/11
 */
@Data
public class NftsRespDTO {
    /** 返回状态值：成功返回Success 失败返回：Faild */
    @JSONField(name = "ReturnStatus")
    private String returnStatus;

    /** 返回信息: ok 表示提交成功 */
    @JSONField(name = "Message")
    private String message;

    /** 余额 */
    @JSONField(name = "RemainPoint")
    private Integer remainPoint;

    /** 返回本次任务的序列ID */
    @JSONField(name = "TaskID")
    private Integer taskId;

    /** 成功短信数：当成功后返回提交成功短信数 */
    @JSONField(name = "SuccessCounts")
    private Integer successCounts;


    /**
     * 判断短信是否发送成功
     */
    public boolean isSuccess() {
        return "ok".equals(message);
    }
}