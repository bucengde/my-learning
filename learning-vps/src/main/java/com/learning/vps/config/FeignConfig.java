package com.learning.vps.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import feign.Logger;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Wang Xu
 * @date 2020/11/11
 */
@Slf4j
@Configuration
public class FeignConfig {
    @Autowired
    private RestTemplateBuilder builder;
    @Autowired
    private ObjectMapper objectMapper;

    // TODO 替换成自定义feign log
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    public RestTemplate restTemplate() {
        // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
        RestTemplate restTemplate = builder.build();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        List<HttpMessageConverter<?>> messageConverters = Lists.newArrayList();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        // 不加会出现异常: Could not extract response: no suitable HttpMessageConverter found for response type [class ]
        MediaType[] mediaTypes = new MediaType[]{
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_OCTET_STREAM,
                MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN,
                MediaType.TEXT_XML,
                MediaType.APPLICATION_STREAM_JSON,
                MediaType.APPLICATION_ATOM_XML,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_PDF,
        };
        converter.setSupportedMediaTypes(Arrays.asList(mediaTypes));
        try {
            // 通过反射设置MessageConverters
            Field field = restTemplate.getClass().getDeclaredField("messageConverters");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<HttpMessageConverter<?>> orgConverterList = (List<HttpMessageConverter<?>>)field.get(restTemplate);
            Optional<HttpMessageConverter<?>> opConverter = orgConverterList.stream()
                    .filter(orgConverter -> orgConverter.getClass().getName().equalsIgnoreCase(MappingJackson2HttpMessageConverter.class.getName()))
                    .findFirst();
            if (!opConverter.isPresent()) {
                return restTemplate;
            }
            // 添加 MappingJackson2HttpMessageConverter
            messageConverters.add(converter);

            // 添加原有的剩余的 HttpMessageConverter
            List<HttpMessageConverter<?>> leftConverterList = orgConverterList.stream()
                    .filter(orgConverter -> !orgConverter.getClass().getName().equalsIgnoreCase(MappingJackson2HttpMessageConverter.class.getName()))
                    .collect(Collectors.toList());
            messageConverters.addAll(leftConverterList);

            log.info("【HttpMessageConverter】原有数量：{}，重新构造后数量：{}", orgConverterList.size(), messageConverters.size());
        } catch (Exception e) {
            log.error("RestTemplate reflect config error, msg:{}", e.toString(), e);
        }
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }
}