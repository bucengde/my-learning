package com.learning.global.interceptor;

import com.learning.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Xu
 * @date 2020/10/11
 */
public class RequestLogPrintInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLogPrintInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.info("请求URL：" + request.getRequestURI());
        LOGGER.info("请求IP信息：" + request.getRemoteAddr());
        LOGGER.info("请求HEADERS如下:");
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headers= new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String hn = headerNames.nextElement();
            headers.put(hn, request.getHeader(hn));
            LOGGER.info(hn + "：" + request.getHeader(hn));
        }
        LOGGER.info("入参信息：" + request.getParameterMap().toString());

        WebUtils.HeaderUtil.setHeaders(headers);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Map<String, Object> headers = WebUtils.HeaderUtil.getHeaders();
        LOGGER.info("postHandle headers: {}", headers.toString());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        WebUtils.HeaderUtil.remove();
    }
}