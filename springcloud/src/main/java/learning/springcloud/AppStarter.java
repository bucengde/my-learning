package learning.springcloud;

import com.learning.autoconfigure.annotation.EnableMyDatasource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @author Wang Xu
 * @date 2020/10/11
 */
//@EnableHystrix
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableMyDatasource({"learning.springcloud.mapper.**"})
public class AppStarter {
    public static void main(String[] args) {
        SpringApplication.run(AppStarter.class, args);
    }
}