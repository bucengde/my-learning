package com.learning.vps.controller.media;

import com.learning.commons.bean.api.SmsRequestParam;
import com.learning.commons.bean.request.UnionResponse;
import com.learning.vps.service.media.sms.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 短信服务
 *
 * @author Wang Xu
 * @date 2020/10/26
 */
@Slf4j
@RestController
@RequestMapping("/media/sms")
public class SmsController {
    @Resource
    private SmsService smsService;

    @RequestMapping("/send")
    public UnionResponse<?> sendSms(@RequestBody SmsRequestParam param, @RequestHeader String sign) {
        log.info("/media/sms/send param:{}, sign:{}", param, sign);
        return smsService.sendSms(param, sign);
    }

}