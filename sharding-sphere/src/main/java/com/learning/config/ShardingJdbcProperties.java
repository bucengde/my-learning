package com.learning.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Wang Xu
 * @date 2020/10/5
 */
@Data
@ConfigurationProperties(prefix = "sharding-jdbc.prop")
public class ShardingJdbcProperties {
    /** 是否打印SQL解析和改写日志 */
    private String sqlShow = Boolean.FALSE.toString();
    /** 用于SQL执行的工作线程数量，为 0 则表示无限制 */
    private Integer executorSize = 0;
    /** 每个物理数据库为每次查询分配的最大连接数量 */
    private Integer maxConnectionsSizePerQuery = 1;
    /** 是否在启动时检查分表元数据一致性 */
    private String checkTableMetadataEnabled = Boolean.FALSE.toString();
}