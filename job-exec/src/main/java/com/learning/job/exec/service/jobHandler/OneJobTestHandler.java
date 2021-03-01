package com.learning.job.exec.service.jobHandler;

import com.learning.job.exec.bean.db.UserInfo;
import com.learning.job.exec.mapper.UserInfoMapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * @author Wang Xu
 * @date 2021/2/4
 */
@Component
public class OneJobTestHandler {

    @Resource
    private UserInfoMapper userInfoMapper;


    @XxlJob("testInsertDBJobHandler")
    public ReturnT<String> testInsertDBJobHandler(String param) throws Exception {
        XxlJobLogger.log("XXL-JOB, test one job about data insert to db. . .");
        for (int i = 1; i <= 5; i++) {
            UserInfo userInfo = new UserInfo(i + "--u");
            userInfoMapper.insert(userInfo);
            TimeUnit.SECONDS.sleep(2);
        }
        return SUCCESS;
    }

}