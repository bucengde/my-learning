package com.learning.vps.handler.media.sms;


import com.learning.commons.bean.api.SmsTypeEnum;
import com.learning.commons.bean.request.BaseResponse;
import com.learning.commons.exception.RuntimeServerException;
import com.learning.commons.utils.BeanUtils;
import com.learning.vps.bean.sms.dto.AwRespDTO;
import com.learning.vps.bean.sms.param.BaseRequestParam;
import com.learning.vps.bean.sms.SmsAccountData;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 第三方发送短信服务处理接口
 *
 * @author : Wang Xu
 * @date : 2020/07/13
 */
public interface SmsHandler<T extends BaseResponse> {
    ConcurrentHashMap<SmsTypeEnum, SmsHandler> MAP = new ConcurrentHashMap<>();
    SmsAccountData SMS_ACCOUNT_DATA = new SmsAccountData();

    /**
     * 生成请求参数
     *
     * @param content 消息模版内容
     * @param mobiles 手机号集合（使用“,”进行分割）
     * @param other 其他参数集
     * @return Map<String, String> 短信请求参数数据集
     */
    BaseRequestParam generateRequestParams(String content, String mobiles, String... other);

    /**
     * 发送短信
     *
     * @param content 消息模版内容
     * @param mobiles 手机号集合（使用“,”进行分割）
     * @param other 其他参数集
     * @return ResultDTO 短信发送结果
     * @throws RuntimeServerException 自定义异常
     */
    T doSendSms(String content, String mobiles, String... other) throws RuntimeServerException;

    /**
     * 签名获取
     *
     * @param timestamp 时间戳
     * @return String 签名
     */
    String getSign(String timestamp);

    /**
     * 设置短信渠道的账户相关信息
     *
     * @param smsAccountData 账户相关信息
     */
    default void setSmsAccountData(SmsAccountData smsAccountData) {
        BeanUtils.DozerBeanMapperUtil.copy(smsAccountData, SMS_ACCOUNT_DATA);
    }

}
