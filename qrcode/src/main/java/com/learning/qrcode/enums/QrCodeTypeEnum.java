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
    normalCoverBackground(3, "普通-全覆盖背景二维码生成"),
    normalFillBackground(4, "普通-填充背景二维码生成"),
    normalRenderingBackground(5, "普通-背景渲染二维码生成"),
    normalStyle(6, "普通-特殊形状二维码生成"),
    normalImageFill(7, "普通-图片填充二维码生成"),
    normalGifCoverBackground(8, "普通-gif全覆盖二维码生成"),
    normalGifFillBackground(17, "普通-gif填充二维码生成"),
    normalGifRenderingBackground(18, "普通-gif渲染二维码生成"),

    logo(9, "logo二维码生成"),
    logoPreColor(10, "logo-前景颜色二维码生成"),
    logoCoverBackground(11, "logo-全覆盖背景二维码生成"),
    logoFillBackground(12, "logo-填充背景二维码生成"),
    logoRenderingBackground(13, "logo-背景渲染二维码生成"),
    logoStyle(14, "logo-特殊形状二维码生成"),
    logoImageFill(15, "logo-图片填充二维码生成"),
    logoGifCoverBackground(16, "logo-gif全覆盖二维码生成"),
    logoGifFillBackground(19, "logo-gif填充二维码生成"),
    logoGifRenderingBackground(20, "logo-gif渲染二维码生成"),
    logoRedHeartBackground(21, "logo-前景红心二维码生成"),

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
        objects.add(logoCoverBackground);
        objects.add(logoFillBackground);
        objects.add(logoRenderingBackground);
        objects.add(logoStyle);
        objects.add(logoImageFill);
        objects.add(logoGifCoverBackground);
        objects.add(logoGifFillBackground);
        objects.add(logoGifRenderingBackground);
        objects.add(logoRedHeartBackground);
        return objects;
    }

    public static boolean isLogoType(Integer type) {
        return Objects.nonNull(type) && getLogoQrCodeEnumList().stream().map(QrCodeTypeEnum::getType).collect(Collectors.toList()).contains(type);
    }
}
