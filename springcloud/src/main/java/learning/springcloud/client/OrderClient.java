package learning.springcloud.client;

import learning.springcloud.config.client.OrderClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Wang Xu
 * @date 2020/10/11
 */
@FeignClient(name = "order-server", url = "localhost:9000", contextId = "OrderClient", configuration = OrderClientConfiguration.class, path = "/order")
public interface OrderClient {

    @GetMapping(value = "/map", headers = {"hh1=${header.hh1}", "hh2=${header.hh2}"})
    Map<String, Object> query(@SpringQueryMap Map<String, String> map, @RequestParam("param") String param);
}