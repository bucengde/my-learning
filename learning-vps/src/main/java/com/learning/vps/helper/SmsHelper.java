package com.learning.vps.helper;

import com.learning.commons.bean.api.SmsRequestParam;
import com.learning.commons.bean.api.SmsTypeEnum;
import com.learning.vps.bean.sms.SmsAccountData;
import com.learning.vps.handler.media.sms.SmsHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Wang Xu
 * @date 2020/11/4
 */
@Service
public class SmsHelper {

    public static final ConcurrentHashMap<SmsTypeEnum, SmsAccountData> ACC_INFO = new ConcurrentHashMap<>();

    public SmsHandler getHandler(SmsRequestParam param) {
        param.setSmsTypeEnum(Optional.ofNullable(param.getSmsTypeEnum()).orElse(SmsTypeEnum.AW));
        return Optional.ofNullable(SmsHandler.MAP.get(param.getSmsTypeEnum())).orElse(null);
    }
}