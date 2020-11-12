package com.learning.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.learning.global.CodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean copy 工具
 * @author Wang Xu
 * @date 2020/10/4
 */
public class BeanUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    public static class CglibBeanCopyUtil {
        // cglib的BeanCopier功能很强大，不过频繁的create太占用资源，降低服务器性能
        // 所以用缓存将类型相同的copier缓存起来
        private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>(64);
        private static final String ERROR_CODE = "BEAN_COPY_ERROR_NEED_NO_CONSTRUCTOR";
        private static final String ERROR_MSG = "目标类需要有无参构造器";
        private static final String ERROR_CODE_FROM_INIT = "BEAN_COPY_ERROR_METHOD_NOT_FOUND";
        private static final String ERROR_MSG_FROM_INIT = "初始化实例未发现方法";

        /**
         * 复制对象（浅拷贝）
         * @param source 源对象
         * @param targetClazz 目标对象class
         * @return R 目标对象
         * @throws CodeException 业务代码异常
         */
        public static <T, R> R map(T source, Class<R> targetClazz) throws CodeException {
            if (Objects.isNull(source)) {
                return null;
            }
            R instance = null;
            try {
                instance = targetClazz.newInstance();
            } catch (IllegalAccessException e) {
                LOGGER.error(targetClazz.getName() + " 无无参构造函数, 异常信息为：{}", e.toString(), e);
                throw new CodeException(ERROR_CODE, ERROR_MSG);
            } catch (InstantiationException e) {
                LOGGER.error("BEAN实例化异常，异常信息为：{}", e.toString(), e);
                throw new CodeException(ERROR_CODE_FROM_INIT, ERROR_MSG_FROM_INIT);
            }
            copy(source, instance);
            return instance;
        }

        /**
         * 复制对象（浅拷贝）
         * @param source 源对象
         * @param target 目标对象
         * @throws CodeException 业务代码异常
         */
        public static <T, R> void copy(T source, R target) {
            if (Objects.isNull(source) || Objects.isNull(target)) {
                return;
            }
            BeanCopier beanCopier = BEAN_COPIER_CACHE.computeIfAbsent(genKey(source.getClass(), target.getClass()), k ->
                    // 一旦使用Converter，BeanCopier只使用Converter定义的规则去拷贝属性，
                    // 所以在convert方法中要考虑所有的属性
                    // BeanCopier对不同对象中的List对象集合类型的属性的拷贝是弱拷贝，而不是深拷贝，
                    // 如果只是做对象拷贝，然后直接抛出这个对象给前台使用是没有问题的，
                    // 但是如果这个通过拷贝得到的对象要在代码中进行业务流转，则会报java.lang.ClassCastException 类强转异常
                    BeanCopier.create(source.getClass(), target.getClass(), false)
            );
            if (Objects.nonNull(beanCopier)) {
                beanCopier.copy(source, target, null);
                return;
            }
            BEAN_COPIER_CACHE.get(genKey(source.getClass(), target.getClass())).copy(source.getClass(), target, null);
        }

        /**
         * 集合复制对象（浅拷贝）
         * @param sourceList 源对象集合
         * @param targetClazz 目标对象class
         * @return List<R>
         * @throws CodeException 业务代码异常
         */
        public static <T, R> List<R> mapList(List<T> sourceList, Class<R> targetClazz) throws CodeException {
            if (Objects.isNull(sourceList) || sourceList.size() == 0) {
                return Collections.emptyList();
            }
            final List<R> result = new ArrayList<>(sourceList.size());
            for (T item : sourceList) {
                result.add(map(item, targetClazz));
            }
            return result;
        }

        /**
         * 生成key
         * @param sourceClazz 源文件的class
         * @param targetClazz 目标文件的class
         * @return string
         */
        private static String genKey(Class<?> sourceClazz, Class<?> targetClazz) {
            return sourceClazz.getName() + targetClazz.getName();
        }
    }


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