package com.learning.autoconfigure.aware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Wang Xu
 * @date 2020/10/19
 */
@Configuration
public class ContextConfiguration {

    @Bean
    public IApplicationContext iApplicationContext() {
        return new IApplicationContext();
    }
}