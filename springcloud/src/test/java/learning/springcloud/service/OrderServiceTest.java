package learning.springcloud.service; 

import learning.springcloud.AppStarter;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/** 
* OrderService Tester. 
* 
* @author Wang Xu 
* @since <pre>10/27/2020</pre> 
* @version 1.0 
*/
@SpringBootTest("learning.springcloud.AppStarter")
@RunWith(SpringRunner.class)
public class OrderServiceTest {
    @Resource
    OrderService orderService;
    
    @Test
    public void testTestTransaction() throws Exception {
        orderService.testTransaction();
    }

}
