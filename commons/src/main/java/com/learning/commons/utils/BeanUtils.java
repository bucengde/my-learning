package com.learning.commons.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Bean copy 工具
 * @author Wang Xu
 * @date 2020/10/4
 */
public class BeanUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    public static class DozerBeanMapperUtil {
        // 避免重复创建的开销
        private static final Mapper MAPPER = DozerBeanMapperBuilder.buildDefault();

        /**
         * Bean copy（基于Dozer，深拷贝）
         * @param source 源对象
         * @param target 目标对象
         */
        public static <T, R> void copy(T source, R target) {
            if (Objects.nonNull(source)) {
                MAPPER.map(source, target);
            }
        }

        public static <T, R> R map(T source, Class<R> targetClazz) {
            if (Objects.isNull(source)) {
                return null;
            }
            return MAPPER.map(source, targetClazz);
        }

        /**
         * Bean Collection copy（基于Dozer，深拷贝）
         * @param sourceList 源对象集合
         * @param targetClazz 目标对象class
         * @return List<R>
         */
        public static <T, R> List<R> mapList(List<T> sourceList, Class<R> targetClazz) {
            if (Objects.isNull(sourceList) || sourceList.size() == 0) {
                return Collections.emptyList();
            }
            final List<R> result = new ArrayList<>(sourceList.size());
            sourceList.forEach(item -> {
                result.add(MAPPER.map(item, targetClazz));
            });
            return result;
        }
    }
}