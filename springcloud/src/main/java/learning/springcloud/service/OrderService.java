package learning.springcloud.service;

import learning.springcloud.bean.OrderInfo;
import learning.springcloud.bean.UserInfo;
import learning.springcloud.mapper.OrderInfoMapper;
import learning.springcloud.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Wang Xu
 * @date 2020/10/27
 */
@Service
public class OrderService {
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void testTransaction() {
        OrderInfo orderInfo = orderInfoMapper.selectById(7661);
        orderInfo.setBusinessFlag("AAA");
        orderInfoMapper.updateById(orderInfo);

        UserInfo userInfo = new UserInfo();
        userInfo.setAccount("aaaaaaaa");
        userInfo.setPassword("11111111");
        userInfoMapper.insert(userInfo);

    }
}