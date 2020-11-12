package com.learning.vps.dao;

import com.learning.vps.bean.log.MediaLog;
import com.learning.vps.mapper.MediaLogMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Wang Xu
 * @date 2020/11/11
 */
@Component
public class MediaLogRepos {
    @Resource
    private MediaLogMapper mediaLogMapper;

    public boolean insertMediaLog(MediaLog mediaLog) {
        return mediaLogMapper.insert(mediaLog) > 0;
    }
}