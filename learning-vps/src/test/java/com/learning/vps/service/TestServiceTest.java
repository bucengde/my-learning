package com.learning.vps.service; 

import com.alibaba.fastjson.JSONObject;
import com.learning.commons.bean.api.SmsRequestParam;
import com.learning.commons.bean.api.SmsTypeEnum;
import com.learning.commons.bean.request.UnionResponse;
import com.learning.commons.exception.RuntimeServerException;
import com.learning.commons.utils.ProtoStuffUtil;
import com.learning.vps.VpsAppStarter;
import com.learning.vps.bean.sms.db.SmsTemplateInfo;
import com.learning.vps.cache.RedisCacheServer;
import com.learning.vps.mapper.SmsTemplateMapper;
import com.learning.vps.service.media.sms.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** 
*
* @author Wang Xu 
* @since <pre>10/20/2020</pre> 
* @version 1.0 
*/
@Slf4j
@SpringBootTest(classes={VpsAppStarter.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class TestServiceTest {

    @Resource
    private RedisCacheServer redisCacheServer;
    @Resource
    private SmsService smsService;
    @Resource
    private SmsTemplateMapper smsTemplateMapper;

    @Test
    public void testSendSms() {
        SmsRequestParam smsRequestParam = new SmsRequestParam();
        Map<String, String> map = new HashMap<>();
        map.put("content", "1234567");
        smsRequestParam.setContent(JSONObject.toJSONString(map));
        smsRequestParam.setMobile("17521014443");
        smsRequestParam.setTemplateCode("DSM20201020000001");
        smsRequestParam.setInternalSerialNumber(UUID.randomUUID() + StringUtils.EMPTY);
        smsRequestParam.setCreator("PALETTE");
        smsRequestParam.setDeviceIp("XXXCCCCVVVV");
        smsRequestParam.setSystemName("LEARNING");
        smsRequestParam.setPlaintext(true);
//        smsRequestParam.setSmsTypeEnum(SmsTypeEnum.AW);
        smsRequestParam.setSmsTypeEnum(SmsTypeEnum.NFTS);


        UnionResponse<?> unionResponse = smsService.sendSms(smsRequestParam, null);
        log.info(unionResponse.toString());
    }

    @Test
    public void testRedisConnection() {
        RedisCacheServer.RedisStringCacheServer stringCacheServer = redisCacheServer.getRedisStringCacheServer();
//        stringCacheServer.stringSetString("test-redis", "this is a test about redis connection.");

//        RuntimeServerException e = new RuntimeServerException("aaa","bbb");
//        byte[] serialize = ProtoStuffUtil.serialize(e);
//        stringCacheServer.stringSetString("aaa-bbb", serialize);
//        Object s = stringCacheServer.stringGetStringByKey("aaa-bbb");
//        RuntimeServerException ee = ProtoStuffUtil.deserialize((byte[])s, RuntimeServerException.class);
//        log.error(ee.getCode() + ee.getMsg());

//        stringCacheServer.stringSetValueAndExpireTime("expire_test", "77777", 1000 * 60);

        redisCacheServer.transferDb(8);
        stringCacheServer.stringSetString("transfer_db_test3", "77777777777777777");


    }

    @Test
    public void testVsCodeDubuger() {
        SmsTemplateInfo templateInfo = smsTemplateMapper.selectById(1);
        System.out.println(templateInfo.toString());

    }


}
