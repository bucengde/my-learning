package com.learning.vps.listener;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.commons.bean.api.SmsTypeEnum;
import com.learning.commons.bean.api.config.ConfigInfo;
import com.learning.commons.exception.RuntimeServerException;
import com.learning.vps.bean.sms.SmsAccountData;
import com.learning.vps.helper.SmsHelper;
import com.learning.vps.mapper.ConfigInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Wang Xu
 * @date 2020/11/4
 */
@Component
public class InitOpListener implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static ApplicationContext context;

    public static void setAppContext(ApplicationContext appContext) {
        if (Objects.nonNull(context)) {
            return;
        }
        context = appContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("begin my init option . . . ");
        ConfigInfoMapper configInfoMapper = (ConfigInfoMapper)context.getBean("configInfoMapper");

        // 昂网账户信息配置
        QueryWrapper<ConfigInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_key", "AW_SMS_INFO");
        queryWrapper.eq("deleted", false);
        ConfigInfo configInfo = configInfoMapper.selectOne(queryWrapper);
        Optional.ofNullable(configInfo).orElseThrow(() -> new RuntimeServerException("####### AW_SMS_INFO 配置获取失败"));
        SmsAccountData smsAccountData = JSONObject.parseObject(configInfo.getConfigValue(), SmsAccountData.class);
        SmsHelper.ACC_INFO.put(SmsTypeEnum.AW, smsAccountData);

        // 9527账户信息配置
        QueryWrapper<ConfigInfo> queryWrapperBy9527 = new QueryWrapper<>();
        queryWrapperBy9527.eq("config_key", "9527_SMS_INFO");
        queryWrapperBy9527.eq("deleted", false);
        ConfigInfo configInfoBy9527 = configInfoMapper.selectOne(queryWrapperBy9527);
        Optional.ofNullable(configInfoBy9527).orElseThrow(() -> new RuntimeServerException("####### 9527_SMS_INFO 配置获取失败"));
        SmsAccountData smsAccountDataBy9527 = JSONObject.parseObject(configInfoBy9527.getConfigValue(), SmsAccountData.class);
        SmsHelper.ACC_INFO.put(SmsTypeEnum.NFTS, smsAccountDataBy9527);
    }

}