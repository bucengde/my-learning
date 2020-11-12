package com.learning.commons.bean.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信类型
 */
@Getter
@AllArgsConstructor
public enum SmsTypeEnum {
    AW(1, "昂网"),
    NFTS(2, "9527"),
    ;

    /** 类型 */
    private int type;
    /** 描述 */
    private String desc;
}
