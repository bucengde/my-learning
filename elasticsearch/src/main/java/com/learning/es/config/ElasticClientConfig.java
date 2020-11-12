package com.learning.es.config;

import com.learning.es.config.prop.ElasticProp;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Wang Xu
 * @date 2020/10/25
 */
@Configuration
@EnableConfigurationProperties(ElasticProp.class)
public class ElasticClientConfig {
    @Autowired
    private ElasticProp elasticProp;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(elasticProp.getHost(), elasticProp.getPort(), "http")));
    }
}