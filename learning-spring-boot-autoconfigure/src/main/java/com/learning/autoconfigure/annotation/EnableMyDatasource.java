package com.learning.autoconfigure.annotation;

import com.learning.autoconfigure.selector.MyDatasourceSelector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Wang Xu
 * @date 2020/10/19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited

@Import({MyDatasourceSelector.class})
@MapperScan
public @interface EnableMyDatasource {
    @AliasFor(annotation = MapperScan.class)
    String[] value() default {};
}