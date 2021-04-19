package learning.springcloud.web.contrller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wang Xu
 * @date 2021/4/5
 */
@RestController
@RequestMapping("/nacos")
public class NacosProviderController {
    @Value("${server.port}")
    private int port;

    @GetMapping("/test1")
    public String test1() {
        return "prot: " + port + "nacos test111111 . . .";
    }

    @GetMapping("/test2")
    public String test2() {
        return "prot: " + port + "nacos test222222 . . .";
    }
}