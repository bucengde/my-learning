package com.learning.commons.bean.api;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author Wang Xu
 * @date 2020/11/4
 */
@Data
public class SmsRequestParam {
    /** 短信内容 */
    private String content;

    /** 手机号 */
    private String mobile;

    /** 模版code */
    private String templateCode;

    /** 请求内部流水号 */
    private String internalSerialNumber;

    /** 是否明文，true - 是，false - 否 */
    private boolean isPlaintext;

    /** 短信渠道类型 */
    private SmsTypeEnum smsTypeEnum;

    /** 业务人员名称 */
    private String creator;

    /** 调用系统名称 */
    private String systemName;

    /** 用户端设备IP */
    private String deviceIp;

    /** 定时发送，为null表示立即发送（需要短信渠道方支持方可用） */
    private String sendTime;


    public void replaceContent(String templateContent) {
        content = formatSmsContent(templateContent, JSONObject.parseObject(content));
    }


    /**
     *  格式化短信内容
     */
    private static String formatSmsContent(String templateContent, JSONObject contentObj) {
        if (Objects.isNull(contentObj)){
            return templateContent;
        }
        for (String key : contentObj.keySet()) {
            templateContent = templateContent.replaceAll("\\{#"+key+"}", contentObj.getString(key));
        }
        return templateContent;
    }
}