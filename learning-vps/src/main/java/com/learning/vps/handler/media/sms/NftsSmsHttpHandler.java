package com.learning.vps.handler.media.sms;

import com.alibaba.fastjson.JSONObject;
import com.learning.commons.bean.api.SmsTypeEnum;
import com.learning.commons.bean.request.UnionResponse;
import com.learning.commons.exception.RuntimeServerException;
import com.learning.vps.bean.sms.dto.NftsRespDTO;
import com.learning.vps.bean.sms.param.BaseRequestParam;
import com.learning.vps.bean.sms.param.NftsRequestParam;
import com.learning.vps.client.NftsSmsClient;
import com.learning.vps.eunm.ServerFlagEnum;
import com.learning.vps.handler.media.log.MediaLogHandler;
import com.learning.vps.helper.SmsHelper;
import com.learning.vps.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 9527短信渠道服务
 * @author Wang Xu
 * @date 2020/11/11
 */
@Slf4j
@Component
public class NftsSmsHttpHandler extends AbstractSmsHttpHandler<UnionResponse<String>> implements BeanPostProcessor {
    private final NftsSmsClient nftsSmsClient;

    NftsSmsHttpHandler(NftsSmsClient nftsSmsClient) {
        this.nftsSmsClient = nftsSmsClient;
    }

    @Override
    public String getSign(String timestamp) {
        String content = SMS_ACCOUNT_DATA.getAccount() + SMS_ACCOUNT_DATA.getPassword() + timestamp;
        return MD5Util.getLowerCaseMD5Encode(content, "UTF-8");
    }

    @Override
    public BaseRequestParam generateRequestParams(String content, String mobile, String... other) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return new NftsRequestParam()
                .setUserid(SMS_ACCOUNT_DATA.getFlagId())
                .setTimestamp(date)
                .setSign(getSign(date))
                .setMobile(mobile)
                .setContent(content);
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
        NftsRequestParam param = (NftsRequestParam)generateRequestParams(content, mobiles);
        log.info("开始调用9527通信服务发送短信, param：{}", param.toString());
        try {
            NftsRespDTO resp = getSendFunction().apply(param);
            log.info("调用9527通信服务发送短信结束, response 为：{}", resp.toString());
            // 填充日志信息
            recordLogConsumer.accept(genePartMediaLog(param.getContent(), JSONObject.toJSONString(resp), resp.isSuccess(), ServerFlagEnum.SMS_9527.getCode()));
            if (resp.isSuccess()) {
                return UnionResponse.success(resp.getMessage());
            }
        } catch (Exception e) {
            log.info("短信请求失败，msg：{}", e.toString(), e);
            recordLogConsumer.accept(genePartMediaLog(param.getContent(), null, false, ServerFlagEnum.SMS_9527.getCode()));
            throw new RuntimeServerException("短信请求失败，请稍后再试！");
        }
        throw new RuntimeServerException("短信发送失败...");
    }

    private Function<NftsRequestParam, NftsRespDTO> getSendFunction() {
        return nftsSmsClient::sendSms;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 添加映射
        MAP.put(SmsTypeEnum.NFTS, this);
        // 设置账户信息
        setSmsAccountData(SmsHelper.ACC_INFO.get(SmsTypeEnum.NFTS));
        return bean;
    }
}