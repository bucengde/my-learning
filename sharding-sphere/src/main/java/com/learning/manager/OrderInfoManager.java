package com.learning.manager;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.es.bean.db.OrderInfo;
import com.learning.es.bean.db.TOrder;
import com.learning.bean.dto.OrderInfoDTO;
import com.learning.config.db.DataSources;
import com.learning.es.mapper.OrderInfoMapper;
import com.learning.es.mapper.TOrderMapper;
import com.learning.utils.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Wang Xu
 * @date 2020/10/4
 */
@Component
public class OrderInfoManager {
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private TOrderMapper tOrderMapper;

    public List<OrderInfoDTO> getTopTen() {
        IPage<OrderInfo> page = new Page<>(1, 10);
        IPage<OrderInfo> orderInfoPage = orderInfoMapper.selectPage(page, Wrappers.emptyWrapper());
        return BeanUtils.DozerBeanMapperUtil.mapList(orderInfoPage.getRecords(), OrderInfoDTO.class);
    }

    public void simpleShardingInsert(OrderInfo orderInfo) {
        orderInfoMapper.insert(orderInfo);
    }

    public void insert(TOrder order) {
        tOrderMapper.insert(order);
    }

    @DS(DataSources.SHARDING_DATA_SOURCE_NAME)
    public TOrder shardingSelect(int orderId) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
//        wrapper.eq("user_id", 100 + orderId);
//         因为这里使用了多分片，orderId+userId，所以这里的查询条件也要加上userId，否则会根据userId选库时，会查所有库
        return tOrderMapper.selectOne(wrapper);
    }

    @DS(DataSources.SHARDING_DATA_SOURCE_NAME)
    public IPage<TOrder> shardingSelectByLimit(int limit) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        IPage<TOrder> page = new Page<>(1, limit);
        wrapper.le("order_id", 100);
        return tOrderMapper.selectPage(page, wrapper);
    }
}