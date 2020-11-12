package com.learning.vps;

import com.learning.autoconfigure.annotation.EnableMyDatasource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Wang Xu
 * @date 2020/10/20
 */
@SpringBootApplication
@EnableMyDatasource("com.learning.vps.mapper.**")
@EnableFeignClients
public class VpsAppStarter {
    public static void main(String[] args) {
        SpringApplication.run(VpsAppStarter.class, args);
    }
}