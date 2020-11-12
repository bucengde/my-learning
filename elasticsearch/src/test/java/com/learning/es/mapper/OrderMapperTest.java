package com.learning.es.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.es.bean.db.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author Wang Xu
 * @date 2020/10/19
 */
@Slf4j
public class OrderMapperTest extends BaseTestBean {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Test
    public void queryByAppCode() {
        OrderInfo orderInfo = orderInfoMapper.queryByAppCode("SN180827155838000349");
        log.debug("##################  orderInfo :  {}", orderInfo);
    }

    @Test
    public void queryPlusTest() {
        IPage<OrderInfo> page = new Page<>();
        page.setPages(1);
        page.setSize(200);
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.le("flow_seq", 2);
        IPage<OrderInfo> orderInfoIPage = orderInfoMapper.selectPage(page, wrapper);
        log.error("##################  getTotal :  {}", orderInfoIPage.getTotal());
        log.error("##################  getCurrent :  {}", orderInfoIPage.getCurrent());
        log.error("##################  getRecords.size :  {}", orderInfoIPage.getRecords().size());

    }


}