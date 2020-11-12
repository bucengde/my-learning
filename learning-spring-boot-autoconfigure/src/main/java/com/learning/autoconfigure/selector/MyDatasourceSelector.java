package com.learning.autoconfigure.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wang Xu
 * @date 2020/10/19
 */
public class MyDatasourceSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        List<String> arr = new ArrayList<>();
        arr.add("com.learning.autoconfigure.aware.ContextConfiguration");
        arr.add("com.learning.autoconfigure.datasource.MybatisPlusAutoConfiguration");
        return StringUtils.toStringArray(arr);
    }
}