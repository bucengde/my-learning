package com.learning.vps.handler.media.sms;

import com.alibaba.fastjson.JSONObject;
import com.learning.commons.bean.api.SmsTypeEnum;
import com.learning.commons.bean.request.UnionResponse;
import com.learning.commons.exception.RuntimeServerException;
import com.learning.vps.bean.sms.dto.AwRespDTO;
import com.learning.vps.bean.sms.param.AwRequestParam;
import com.learning.vps.bean.sms.param.BaseRequestParam;
import com.learning.vps.client.AwSmsClient;
import com.learning.vps.eunm.ServerFlagEnum;
import com.learning.vps.handler.media.log.MediaLogHandler;
import com.learning.vps.helper.SmsHelper;
import com.learning.vps.utils.MediaUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 昂网通信发送短信服务处理接口
 * @author Wang Xu
 * @date 2020/7/13
 */
@Slf4j
@Component
public class AwSmsHttpHandler extends AbstractSmsHttpHandler<UnionResponse<String>> implements BeanPostProcessor {
    private final AwSmsClient awSmsClient;

    AwSmsHttpHandler(AwSmsClient awSmsClient) {
        this.awSmsClient = awSmsClient;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 添加映射
        MAP.put(SmsTypeEnum.AW, this);
        // 设置账户信息
        setSmsAccountData(SmsHelper.ACC_INFO.get(SmsTypeEnum.AW));
        return bean;
    }

    @Getter
    @AllArgsConstructor
    enum RespResultCodeEnum {
        /** 发送短信成功 */
        RESP_RESULT_CODE_SUCCESS("0", "发送短信成功"),
        /** 提交参数不能为空 */
        RESP_RESULT_CODE_1("1", "提交参数不能为空"),
        /** 账号无效或未开户 */
        RESP_RESULT_CODE_2("2", "账号无效或未开户"),
        /** 账号密码错误 */
        RESP_RESULT_CODE_3("3", "账号密码错误"),
        /** 预约发送时间无效 */
        RESP_RESULT_CODE_4("4", "预约发送时间无效"),
        /** IP不合法 */
        RESP_RESULT_CODE_5("5", "IP不合法"),
        /** 号码中含有无效号码或不在规定的号段 */
        RESP_RESULT_CODE_6("6", "号码中含有无效号码或不在规定的号段"),
        /** 内容中含有非法关键字 */
        RESP_RESULT_CODE_7("7", "内容中含有非法关键字"),
        /** 内容长度超过上限，最大4000 */
        RESP_RESULT_CODE_8("8", "内容长度超过上限，最大4000"),
        /** 接受号码过多，最大5000 */
        RESP_RESULT_CODE_9("9", "接受号码过多，最大5000"),
        /** 黑名单用户 */
        RESP_RESULT_CODE_10("10", "黑名单用户"),
        /** 提交速度太快 */
        RESP_RESULT_CODE_11("11", "提交速度太快"),
        /** 您尚未订购[普通短信业务]，暂不能发送该类信息 */
        RESP_RESULT_CODE_12("12", "您尚未订购[普通短信业务]，暂不能发送该类信息"),
        /** 您的[普通短信业务]剩余数量发送不足，暂不能发送该类信息 */
        RESP_RESULT_CODE_13("13", "您的[普通短信业务]剩余数量发送不足，暂不能发送该类信息"),
        /** 流水号格式不正确 */
        RESP_RESULT_CODE_14("14", "流水号格式不正确"),
        /** 流水号重复 */
        RESP_RESULT_CODE_15("15", "流水号重复"),
        /** 超出发送上限（操作员帐户当日发送上限） */
        RESP_RESULT_CODE_16("16", "超出发送上限（操作员帐户当日发送上限）"),
        /** 余额不足 */
        RESP_RESULT_CODE_17("17", "余额不足"),
        /** 扣费不成功 */
        RESP_RESULT_CODE_18("18", "扣费不成功"),
        /** 系统错误 */
        RESP_RESULT_CODE_19("20", "系统错误"),
        /** 您只能发送联通的手机号码，本次发送的手机号码中包含了非联通的手机号码 */
        RESP_RESULT_CODE_20("21", "您只能发送联通的手机号码，本次发送的手机号码中包含了非联通的手机号码"),
        /** 您只能发送移动的手机号码，本次发送的手机号码中包含了非移动的手机号码 */
        RESP_RESULT_CODE_21("22", "您只能发送移动的手机号码，本次发送的手机号码中包含了非移动的手机号码"),
        /** 您只能发送电信的手机号码，本次发送的手机号码中包含了非电信的手机号码 */
        RESP_RESULT_CODE_23("23", "您只能发送电信的手机号码，本次发送的手机号码中包含了非电信的手机号码"),
        /** 账户状态不正常 */
        RESP_RESULT_CODE_24("24", "账户状态不正常"),
        /** 账户权限不足 */
        RESP_RESULT_CODE_25("25", "账户权限不足"),
        /** 需要人工审核 */
        RESP_RESULT_CODE_26("26", "需要人工审核"),
        /** 发送内容与模板不符 */
        RESP_RESULT_CODE_28("28", "发送内容与模板不符"),
        ;

        /** 昂网响应code */
        private final String code;
        /** 响应描述 */
        private final String desc;

        private static boolean success(String result) {
            return StringUtils.isNotBlank(result) &&
                    RespResultCodeEnum.RESP_RESULT_CODE_SUCCESS.code.equals(result);
        }

    }

    @Override
    public BaseRequestParam generateRequestParams(String content, String mobiles, String... other) {
        return new AwRequestParam()
                .setLoginName(SMS_ACCOUNT_DATA.getAccount())
                .setPassword(SMS_ACCOUNT_DATA.getPassword())
                .setSpCode(SMS_ACCOUNT_DATA.getFlagId())
                .setMessageContent(content)
                .setUserNumber(mobiles);
    }

    @Override
    public UnionResponse<String> doSendSms(String content, String mobiles, String... other) throws RuntimeServerException {
        return doSendSms(content,
                mobiles,
                MediaLogHandler::fillResp2MediaLog,
                other);
    }

    @Override
    protected UnionResponse<String> doSendSms(String content, String mobiles, Consumer<MediaLogHandler.PartMediaLog> recordLogConsumer, String... other) throws RuntimeServerException {
        AwRequestParam awRequestParam = (AwRequestParam)generateRequestParams(content, mobiles);
        log.info("开始调用昂网通信服务发送短信, awRequestParam为：{}", awRequestParam.toString());
        try {
            String resp = getSendFunction().apply(awRequestParam);
            log.info("调用昂网通信服务发送短信结束, response 为：{}", resp);
            AwRespDTO awRespDTO = JSONObject.parseObject(resp, AwRespDTO.class);
            // 填充日志信息
            recordLogConsumer.accept(genePartMediaLog(awRequestParam.getMessageContent(), resp, RespResultCodeEnum.success(awRespDTO.getResult()), ServerFlagEnum.SMS_AW.getCode()));
            if (RespResultCodeEnum.success(awRespDTO.getResult())) {
                return UnionResponse.success(awRespDTO.getDescription());
            }
        } catch (Exception e) {
            log.info("短信请求失败，msg：{}", e.toString(), e);
            recordLogConsumer.accept(genePartMediaLog(awRequestParam.getMessageContent(), null, false, ServerFlagEnum.SMS_AW.getCode()));
            throw new RuntimeServerException("短信请求失败，请稍后再试！");
        }
        throw new RuntimeServerException("短信发送失败...");
    }

    private Function<AwRequestParam, String> getSendFunction() {
        return awSmsClient::sendSms;
    }

}