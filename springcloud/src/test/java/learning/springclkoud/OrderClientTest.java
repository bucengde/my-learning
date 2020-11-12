package learning.springclkoud;

import com.google.common.collect.Maps;
import learning.springcloud.AppStarter;
import learning.springcloud.client.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Xu
 * @date 2020/10/11
 */
@SpringBootTest(classes = {AppStarter.class})
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class OrderClientTest {

    @Resource
    private OrderClient orderClient;

    @Test
    public void orderClientTest() {
        Map<String, String> map = Maps.newHashMap();
        map.put("key1", "v1111");
        map.put("key2", "v2222");
        Map<String, Object> query = orderClient.query(map, "mmd");
        log.info(query.toString());
    }
}