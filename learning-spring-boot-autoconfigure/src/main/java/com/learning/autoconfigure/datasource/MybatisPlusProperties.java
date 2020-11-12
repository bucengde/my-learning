package com.learning.autoconfigure.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Wang Xu
 * @date 2020/10/19
 */
@Data
@ConfigurationProperties(prefix = "mybatis-plus.prop")
public class MybatisPlusProperties {
    private Long pageLimitCount = 500L;
    private Boolean mapUnderscoreToCamelCase = true;
    private Integer fetchSize = 5;
    private Integer statementTimeout = 60000;
    private Boolean safeResultHandlerEnabled = true;
}
