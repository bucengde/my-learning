package com.learning.job.exec;

import com.learning.job.exec.bean.db.UserInfo;
import com.learning.job.exec.mapper.UserInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Wang Xu
 * @date 2021/2/4
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JobTest {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Test
    public void testOneJob() {
        UserInfo userInfo = new UserInfo("qqqqq");
        userInfoMapper.insert(userInfo);
    }
}