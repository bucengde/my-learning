package com.learning.qrcode.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum QrCodeTypeEnum {
    normal(1, "普通二维码生成"),
    normalPreColor(2, "普通-前景颜色二维码生成"),
    normalColor2(3, "普通-颜色二维码生成"),
    normalColor3(4, "普通-颜色二维码生成"),
    normalBackground(5, "普通-背景二维码生成"),
    normalStyle(6, "普通-特殊形状二维码生成"),
    normalImageFill(7, "普通-图片填充二维码生成"),
    normalGif(8, "普通-gif二维码生成"),

    logo(9, "logo二维码生成"),
    logoPreColor(10, "logo-前景颜色二维码生成"),
    logoColor2(11, "logo-颜色二维码生成"),
    logoColor3(12, "logo-颜色二维码生成"),
    logoBackground(13, "logo-背景二维码生成"),
    logoStyle(14, "logo-特殊形状二维码生成"),
    logoImageFill(15, "logo-图片填充二维码生成"),
    logoGif(16, "logo-gif二维码生成"),

    ;

    private final int type;
    private final String desc;

    public static QrCodeTypeEnum getByType(Integer type) {
        return Arrays.stream(QrCodeTypeEnum.values()).filter(it -> it.type == type).findFirst().get();
    }

    public static List<QrCodeTypeEnum> getLogoQrCodeEnumList() {
        ArrayList<QrCodeTypeEnum> objects = Lists.newArrayList();
        objects.add(logo);
        objects.add(logoPreColor);
        objects.add(logoColor2);
        objects.add(logoColor3);
        objects.add(logoBackground);
        objects.add(logoStyle);
        objects.add(logoImageFill);
        objects.add(logoGif);
        return objects;
    }

    public static boolean isLogoType(Integer type) {
        return Objects.nonNull(type) && getLogoQrCodeEnumList().stream().map(QrCodeTypeEnum::getType).collect(Collectors.toList()).contains(type);
    }
}
