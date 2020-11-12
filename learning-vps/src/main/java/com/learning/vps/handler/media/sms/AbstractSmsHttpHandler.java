package com.learning.vps.handler.media.sms;

import com.learning.commons.bean.api.SmsTypeEnum;
import com.learning.commons.bean.request.BaseResponse;
import com.learning.commons.exception.RuntimeServerException;
import com.learning.vps.bean.sms.dto.AwRespDTO;
import com.learning.vps.bean.sms.param.BaseRequestParam;
import com.learning.vps.eunm.MediaTypeEnum;
import com.learning.vps.handler.media.log.MediaLogHandler;
import com.learning.vps.utils.MediaUtils;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 第三方发送短信服务处理接口
 *
 * @author : Wang Xu
 * @date : 2020/07/13
 */
public abstract class AbstractSmsHttpHandler<T extends BaseResponse> implements SmsHandler<T> {

    /**
     * 生成请求参数对象
     */
    @Override
    public BaseRequestParam generateRequestParams(String content, String mobiles, String... other) {
        throw new UnsupportedOperationException();
    }

    protected String generateGetQueryStringParams(boolean encodingFlag, String content, String mobiles, String... other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSign(String timestamp) {
        throw new UnsupportedOperationException();
    }


    @Override
    public T doSendSms(String content, String mobiles, String... other) throws RuntimeServerException {
        throw new UnsupportedOperationException();
    }

    final protected MediaLogHandler.PartMediaLog genePartMediaLog(String content, String resp, Boolean respStatus, String serverFlag) {
        MediaLogHandler.PartMediaLog partLog = new MediaLogHandler.PartMediaLog();
        partLog.setMediaType(MediaTypeEnum.SMS.getMediaType());
        partLog.setFeeCount(MediaUtils.calculationSmsCount(content));
        partLog.setRespStatus(respStatus);
        partLog.setServerResponse(resp);
        partLog.setServerFlag(serverFlag);
        return partLog;
    }

    abstract protected T doSendSms(String content, String mobiles, Consumer<MediaLogHandler.PartMediaLog> fillResp2MediaLogConsumer, String... other) throws RuntimeServerException;
}
