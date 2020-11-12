package com.learning.vps.client;

import com.learning.vps.bean.sms.dto.NftsRespDTO;
import com.learning.vps.bean.sms.param.NftsRequestParam;
import com.learning.vps.config.SmsClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "sms-9527", url = "${sms.nfts.server.url}", contextId = "NftsSmsClient", configuration = SmsClientConfiguration.class)
public interface NftsSmsClient {

    @GetMapping
    NftsRespDTO sendSms(@SpringQueryMap(encoded = true) NftsRequestParam param);
}
