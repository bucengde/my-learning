package learning.springcloud.web.contrller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author Wang Xu
 * @date 2021/4/5
 */
@RestController
@RequestMapping("consumer/nacos")
public class NacosConsumerController {

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("test1/{note}")
    public String test1(@PathVariable("note") String note) {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://learning-cloud/nacos/test1", String.class);
        return note + " === " + forEntity.getBody();
    }
}