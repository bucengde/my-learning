package com.learning.qrcode.bean;

import com.github.hui.quick.plugin.qrcode.helper.QrCodeGenerateHelper;
import com.github.hui.quick.plugin.qrcode.helper.QrCodeRenderHelper;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Wang Xu
 * @date 2021/3/7
 */
public class IQrCodeGenerateHelper extends QrCodeGenerateHelper {
    public static List<ImmutablePair<BufferedImage, Integer>> toGifImages(QrCodeOptions qrCodeConfig,
                                                                          BitMatrixEx bitMatrix) {
        boolean preGif = qrCodeConfig.getFtImgOptions() != null && qrCodeConfig.getFtImgOptions().getGifDecoder() != null && qrCodeConfig.getFtImgOptions().getGifDecoder().getFrameCount() > 0;
        boolean bgGif = qrCodeConfig.getBgImgOptions() != null && qrCodeConfig.getBgImgOptions().getGifDecoder() != null && qrCodeConfig.getBgImgOptions().getGifDecoder().getFrameCount() > 0;
        if (!bgGif && !preGif) {
            throw new IllegalArgumentException("animated background image should not be null!");
        }

        BufferedImage qrCode = QrCodeRenderHelper.drawQrInfo(qrCodeConfig, bitMatrix);


        boolean logoAlreadyDraw = false;
        if (qrCodeConfig.getLogoOptions() != null &&
                qrCodeConfig.getBgImgOptions().getBgImgStyle() == QrCodeOptions.BgImgStyle.FILL) {
            // 此种模式，先绘制logo
            qrCode = QrCodeRenderHelper.drawLogo(qrCode, qrCodeConfig.getLogoOptions());
            logoAlreadyDraw = true;
        }

        boolean flag = false;
        QrCodeOptions.FrontImgOptions ftImgOptions = null;
        if (!preGif && qrCodeConfig.getFtImgOptions() != null && qrCodeConfig.getFtImgOptions().getFtImg() != null) {
            ftImgOptions = qrCodeConfig.getFtImgOptions();
            qrCodeConfig.setFtImgOptions(null);
            flag = true;
        }

        // 绘制动态背景图
        List<ImmutablePair<BufferedImage, Integer>> bgList = bgGif ?
                QrCodeRenderHelper.drawGifBackground(qrCode, qrCodeConfig.getBgImgOptions()) : Collections.emptyList();


        if (!logoAlreadyDraw && qrCodeConfig.getLogoOptions() != null) {
            if (bgGif) {
                // 插入logo
                List<ImmutablePair<BufferedImage, Integer>> result = new ArrayList<>(bgList.size());
                for (ImmutablePair<BufferedImage, Integer> pair : bgList) {
                    result.add(ImmutablePair.of(QrCodeRenderHelper.drawLogo(pair.getLeft(), qrCodeConfig.getLogoOptions()),
                            pair.getRight()));
                }
                // bg 是gif，ft是非gif
                List<ImmutablePair<BufferedImage, Integer>> result2 = new ArrayList<>(bgList.size());
//                if (!preGif && qrCodeConfig.getFtImgOptions() != null && qrCodeConfig.getFtImgOptions().getFtImg() != null) {
//                    QrCodeOptions.FrontImgOptions ftImgOptions = qrCodeConfig.getFtImgOptions();
//                    qrCodeConfig.setFtImgOptions(new QrCodeOptions.FrontImgOptions(ftImgOptions.getFtImg(),
//                            ftImgOptions.getImgStyle(), ftImgOptions.getRadius(), null, qrCodeConfig.getFtImgOptions().getFtW(),
//                            qrCodeConfig.getFtImgOptions().getFtH(),
//                            0, 0, qrCodeConfig.getFtImgOptions().getFillColor()));
//                    for (ImmutablePair<BufferedImage, Integer> pair : result) {
//                        result2.add(ImmutablePair.of(QrCodeRenderHelper.drawFrontImg(pair.getLeft(), qrCodeConfig.getFtImgOptions()), pair.getRight()));
//                    }
//                }
                if (flag) {
                    qrCodeConfig.setFtImgOptions(ftImgOptions);
                    for (ImmutablePair<BufferedImage, Integer> pair : result) {
                        result2.add(ImmutablePair.of(IQrCodeRenderHelper.drawFrontImg(pair.getLeft(), qrCodeConfig.getFtImgOptions()), pair.getRight()));
                    }

                }
                bgList = result2.size() == 0 ? result : result2;
            } else {
                qrCode = QrCodeRenderHelper.drawLogo(qrCode, qrCodeConfig.getLogoOptions());
            }
        }


        if (bgGif) {
            return bgList;
        }

        // todo 暂时不支持前置图和背景图同时设置为gif的场景，如果同时存在，只保存背景gif图

        // 前置图为gif的场景
        return QrCodeRenderHelper.drawFrontGifImg(qrCode, qrCodeConfig.getFtImgOptions());
    }
}