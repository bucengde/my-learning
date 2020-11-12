package com.learning.utils;

import com.learning.es.bean.db.TOrder;
import com.learning.bean.dto.OrderInfoDTO;
import com.learning.service.OrderInfoService;
import com.learning.utils.testbean.A;
import com.learning.utils.testbean.B;
import com.learning.utils.testbean.C;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

/** 
* BeanUtils Tester. 
* 
* @author Wang Xu 
* @since <pre>10/05/2020</pre> 
* @version 1.0 
*/
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class BeanUtilsTest { 
    @Autowired
    private OrderInfoService orderInfoService;

    @Test
    public void shardingSelectTest() {
//        TOrder tOrder = orderInfoService.shardingSelect(10);
//        IPage<TOrder> list = orderInfoService.shardingSelectByLimit(7);

        List<OrderInfoDTO> topTen = orderInfoService.getTopTen();
        topTen.forEach(item -> log.info(item.toString()));


//        log.error(tOrder.toString());

//        log.error("getTotal: {}, getPages: {}", list.getTotal(), list.getPages());
//        list.getRecords().forEach(item -> {
//            log.info(item.toString());
//        });

    }

    @Test
    public void shardingSphereTest() {
        TOrder tOrder = new TOrder();
        for (int i = 1; i <= 200; i++) {
            tOrder.setOrderId(i);
            tOrder.setUserId(new Random().nextInt(1000));
            orderInfoService.shardingInsert(tOrder);
        }

    }

    @Test
    public void mapperTest() {
        List<OrderInfoDTO> topTen = orderInfoService.getTopTen();
        topTen.forEach(System.err::println);
    }

    @Test
    public void testDeepCopy() throws Exception {
        A a = new A();
        C c = new C();
        a.setA("aaa");
        a.setB(1);
        a.setBb(11);
        a.setC(c);
        a.setF("fff");
        c.setCc("ccc");
//        B copy = BeanUtils.CglibBeanCopyUtil.copy(a, B.class);
        B copy = BeanUtils.DozerBeanMapperUtil.map(a, B.class);
        System.out.println(copy);
        copy.getC().setCc("bbbbcccc");
        System.out.println(copy);
        System.out.println(c);
    }


}