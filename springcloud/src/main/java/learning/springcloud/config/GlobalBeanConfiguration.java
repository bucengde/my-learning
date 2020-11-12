package learning.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Wang Xu
 * @date 2020/10/28
 */
@Configuration
public class GlobalBeanConfiguration {

    @Bean
    public ThreadPoolTaskExecutor threadPoolExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(90);
        threadPoolTaskExecutor.setMaxPoolSize(110);
        threadPoolTaskExecutor.setQueueCapacity(90);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}