package com.learning.qrcode.utils;

import com.google.common.base.Charsets;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.learning.commons.exception.RuntimeServerException;
import com.learning.qrcode.enums.ZXingTypeEnum;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Wang Xu
 * @date 2021/2/28
 */
public class ZXingUtil {
    private static final int Z_X_ARC_WIGHT = 6;
    private static final int Z_X_ARC_HEIGHT = 6;
    private static final int Z_X_WIGHT = 300;
    private static final int Z_X_HEIGHT = 300;
    private static final float SHARE_STROKE_WIDTH = 3f;

    public static String generate(String content, ZXingTypeEnum typeEnum, InputStream imgInputStream) throws Exception {
        switch (typeEnum) {
            case normal:
                return generateNormal(content);
            case logo:
                return generateLogo(content, imgInputStream);
            default:
                throw new RuntimeServerException("type not match ...");
        }
    }

    /**
     * 带有logo的二维码生成
     */
    private static String generateLogo(String toLink, InputStream imgInputStream) throws Exception {
        if (Objects.isNull(imgInputStream)) {
            return generateNormal(toLink);
        }
        BufferedImage bufferedImage = doGenerateBufferedImage(toLink);
        Objects.requireNonNull(Optional.of(bufferedImage));
        doLogoDraw(bufferedImage, imgInputStream);
        return bufferedImage2Base64Str(bufferedImage);
    }

    private static Image doCompressImage(BufferedImage image, int compressWidth, int compressHeight) {
        Image compressImage = image.getScaledInstance(compressWidth, compressHeight, Image.SCALE_SMOOTH);
        Graphics graphics = new BufferedImage(compressWidth, compressHeight, BufferedImage.TYPE_INT_RGB).getGraphics();
        graphics.drawImage(compressImage, 0, 0, null);
        // 释放资源
        graphics.dispose();
        return compressImage;
    }

    private static void doLogoDraw(BufferedImage qrCodeImage, InputStream imgInputStream) throws Exception {
        BufferedImage logoImage = ImageIO.read(imgInputStream);
        // 压缩logo，限制logo图片宽高最大为 100 * 100
        int width = Math.min(logoImage.getWidth(), 100);
        int height = Math.min(logoImage.getHeight(), 100);

//        Image compressLogoImage = logoImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        Graphics graphics = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB).getGraphics();
//        graphics.drawImage(compressLogoImage, 0, 0, null);
//        // 释放资源
//        graphics.dispose();

        Image compressLogoImage = doCompressImage(logoImage, width, height);

        // 在二维码图上画2D的logo图
        Graphics2D g = qrCodeImage.createGraphics();
        // 计算绘制起点
        int x = (Z_X_WIGHT - width) / 2;
        int y = (Z_X_HEIGHT - height) / 2;
        // 画logo
        g.drawImage(compressLogoImage, x, y, null);

        // 用圆角矩形来修饰logo边界
        Shape shape = new RoundRectangle2D.Float(x, y, width, height, Z_X_ARC_WIGHT, Z_X_ARC_HEIGHT);
        // 设置画笔粗细
        g.setStroke(new BasicStroke(SHARE_STROKE_WIDTH));
        // 消除锯齿边缘
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 设置线条颜色
        g.setColor(Color.black);
        g.draw(shape);
        g.dispose();
    }

    /**
     * 普通二维码生成
     */
    private static String generateNormal(String content) throws Exception {
        return bufferedImage2Base64Str(doGenerateBufferedImage(content));
    }

    private static BufferedImage doGenerateBufferedImage(String content) throws Exception {
        Map<EncodeHintType, Object> param = new HashMap<>();
        // 设置字符集
        param.put(EncodeHintType.CHARACTER_SET, Charsets.UTF_8);
        // 设置二维码边距
        param.put(EncodeHintType.MARGIN, 1);
        // 设置精度(纠错程度)
        param.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, Z_X_WIGHT, Z_X_HEIGHT, param);
        BufferedImage bufferedImage = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int w = 0; w < bitMatrix.getWidth(); w++) {
            for (int h = 0; h < bitMatrix.getHeight(); h++) {
                bufferedImage.setRGB(w, h, bitMatrix.get(w, h) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bufferedImage;
    }

    /**
     * 将bufferedImage的字节数组转为base64加密后的字符串
     */
    public static String bufferedImage2Base64Str(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        return Base64.encodeBase64String(byteArrayOutputStream.toByteArray());
    }
}