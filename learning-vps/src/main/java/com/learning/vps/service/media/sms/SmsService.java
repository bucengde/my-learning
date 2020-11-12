package com.learning.vps.service.media.sms;

import com.learning.commons.bean.api.SmsRequestParam;
import com.learning.commons.bean.request.UnionResponse;
import com.learning.vps.handler.media.log.MediaLogHandler;
import com.learning.vps.helper.SmsHelper;
import com.learning.vps.manager.MediaLogManager;
import com.learning.vps.manager.SmsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Wang Xu
 * @date 2020/11/4
 */
@Slf4j
@Service
public class SmsService {
    @Resource
    private SmsManager smsManager;
    @Resource
    private SmsHelper smsHelper;
    @Resource
    private MediaLogManager mediaLogManager;

    @SuppressWarnings("unchecked")
    public UnionResponse<?> sendSms(SmsRequestParam param, String sign) {
        // TODO sign check
        // TODO 敏感信息脱敏
        MediaLogHandler.init(param, sign);
        UnionResponse<?> unionResponse = smsManager.sendSms(param, smsHelper.getHandler(param));
        // 记录操作日志信息
        mediaLogManager.recordLog();
        return unionResponse;
    }
}