package com.learning.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learning.es.bean.db.OrderInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author Wang Xu
 * @date 2020/10/19
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /** 根据订单编号查询订单详情 */
    OrderInfo queryByAppCode(@Param("appCode") String appCode);
}