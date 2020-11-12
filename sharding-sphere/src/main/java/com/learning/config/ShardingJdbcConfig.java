package com.learning.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.google.common.collect.Range;
import com.learning.global.CodeException;
import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.algorithm.sharding.standard.RangeShardingAlgorithm;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.api.config.strategy.NoneShardingStrategyConfiguration;
import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import io.shardingsphere.core.keygen.KeyGenerator;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import io.shardingsphere.transaction.api.TransactionType;
import io.shardingsphere.transaction.api.TransactionTypeHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * sharding-jdbc 配置类
 * @author Wang Xu
 * @date 2020/10/5
 */
@Slf4j
@Configuration
@AutoConfigureBefore({DynamicDataSourceAutoConfiguration.class, SpringBootConfiguration.class})
@EnableConfigurationProperties(ShardingJdbcProperties.class)
@MapperScan(value = "com.learning.mapper", sqlSessionTemplateRef = "shardingSqlSessionTemplate")
public class ShardingJdbcConfig {
    public static final String DEFAULT_DATA_SOURCE = "ds0";
    public static final String DATA_SOURCE_ONE = "ds1";
    public static final String DATA_SOURCE_TWO = "ds2";

    @Resource
    private ShardingJdbcProperties shardingJdbcProperties;

    @Resource(name = "defaultDataSource")
    private DataSource defaultDataSource;
    //这里直接注入你项目中配置的数据源
    @Resource(name = "dataSourceOne")
    private DataSource dataSourceOne;
    @Resource(name = "dataSourceTwo")
    private DataSource dataSourceTwo;

    //分布式唯一主键
    @Bean("myKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new DefaultKeyGenerator();
    }

    @Bean(name = "shardingDataSource")
    public DataSource dataSource(@Qualifier("myKeyGenerator") KeyGenerator keyGenerator) throws SQLException {

        TransactionTypeHolder.set(TransactionType.LOCAL);
        //1、指定需要分库分表的数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put(DATA_SOURCE_ONE, dataSourceOne);
        dataSourceMap.put(DATA_SOURCE_TWO, dataSourceTwo);
        //2、分库分表配置
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        //2.1、配置默认自增主键生成器
        shardingRuleConfig.setDefaultKeyGenerator(keyGenerator);
        //2.2、配置各个表的分库分表策略，这里只配了一张表的就是order_info
        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration(keyGenerator));
        //2.3、配置绑定表规则列表，级联绑定表代表一组表，这组表的逻辑表与实际表之间的映射关系是相同的
//        shardingRuleConfig.getBindingTableGroups().add("order_info","order_info_item");
        //2.4、配置广播表规则列表，利用广播小表提高性能
//        shardingRuleConfig.getBroadcastTables().add("t_config");
        //2.5、配置默认分表规则
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new NoneShardingStrategyConfiguration());
        //2.6、配置默认分库规则(不配置分库规则,则只采用分表规则)
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new NoneShardingStrategyConfiguration());
        //2.7、配置默认数据源
        shardingRuleConfig.setDefaultDataSourceName(DATA_SOURCE_ONE);
        //2.8、配置读写分离规则
//        shardingRuleConfig.setMasterSlaveRuleConfigs();

        //3、属性配置项，可以为以下属性
        Properties properties = new Properties();
        //是否打印SQL解析和改写日志
        properties.setProperty("sql.show", shardingJdbcProperties.getSqlShow());
        //用于SQL执行的工作线程数量，为 0 则表示无限制
        properties.setProperty("executor.size", shardingJdbcProperties.getExecutorSize().toString());
        //每个物理数据库为每次查询分配的最大连接数量
        properties.setProperty("max.connections.size.per.query", shardingJdbcProperties.getMaxConnectionsSizePerQuery().toString());
        //是否在启动时检查分表元数据一致性
        properties.setProperty("check.table.metadata.enabled", shardingJdbcProperties.getCheckTableMetadataEnabled());

        //4、用户自定义属性
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("effect","分库分表test");
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, configMap, properties);

        //5、数据治理
        //5.1、配置注册中心
//        RegistryCenterConfiguration regConfig = new RegistryCenterConfiguration();
//        regConfig.setServerLists("localhost:2000");
//        regConfig.setNamespace("sharding-sphere-orchestration");
        //regConfig.setDigest("data-center");
        //5.2、配置数据治理
//        OrchestrationConfiguration orchestrationConfig = new OrchestrationConfiguration("orchestration-sharding-data-source", regConfig, true);
        //5.3、获取数据源对象
//        DataSource dataSource = OrchestrationShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, configMap, properties, orchestrationConfig);
        return dataSource;
    }

    /**
     * Sharding提供了5种分片策略：
     *      StandardShardingStrategyConfiguration:标准分片策略, 提供对SQL语句中的=, IN和BETWEEN AND的分片操作支持
     *      ComplexShardingStrategyConfiguration:复合分片策略, 提供对SQL语句中的=, IN和BETWEEN AND的分片操作支持。
     *      InlineShardingStrategyConfiguration:Inline表达式分片策略, 使用Groovy的Inline表达式，提供对SQL语句中的=和IN的分片操作支持
     *      HintShardingStrategyConfiguration:通过Hint而非SQL解析的方式分片的策略
     *      NoneShardingStrategyConfiguration:不分片的策略
     * Sharding提供了以下4种算法接口：
     *      PreciseShardingAlgorithm
     *      RangeShardingAlgorithm
     *      HintShardingAlgorithm
     *      ComplexKeysShardingAlgorithm
     * @return
     */
    private TableRuleConfiguration getOrderTableRuleConfiguration(KeyGenerator keyGenerator) {
        TableRuleConfiguration result = new TableRuleConfiguration();
        //1、指定逻辑索引(oracle/PostgreSQL 需要配置)
//        result.setLogicIndex("order_id");
        //2、指定逻辑表名
        result.setLogicTable("t_order");
        //3、指定映射的实际表名
        result.setActualDataNodes("ds${1..2}.t_order_${1..2}");
        //4、配置分库策略，缺省表示使用默认分库策略
//        result.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id","ds${user_id % 2 + 1}"));
        result.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id","ds${user_id % 2 + 1}"));
        //result.setDatabaseShardingStrategyConfig(new HintShardingStrategyConfiguration(new OrderDataBaseHintShardingAlgorithm()));
        //5、配置分表策略,缺省表示使用默认分表策略
        result.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order_${order_id % 2 + 1}"));
//        result.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id", new OrderPreciseShardingAlgorithm(), new OrderRangeShardingAlgorithm()));
//        result.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("order_id,user_id", new OrderComplexKeysShardingAlgorithm()));
//        result.setTableShardingStrategyConfig(new HintShardingStrategyConfiguration(new OrderTableHintShardingAlgorithm()));
        //6、指定自增字段以及key的生成方式
        result.setKeyGeneratorColumnName("order_id");
        result.setKeyGenerator(keyGenerator);
        return result;
    }

//    @Bean(name = "shardingSqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactorySharding(@Qualifier("shardingDataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/**Mapper.xml"));
//        return bean.getObject();
//    }

    @Bean(name = "shardingSqlSessionFactory")
//    public MybatisSqlSessionFactoryBean sqlSessionFactory(@Qualifier("shardingDataSource") DataSource dataSource,
public MybatisSqlSessionFactoryBean sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource,
                                                      @Qualifier("paginationInterceptor") PaginationInterceptor paginationInterceptor , GlobalConfig globalConfig) throws CodeException {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        try {
            MybatisConfiguration configuration = new MybatisConfiguration();
            configuration.setMapUnderscoreToCamelCase(true);
            configuration.setDefaultFetchSize(5);
            configuration.setDefaultStatementTimeout(60000);
            configuration.setSafeResultHandlerEnabled(true);
            configuration.setSafeResultHandlerEnabled(true);
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
            sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/**/*.xml"));
            // SpringBootVFS 继承了 VFS ：用于解决jar运行时，VFS 的默认实现DefaultVFS无法扫描识别mapper别名映射的实体
            sqlSessionFactory.setVfs(SpringBootVFS.class);
        } catch (IOException e) {
            throw new CodeException("IO_EXCEPTION", "shardingSqlSessionFactory configuration failed");
        }
        return sqlSessionFactory;
    }

    //本地事务
    @Bean(name = "shardingTransactionManagerLOCAL")
    public PlatformTransactionManager transactionManagerLocal(@Qualifier("shardingDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

      //XA事务
//    @Bean(name = "shardingTransactionManagerXA")
//    public ShardingTransactionManager transactionManagerXA(@Qualifier("shardingDataSource") DataSource dataSource) {
//        return new AtomikosTransactionManager();
//    }

    @Bean(name = "shardingSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplateThree(@Qualifier("shardingSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //PreciseShardingAlgorithm接口实现（用于处理 = 和 in 的路由）
    @SuppressWarnings("rawtypes")
    final class OrderPreciseShardingAlgorithm implements PreciseShardingAlgorithm {
        @Override
        public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {
            log.info("collection：" + JSON.toJSONString(collection) + "，preciseShardingValue：" + JSON.toJSONString(preciseShardingValue));
            for (Object name : collection) {
                if (((String)name).endsWith((Integer)preciseShardingValue.getValue() % collection.size() + 1 + "")) {
                    log.info("return name：" + name);
                    return (String)name;
                }
            }
            return null;
        }
    }

    //RangeShardingAlgorithm接口实现（用于处理BETWEEN AND分片），这里的核心是找出这个范围的数据分布在那些表(库)中
    @SuppressWarnings({"rawtypes", "unchecked"})
    final class OrderRangeShardingAlgorithm implements RangeShardingAlgorithm {
        @Override
        public Collection doSharding(Collection collection, RangeShardingValue rangeShardingValue) {
            log.info("Range collection：" + JSON.toJSONString(collection) + "，rangeShardingValue：" + JSON.toJSONString(rangeShardingValue));
            Collection collect = new ArrayList<>();
            Range valueRange = rangeShardingValue.getValueRange();
            for (Long i = (Long)valueRange.lowerEndpoint(); i <= (Long)valueRange.upperEndpoint(); i++) {
                for (Object each : collection) {
                    if (((String)each).endsWith(i % collection.size() + 1 + "")) {
                        collect.add(each);
                    }
                }
            }
            return collect;
        }
    }


    //ComplexShardingStrategy支持多分片键
    @SuppressWarnings({"rawtypes", "unchecked"})
    final class OrderComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm {
        @Override
        public Collection doSharding(Collection collection, Collection shardingValues) {
            log.info("collection：" + JSON.toJSONString(collection) + "，shardingValues：" + JSON.toJSONString(shardingValues));
            Collection orderIdValues = getShardingValue(shardingValues, "order_id");
            Collection userIdValues = getShardingValue(shardingValues, "user_id");
            List shardingSuffix = new ArrayList<>();
            /* 例如：根据 user_id + order_id 双分片键来进行分表 */
            //<Set> valueResult = Sets.cartesianProduct(userIdValues, orderIdValues);
            for (Object userIdVal : userIdValues) {
                Long userIdValLong = (Long)userIdVal;
                for (Object orderIdVal : orderIdValues) {
                    Long orderIdValLong = (Long)orderIdVal;
                    String suffix = userIdValLong % 2 + 1 + "_" + (orderIdValLong % 2 + 1);
                    collection.forEach(tableName -> {
                        if (((String)tableName).endsWith(suffix)) {
                            shardingSuffix.add(tableName);
                        }
                    });
                }
            }
            return shardingSuffix;
        }

        private Collection getShardingValue(Collection shardingValues, final String key) {
            for (Object shardingValue : shardingValues) {
                ShardingValue next = (ShardingValue) shardingValue;
                if (next instanceof ListShardingValue) {
                    ListShardingValue value = (ListShardingValue) next;
                    /* 例如：根据user_id + order_id 双分片键来进行分表 */
                    if (value.getColumnName().equals(key)) {
                        return value.getValues();
                    }
                }
            }
            return Collections.emptySet();
        }
    }

    //表的强制分片策略
    @SuppressWarnings({"rawtypes", "unchecked"})
    final class OrderTableHintShardingAlgorithm implements HintShardingAlgorithm {
        @Override
        public Collection doSharding(Collection collection, ShardingValue shardingValue) {
            log.info("collection：" + JSON.toJSONString(collection) + "，shardingValues：" + JSON.toJSONString(shardingValue));
            Collection result = new ArrayList<>();
            String logicTableName = shardingValue.getLogicTableName();
            ListShardingValue listShardingValue = (ListShardingValue) shardingValue;
            List shardingValueList = new ArrayList(listShardingValue.getValues());
            String table = logicTableName + "_" + shardingValueList.get(0);
            result.add(table);
            return result;
        }
    }

    //库的强制分片策略
    @SuppressWarnings({"rawtypes", "unchecked"})
    final class OrderDataBaseHintShardingAlgorithm implements HintShardingAlgorithm {
        @Override
        public Collection doSharding(Collection collection, ShardingValue shardingValue) {
            log.info("collection：" + JSON.toJSONString(collection) + "，shardingValues：" + JSON.toJSONString(shardingValue));
            Collection result = new ArrayList<>();
            ListShardingValue listShardingValue = (ListShardingValue) shardingValue;
            List list = new ArrayList(listShardingValue.getValues());
            for (Object db : collection) {
                String dbName = (String)db;
                if (dbName.endsWith(list.get(0).toString())) {
                    result.add(db);
                }
            }
            return result;
        }
    }

}