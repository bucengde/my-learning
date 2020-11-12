package learning.springcloud;

import com.learning.autoconfigure.annotation.EnableMyDatasource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Wang Xu
 * @date 2020/10/11
 */
@EnableFeignClients
@SpringBootApplication
@EnableMyDatasource({"learning.springcloud.mapper.**"})
public class AppStarter {
    public static void main(String[] args) {
        SpringApplication.run(AppStarter.class, args);
    }
}