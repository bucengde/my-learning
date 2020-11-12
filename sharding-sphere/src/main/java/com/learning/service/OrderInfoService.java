package com.learning.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.learning.es.bean.db.OrderInfo;
import com.learning.es.bean.db.TOrder;
import com.learning.bean.dto.OrderInfoDTO;
import com.learning.global.CodeException;
import com.learning.manager.OrderInfoManager;
import com.learning.utils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Wang Xu
 * @date 2020/10/4
 */
@Service
public class OrderInfoService {

    @Resource
    private OrderInfoManager orderInfoManager;

    public List<OrderInfoDTO> getTopTen() {
        return orderInfoManager.getTopTen();
    }

    public void simpleShardingInsert(OrderInfoDTO orderInfoDTO) {
        try {
            OrderInfo orderInfo = BeanUtils.CglibBeanCopyUtil.map(orderInfoDTO, OrderInfo.class);
            orderInfoManager.simpleShardingInsert(orderInfo);
        } catch (CodeException e) {
            e.printStackTrace();
        }
    }

    public void shardingInsert(TOrder order) {
        orderInfoManager.insert(order);
    }

    public TOrder shardingSelect(int orderId) {
        return orderInfoManager.shardingSelect(orderId);
    }

    public IPage<TOrder> shardingSelectByLimit(int limit) {
        return orderInfoManager.shardingSelectByLimit(limit);
    }
}