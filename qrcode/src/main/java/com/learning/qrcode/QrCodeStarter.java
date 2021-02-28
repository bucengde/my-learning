package com.learning.qrcode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author Wang Xu
 * @date 2021/2/27
 */
@Slf4j
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class QrCodeStarter {

    public static void main(String[] args) {
        log.info("######################\n########### QrCodeStarter start run ... ###\n##############################################");
        SpringApplication.run(QrCodeStarter.class, args);
        log.info("##############################################\n########### QrCodeStarter run finish ... ####\n##############################################");
    }
}