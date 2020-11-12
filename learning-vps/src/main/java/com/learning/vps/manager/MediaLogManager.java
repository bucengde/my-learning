package com.learning.vps.manager;

import com.learning.vps.bean.log.MediaLog;
import com.learning.vps.dao.MediaLogRepos;
import com.learning.vps.handler.media.log.MediaLogHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Wang Xu
 * @date 2020/11/11
 */
@Slf4j
@Service
public class MediaLogManager {
    @Resource
    private MediaLogRepos mediaLogRepos;

    public void recordLog() {
        try {
            MediaLog mediaLog = MediaLogHandler.getMediaLog();
            if (Objects.isNull(mediaLog)) {
                log.warn("recordLog error, not found media log . . .");
                return;
            }
            if (!mediaLogRepos.insertMediaLog(mediaLog)) {
                log.warn("recordLog failed, mediaLog: {}", mediaLog);
            }
        } catch (Exception e) {
            log.warn("recordLog error, msg: {}", e.toString(), e);
        } finally {
            MediaLogHandler.removeMediaLog();
        }
    }
}