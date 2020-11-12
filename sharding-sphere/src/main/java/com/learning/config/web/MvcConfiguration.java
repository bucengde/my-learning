package com.learning.config.web;

import com.learning.global.interceptor.RequestLogPrintInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

/**
 * @author Wang Xu
 * @date 2020/10/11
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ArrayList<String> interceptors = new ArrayList<>();
        interceptors.add("");
        interceptors.add("/");
        interceptors.add("index.html#/**");
        interceptors.add("index.html/**");
        interceptors.add("/static/**");
        interceptors.add("/404");
        interceptors.add("/403");
        interceptors.add("/500");
        interceptors.add("/favicon.ico");
        interceptors.add("/swagger-ui.html");
        interceptors.add("/swagger-resources/**");
        registry.addInterceptor(new RequestLogPrintInterceptor())
                .addPathPatterns("/**/")
                .excludePathPatterns(interceptors.toArray(new String[]{}));
    }
}