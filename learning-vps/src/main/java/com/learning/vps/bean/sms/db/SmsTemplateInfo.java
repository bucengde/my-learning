package com.learning.vps.bean.sms.db;

import lombok.Data;

import java.util.Date;

/**
 * @author Wang Xu
 * @date 2020/11/10
 */
@Data
public class SmsTemplateInfo {
    private Integer id;

    private String templateCode;

    private String content;

    private String remark;

    private Boolean deleted;

    private Date created;

    private Date modified;

}