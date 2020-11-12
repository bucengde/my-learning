package com.learning.vps.eunm;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 媒体类型枚举
 * @author Wang Xu
 * @date 2020/11/11
 */
@Getter
@AllArgsConstructor
public enum MediaTypeEnum {
    /** 短信类型 */
    SMS(1, "短信"),

    ;

    /** 类型 */
    private int mediaType;
    /** 描述 */
    private String desc;

}
