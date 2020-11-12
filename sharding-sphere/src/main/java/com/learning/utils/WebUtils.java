package com.learning.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Wang Xu
 * @date 2020/10/11
 */
public class WebUtils {
    private static ThreadLocal<Map<String, Object>> HEADERS = new ThreadLocal<>();

    public static class HeaderUtil {
        static {
            HEADERS.set(new HashMap<>());
        }

        public static Map<String, Object> getHeaders() {
            return HEADERS.get();
        }

        public static Object getHeader(String name) {
            return HEADERS.get().get(name);
        }

        public static void setHeaders(Map<String, Object> headers) {
            HEADERS.set(headers);
        }

        public static synchronized void setHeader(String name, Object obj) {
            Optional.ofNullable(HEADERS.get()).orElseGet(() -> {
                HEADERS.set(new HashMap<>());
                return HEADERS.get();
            }).put(name, obj);
        }

        public static void remove() {
            HEADERS.remove();
        }
    }
}