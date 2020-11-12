package com.learning.vps.bean.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第三方短信服务账户数据
 * @author Wang Xu
 * @date 2020/7/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsAccountData {
    private String account;
    private String password;
    private String url;
    private String flagId;
}