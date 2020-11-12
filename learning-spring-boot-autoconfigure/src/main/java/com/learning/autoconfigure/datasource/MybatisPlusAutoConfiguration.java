package com.learning.autoconfigure.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

/**
 * 数据源自动配置（druid + mybatis-plus）
 * @author Wang Xu
 * @date 2020/10/19
 */
@Configuration
//@ConditionalOnMissingClass("javax.sql.DataSource")
@ConditionalOnMissingBean(DataSource.class)
@EnableConfigurationProperties({DatasourceProperties.class, MybatisPlusProperties.class})
public class MybatisPlusAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisPlusAutoConfiguration.class);

    @Resource
    private DatasourceProperties datasourceProperties;
    @Resource
    private MybatisPlusProperties mybatisPlusProperties;

    @Bean(name = "druidDataSource", initMethod = "init", destroyMethod = "close")
    @ConditionalOnMissingBean({DruidDataSource.class})
    public DruidDataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(datasourceProperties.getDriverClassName());
        druidDataSource.setUrl(datasourceProperties.getUrl());
        druidDataSource.setUsername(datasourceProperties.getUsername());
        druidDataSource.setPassword(datasourceProperties.getPassword());
        druidDataSource.setInitialSize(datasourceProperties.getInitialSize());
        druidDataSource.setMinIdle(datasourceProperties.getMinIdle());
        druidDataSource.setMaxActive(datasourceProperties.getMaxActive());
        druidDataSource.setMaxWait(datasourceProperties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(datasourceProperties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(datasourceProperties.getMinEvictableIdleTimeMillis());
        String validationQuery = datasourceProperties.getValidationQuery();
        if (Objects.nonNull(validationQuery) && !Strings.EMPTY.equals(validationQuery)) {
            druidDataSource.setValidationQuery(validationQuery);
        }
        druidDataSource.setTestWhileIdle(datasourceProperties.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(datasourceProperties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(datasourceProperties.isTestOnReturn());
        if(datasourceProperties.isPoolPreparedStatements()){
            druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(datasourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        }
        try {
            // 这是最关键的,否则SQL监控无法生效
            druidDataSource.setFilters(datasourceProperties.getFilters());
        } catch (SQLException e) {
            LOGGER.warn("druid set filters error, msg: {}", e.toString(), e);
        }
        String connectionPropertiesStr = datasourceProperties.getConnectionProperties();
        if(Objects.nonNull(connectionPropertiesStr) && !Strings.EMPTY.equals(connectionPropertiesStr)){
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
        druidDataSource.setUseGlobalDataSourceStat(datasourceProperties.isUseGlobalDataSourceStat());
        return druidDataSource;
    }

    @Bean
    @ConditionalOnBean({DruidDataSource.class})
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", datasourceProperties.getUsername());
        reg.addInitParameter("loginPassword", datasourceProperties.getPassword());
        reg.addInitParameter("logSlowSql", datasourceProperties.getLogShowSql());
        return reg;
    }

    @Bean
    @ConditionalOnBean({DruidDataSource.class})
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    @Bean("mybatisPlusGlobalConfig")
    @ConditionalOnBean({DataSource.class})
    @ConditionalOnMissingBean({MybatisSqlSessionFactoryBean.class})
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        globalConfig.setDbConfig(dbConfig);
        // 设置主键自动增长
        dbConfig.setIdType(IdType.AUTO);
        // 设置表前缀
//        dbConfig.setTablePrefix("sys_");
        // 设置逻辑删除
        dbConfig.setLogicDeleteValue("1");
        dbConfig.setLogicNotDeleteValue("0");
        return globalConfig;
    }

    /**
     * 1.分页插件
     * 2.多租户
     */
    @Bean
    @ConditionalOnBean({DataSource.class, DruidDataSource.class})
    @ConditionalOnMissingBean({MybatisSqlSessionFactoryBean.class})
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // (你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);
        paginationInterceptor.setLimit(mybatisPlusProperties.getPageLimitCount());
        return paginationInterceptor;
    }

    @Bean("mybatisPlusSqlSessionFactory")
    @ConditionalOnBean({DataSource.class})
    @ConditionalOnMissingBean({MybatisSqlSessionFactoryBean.class})
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource,
        @Qualifier("paginationInterceptor") PaginationInterceptor paginationInterceptor,
        @Qualifier("mybatisPlusGlobalConfig") GlobalConfig globalConfig) throws IOException {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        try {
            MybatisConfiguration configuration = new MybatisConfiguration();
            configuration.setMapUnderscoreToCamelCase(mybatisPlusProperties.getMapUnderscoreToCamelCase());
            configuration.setDefaultFetchSize(mybatisPlusProperties.getFetchSize());
            configuration.setDefaultStatementTimeout(mybatisPlusProperties.getStatementTimeout());
            configuration.setSafeResultHandlerEnabled(mybatisPlusProperties.getSafeResultHandlerEnabled());
            configuration.setLogImpl(StdOutImpl.class);
            sqlSessionFactory.setConfiguration(configuration);
            // 注意全局配置是 配置在 sqlSessionFactory 里，不是 MybatisConfiguration 里
            sqlSessionFactory.setGlobalConfig(globalConfig);
            // 注册 PaginationInterceptor 插件
            sqlSessionFactory.setPlugins(new Interceptor[]{paginationInterceptor});
//            sqlSessionFactory.setPlugins(interceptors);
            // 配置扫描的枚举包路径
//            sqlSessionFactory.setTypeEnumsPackage(BASE_PACKAGE_ENUM);
//            sqlSessionFactory.setTypeAliasesPackage(BASE_PACKAGE_DB);
            sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:**/*Mapper.xml"));
            // SpringBootVFS 继承了 VFS ：用于解决jar运行时，VFS 的默认实现DefaultVFS无法扫描识别mapper别名映射的实体
            sqlSessionFactory.setVfs(SpringBootVFS.class);
        } catch (Exception e) {
            LOGGER.error("shardingSqlSessionFactory configuration failed, msg: {}", e.toString(), e);
            throw e;
        }
        return sqlSessionFactory;
    }

}