package com.learning.es.bean.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

/**
 * @author Wang Xu
 * @date 2020/10/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodInfo {
    private String img;
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;
    private String price;
    private String shop;
}