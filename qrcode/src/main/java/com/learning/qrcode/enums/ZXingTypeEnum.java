package com.learning.qrcode.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ZXingTypeEnum {
    normal(1, "普通二维码生成"),
    logo(2, "logo二维码生成"),

    ;

    private final int type;
    private final String desc;

    public static ZXingTypeEnum getByType(Integer type) {
        return Arrays.stream(ZXingTypeEnum.values()).filter(it -> it.type == type).findFirst().get();
    }
}
