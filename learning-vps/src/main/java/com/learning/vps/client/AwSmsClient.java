package com.learning.vps.client;

import com.learning.vps.bean.sms.param.AwRequestParam;
import com.learning.vps.config.SmsClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "sms-aw", url = "${sms.aw.server.url}", contextId = "AwSmsClient", configuration = SmsClientConfiguration.class)
public interface AwSmsClient {

    @GetMapping
    String sendSms(@SpringQueryMap(encoded = true) AwRequestParam awRequestParam);
}
