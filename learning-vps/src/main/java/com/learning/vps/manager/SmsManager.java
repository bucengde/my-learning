package com.learning.vps.manager;

import com.learning.commons.bean.api.SmsRequestParam;
import com.learning.commons.bean.request.UnionResponse;
import com.learning.vps.bean.sms.db.SmsTemplateInfo;
import com.learning.vps.dao.SmsTemplateInfoRepos;
import com.learning.vps.handler.media.sms.SmsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Wang Xu
 * @date 2020/11/2
 */
@Slf4j
@Service
public class SmsManager {
    private final static String ERROR_TIP = "短信发送失败，请稍后再试 . . .";

    private final SmsTemplateInfoRepos smsTemplateInfoRepos;

    public SmsManager(SmsTemplateInfoRepos smsTemplateInfoRepos) {
        this.smsTemplateInfoRepos = smsTemplateInfoRepos;
    }

    public UnionResponse<?> sendSms(SmsRequestParam param, SmsHandler<UnionResponse<String>> smsHandler) {
        String templateCode = param.getTemplateCode();
        // 查询模版模版信息
        SmsTemplateInfo smsTemplateInfo = smsTemplateInfoRepos.getByTemplateCode(templateCode);
        if (Objects.isNull(smsTemplateInfo)) {
            log.info("内部流水号为：{}，短信发送失败，短信模板 templateCode: {} 未配置", param.getInternalSerialNumber(), templateCode);
            return UnionResponse.fail(ERROR_TIP);
        }
        // 发送短信
        try {
            param.replaceContent(smsTemplateInfo.getContent());
            return smsHandler.doSendSms(param.getContent(), param.getMobile());
        } catch (Exception e) {
            log.error("内部流水号为：{}，短信发送失败，异常信息为：{}", param.getInternalSerialNumber(),  e.toString(), e);
        }
        return UnionResponse.fail(ERROR_TIP);
    }
}