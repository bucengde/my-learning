package com.learning.vps.bean.sms.param;

/**
 * @author Wang Xu
 * @date 2020/11/11
 */
public class NftsRequestParam extends BaseRequestParam {
    private static final long serialVersionUID = 662095412572334128L;

    /** 企业ID */
    private String userid;
    /** 系统当前时间字符串，年月日时分秒，例如：20120701231212 */
    private String timestamp;
    /**
     * 使用 账号+密码+时间戳 生成MD5字符串作为签名。MD5生成32位，且需要小写
     * 例如：
     * 账号是 test
     * 密码是 mima
     * 时间戳是 20120701231212
     * 就需要用 testmima20120701231212
     * 来生成MD5的签名，生成的签名为5cc68982f55ac74348e3d819f868fbe1
     */
    private String sign;
    /** 全部被叫号码，短信发送的目的号码.多个号码之间用半角逗号隔开 */
    private String mobile;
    /** 短信的内容，内容需要UTF-8编码 */
    private String content;
    /** 定时发送时间, 为空表示立即发送，定时发送格式 yyyy-MM-dd HH:mm:ss */
    private String sendTime = "";
    /** 发送任务命令, 设置为固定的:send */
    private String action = "send";
    /** 接口类型, 固定值 json，不填则为XML格式返回 */
    private String rt = "json";
    /** 扩展子号, 请先询问配置的通道是否支持扩展子号，如果不支持，请填空。子号只能为数字，且最多10位数。 */
    private String extno = "";


    public String getUserid() {
        return userid;
    }

    public NftsRequestParam setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public NftsRequestParam setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public NftsRequestParam setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public NftsRequestParam setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NftsRequestParam setContent(String content) {
        this.content = content;
        return this;
    }

    public String getSendTime() {
        return sendTime;
    }

    public NftsRequestParam setSendTime(String sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public String getAction() {
        return action;
    }

    public NftsRequestParam setAction(String action) {
        this.action = action;
        return this;
    }

    public String getRt() {
        return rt;
    }

    public NftsRequestParam setRt(String rt) {
        this.rt = rt;
        return this;
    }

    public String getExtno() {
        return extno;
    }

    public NftsRequestParam setExtno(String extno) {
        this.extno = extno;
        return this;
    }
}