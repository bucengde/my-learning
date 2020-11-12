package com.learning.es.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Wang Xu
 * @date 2020/10/25
 */
@ConfigurationProperties(prefix = "elastic")
@Data
public class ElasticProp {
    private String host = "localhost";
    private Integer port = 9200;
}