package com.learning.vps.bean.sms.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 昂网短信发送响应
 *
 * @author Wang Xu
 * @date 2020/11/2
 */
@Data
public class AwRespDTO {

    private String result;

    private String description;

    @JSONField(name = "taskid")
    private String taskId;

}