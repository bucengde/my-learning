package com.learning.es;

import com.learning.autoconfigure.annotation.EnableMyDatasource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * ElasticSearch服务
 * @author Wang Xu
 * @date 2020/10/19
 */
@SpringBootApplication
@EnableMyDatasource({"com.learning.es.mapper.**"})
public class ESServerStarter {
    public static void main(String[] args) {
        SpringApplication.run(ESServerStarter.class, args);
    }
}