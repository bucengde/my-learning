package com.learning.vps.handler.media.log;

import com.alibaba.fastjson.JSONObject;
import com.learning.commons.bean.api.SmsRequestParam;
import com.learning.commons.exception.RuntimeServerException;
import com.learning.commons.utils.BeanUtils;
import com.learning.vps.bean.log.MediaLog;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Wang Xu
 * @date 2020/11/11
 */
@Slf4j
public class MediaLogHandler {
    public final static ThreadLocal<MediaLog> MEDIA_LOG_THREAD_LOCAL = new ThreadLocal<>();

    public static void init(SmsRequestParam param, String sign) {
        MediaLog mediaLog = MEDIA_LOG_THREAD_LOCAL.get();
        if (Objects.nonNull(mediaLog)) {
            MEDIA_LOG_THREAD_LOCAL.remove();
        }
        mediaLog = BeanUtils.DozerBeanMapperUtil.map(param, MediaLog.class);
        if (!Optional.ofNullable(param).isPresent() || !Optional.ofNullable(mediaLog).isPresent()) {
            log.warn("MediaLog Object init error, param: {}, mediaLog: {}", param, mediaLog);
            MEDIA_LOG_THREAD_LOCAL.set(new MediaLog());
            return;
        }
        mediaLog.setRequestParam(JSONObject.toJSONString(param));
        mediaLog.setSignInfo(sign);
        mediaLog.setCreated(new Date());
        MEDIA_LOG_THREAD_LOCAL.set(mediaLog);
    }

    public static void fillResp2MediaLog(PartMediaLog partMediaLog) {
        MediaLog mediaLog = MEDIA_LOG_THREAD_LOCAL.get();
        if (Objects.isNull(mediaLog)) {
            log.warn("fillResp2MediaLog error, mediaLog not exist . . . ");
            return;
        }
        BeanUtils.DozerBeanMapperUtil.copy(partMediaLog, mediaLog);
        mediaLog.setRespStatus(partMediaLog.getRespStatus());
    }

    public static MediaLog getMediaLog() {
        return MEDIA_LOG_THREAD_LOCAL.get();
    }

    public static void removeMediaLog() {
        MEDIA_LOG_THREAD_LOCAL.remove();
    }

    @Data
    public static class PartMediaLog {
        /** 类型 */
        private Integer mediaType;
        /** 服务响应结果 json */
        private String serverResponse;
        /** 计数使用，如短信收费，70字符为一条短信，超过70则为多条短信 */
        private Integer feeCount;
        /** 请求状态，true - 成功，false - 失败 */
        private Boolean respStatus;
        /** 服务方标识 */
        private String serverFlag;
    }
}