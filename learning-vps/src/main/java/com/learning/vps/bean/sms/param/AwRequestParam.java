package com.learning.vps.bean.sms.param;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 昂网请求入参对象
 * @author Wang Xu
 * @date 2020/10/27
 */
@AllArgsConstructor
@NoArgsConstructor
public class AwRequestParam extends BaseRequestParam {
    private static final long serialVersionUID = 9036063899362606216L;

//    @JSONField(name = "SpCode")
    private String SpCode;
//    @JSONField(name = "LoginName")
    private String LoginName;
//    @JSONField(name = "Password")
    private String Password;
//    @JSONField(name = "MessageContent")
    private String MessageContent;
//    @JSONField(name = "UserNumber")
    private String UserNumber;
    @JSONField(name = "ScheduleTime", format = "yyyyMMddhhmmss")
    private String ScheduleTime;

    public String getSpCode() {
        return SpCode;
    }

    public AwRequestParam setSpCode(String spCode) {
        this.SpCode = spCode;
        return this;
    }

    public String getLoginName() {
        return LoginName;
    }

    public AwRequestParam setLoginName(String loginName) {
        this.LoginName = loginName;
        return this;
    }

    public String getPassword() {
        return Password;
    }

    public AwRequestParam setPassword(String password) {
        this.Password = password;
        return this;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public AwRequestParam setMessageContent(String messageContent) {
        this.MessageContent = messageContent;
        return this;
    }

    public String getUserNumber() {
        return UserNumber;
    }

    public AwRequestParam setUserNumber(String userNumber) {
        this.UserNumber = userNumber;
        return this;
    }

    public String getScheduleTime() {
        return ScheduleTime;
    }

    public AwRequestParam setScheduleTime(String scheduleTime) {
        this.ScheduleTime = scheduleTime;
        return this;
    }
}