package com.learning.vps.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.vps.bean.sms.db.SmsTemplateInfo;
import com.learning.vps.mapper.SmsTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Wang Xu
 * @date 2020/11/10
 */
@Component
public class SmsTemplateInfoRepos {
    @Autowired
    private SmsTemplateMapper smsTemplateMapper;

    public SmsTemplateInfo getByTemplateCode(String templateCode) {
        QueryWrapper<SmsTemplateInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_code", templateCode);
        queryWrapper.eq("deleted", false);
        return smsTemplateMapper.selectOne(queryWrapper);
    }
}