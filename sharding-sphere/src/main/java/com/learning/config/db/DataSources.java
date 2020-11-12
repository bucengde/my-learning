package com.learning.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.learning.config.ShardingJdbcConfig;
import com.learning.config.settings.DruidSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Wang Xu
 * @date 2020/10/5
 */
@Configuration
@EnableConfigurationProperties(DruidSettings.class)
public class DataSources {
    public static final String SHARDING_DATA_SOURCE_NAME = "sharding-ds";
    @Resource
    private DruidSettings druidSettings;

    // 动态数据源配置
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider(@Qualifier("shardingDataSource") DataSource shardingDataSource) {
        return () -> {
            Map<String, DataSource> dataSourceMap = new HashMap<>();
            dataSourceMap.put(ShardingJdbcConfig.DEFAULT_DATA_SOURCE, defaultDataSource());
            // 将 sharding-jdbc 管理的数据源也交给动态数据源管理
            dataSourceMap.put(SHARDING_DATA_SOURCE_NAME, shardingDataSource);
            return dataSourceMap;
        };
    }

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("shardingDataSource") DataSource shardingDataSource, DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        dynamicRoutingDataSource.addDataSource(ShardingJdbcConfig.DEFAULT_DATA_SOURCE, defaultDataSource());
        dynamicRoutingDataSource.addDataSource(SHARDING_DATA_SOURCE_NAME, shardingDataSource);
        dynamicRoutingDataSource.setPrimary(ShardingJdbcConfig.DEFAULT_DATA_SOURCE);
        dynamicRoutingDataSource.setProvider(dynamicDataSourceProvider);
        return dynamicRoutingDataSource;
    }

    @Bean(name = "defaultDataSource",initMethod = "init", destroyMethod = "close")
//    @Primary
    public DruidDataSource defaultDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(druidSettings.getDefaultDataSourceSetting().getDriverClassName());
        druidDataSource.setUrl(druidSettings.getDefaultDataSourceSetting().getUrl());
        druidDataSource.setUsername(druidSettings.getDefaultDataSourceSetting().getUsername());
        druidDataSource.setPassword(druidSettings.getDefaultDataSourceSetting().getPassword());
        druidDataSource.setInitialSize(druidSettings.getDefaultDataSourceSetting().getInitialSize());
        druidDataSource.setMinIdle(druidSettings.getDefaultDataSourceSetting().getMinIdle());
        druidDataSource.setMaxActive(druidSettings.getDefaultDataSourceSetting().getMaxActive());
        druidDataSource.setMaxWait(druidSettings.getDefaultDataSourceSetting().getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(druidSettings.getDefaultDataSourceSetting().getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(druidSettings.getDefaultDataSourceSetting().getMinEvictableIdleTimeMillis());
        String validationQuery = druidSettings.getDefaultDataSourceSetting().getValidationQuery();
        if (validationQuery != null && !"".equals(validationQuery)) {
            druidDataSource.setValidationQuery(validationQuery);
        }
        druidDataSource.setTestWhileIdle(druidSettings.getDefaultDataSourceSetting().isTestWhileIdle());
        druidDataSource.setTestOnBorrow(druidSettings.getDefaultDataSourceSetting().isTestOnBorrow());
        druidDataSource.setTestOnReturn(druidSettings.getDefaultDataSourceSetting().isTestOnReturn());
        if(druidSettings.getDefaultDataSourceSetting().isPoolPreparedStatements()){
            druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(druidSettings.getDefaultDataSourceSetting().getMaxPoolPreparedStatementPerConnectionSize());
        }
        try {
            druidDataSource.setFilters(druidSettings.getDefaultDataSourceSetting().getFilters());//这是最关键的,否则SQL监控无法生效
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String connectionPropertiesStr = druidSettings.getDefaultDataSourceSetting().getConnectionProperties();
        if(connectionPropertiesStr != null && !"".equals(connectionPropertiesStr)){
            Properties connectProperties = new Properties();
            String[] propertiesList = connectionPropertiesStr.split(";");
            for(String propertiesTmp:propertiesList){
                String[] obj = propertiesTmp.split("=");
                String key = obj[0];
                String value = obj[1];
                connectProperties.put(key,value);
            }
            druidDataSource.setConnectProperties(connectProperties);
        }
        druidDataSource.setUseGlobalDataSourceStat(druidSettings.getDefaultDataSourceSetting().isUseGlobalDataSourceStat());
        return druidDataSource;
    }

    @Bean(name = "dataSourceOne", initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSourceOne() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(druidSettings.getDataSourceOneSetting().getDriverClassName());
        druidDataSource.setUrl(druidSettings.getDataSourceOneSetting().getUrl());
        druidDataSource.setUsername(druidSettings.getDataSourceOneSetting().getUsername());
        druidDataSource.setPassword(druidSettings.getDataSourceOneSetting().getPassword());
        druidDataSource.setInitialSize(druidSettings.getDataSourceOneSetting().getInitialSize());
        druidDataSource.setMinIdle(druidSettings.getDataSourceOneSetting().getMinIdle());
        druidDataSource.setMaxActive(druidSettings.getDataSourceOneSetting().getMaxActive());
        druidDataSource.setMaxWait(druidSettings.getDataSourceOneSetting().getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(druidSettings.getDataSourceOneSetting().getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(druidSettings.getDataSourceOneSetting().getMinEvictableIdleTimeMillis());
        String validationQuery = druidSettings.getDataSourceOneSetting().getValidationQuery();
        if (validationQuery != null && !"".equals(validationQuery)) {
            druidDataSource.setValidationQuery(validationQuery);
        }
        druidDataSource.setTestWhileIdle(druidSettings.getDataSourceOneSetting().isTestWhileIdle());
        druidDataSource.setTestOnBorrow(druidSettings.getDataSourceOneSetting().isTestOnBorrow());
        druidDataSource.setTestOnReturn(druidSettings.getDataSourceOneSetting().isTestOnReturn());
        if(druidSettings.getDataSourceOneSetting().isPoolPreparedStatements()){
            druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(druidSettings.getDataSourceOneSetting().getMaxPoolPreparedStatementPerConnectionSize());
        }
        try {
            druidDataSource.setFilters(druidSettings.getDataSourceOneSetting().getFilters());//这是最关键的,否则SQL监控无法生效
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String connectionPropertiesStr = druidSettings.getDataSourceOneSetting().getConnectionProperties();
        if(connectionPropertiesStr != null && !"".equals(connectionPropertiesStr)){
            Properties connectProperties = new Properties();
            String[] propertiesList = connectionPropertiesStr.split(";");
            for(String propertiesTmp:propertiesList){
                String[] obj = propertiesTmp.split("=");
                String key = obj[0];
                String value = obj[1];
                connectProperties.put(key,value);
            }
            druidDataSource.setConnectProperties(connectProperties);
        }
        druidDataSource.setUseGlobalDataSourceStat(druidSettings.getDataSourceOneSetting().isUseGlobalDataSourceStat());
        return druidDataSource;
    }

    @Bean(name = "dataSourceTwo", initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSourceTwo() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(druidSettings.getDataSourceTwoSetting().getDriverClassName());
        druidDataSource.setUrl(druidSettings.getDataSourceTwoSetting().getUrl());
        druidDataSource.setUsername(druidSettings.getDataSourceTwoSetting().getUsername());
        druidDataSource.setPassword(druidSettings.getDataSourceTwoSetting().getPassword());
        druidDataSource.setInitialSize(druidSettings.getDataSourceTwoSetting().getInitialSize());
        druidDataSource.setMinIdle(druidSettings.getDataSourceTwoSetting().getMinIdle());
        druidDataSource.setMaxActive(druidSettings.getDataSourceTwoSetting().getMaxActive());
        druidDataSource.setMaxWait(druidSettings.getDataSourceTwoSetting().getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(druidSettings.getDataSourceTwoSetting().getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(druidSettings.getDataSourceTwoSetting().getMinEvictableIdleTimeMillis());
        String validationQuery = druidSettings.getDataSourceTwoSetting().getValidationQuery();
        if (validationQuery != null && !"".equals(validationQuery)) {
            druidDataSource.setValidationQuery(validationQuery);
        }
        druidDataSource.setTestWhileIdle(druidSettings.getDataSourceTwoSetting().isTestWhileIdle());
        druidDataSource.setTestOnBorrow(druidSettings.getDataSourceTwoSetting().isTestOnBorrow());
        druidDataSource.setTestOnReturn(druidSettings.getDataSourceTwoSetting().isTestOnReturn());
        if(druidSettings.getDataSourceTwoSetting().isPoolPreparedStatements()){
            druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(druidSettings.getDataSourceTwoSetting().getMaxPoolPreparedStatementPerConnectionSize());
        }
        try {
            druidDataSource.setFilters(druidSettings.getDataSourceTwoSetting().getFilters());//这是最关键的,否则SQL监控无法生效
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String connectionPropertiesStr = druidSettings.getDataSourceTwoSetting().getConnectionProperties();
        if(connectionPropertiesStr != null && !"".equals(connectionPropertiesStr)){
            Properties connectProperties = new Properties();
            String[] propertiesList = connectionPropertiesStr.split(";");
            for(String propertiesTmp:propertiesList){
                String[] obj = propertiesTmp.split("=");
                String key = obj[0];
                String value = obj[1];
                connectProperties.put(key,value);
            }
            druidDataSource.setConnectProperties(connectProperties);
        }
        druidDataSource.setUseGlobalDataSourceStat(druidSettings.getDataSourceTwoSetting().isUseGlobalDataSourceStat());
        return druidDataSource;
    }

    /**
     * 配置监控功能的Servlet
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", druidSettings.getDefaultDataSourceSetting().getUsername());
        reg.addInitParameter("loginPassword", druidSettings.getDefaultDataSourceSetting().getPassword());
        reg.addInitParameter("logSlowSql", druidSettings.getDefaultDataSourceSetting().getLogShowSql());
        return reg;
    }

    /**
     * 配置监控功能的Filter
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

}