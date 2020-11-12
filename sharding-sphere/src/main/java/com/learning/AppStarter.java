package com.learning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Wang Xu
 * @date 2020/10/4
 */
@SpringBootApplication
public class AppStarter {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AppStarter.class);
        springApplication.run(args);
    }
}