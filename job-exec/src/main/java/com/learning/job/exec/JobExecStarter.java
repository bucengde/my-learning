package com.learning.job.exec;

import com.learning.autoconfigure.annotation.EnableMyDatasource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Wang Xu
 * @date 2021/2/4
 */
@SpringBootApplication
@EnableMyDatasource({"com.learning.job.exec.mapper.**"})
public class JobExecStarter {

    public static void main(String[] args) {
        SpringApplication.run(JobExecStarter.class, args);
    }

}