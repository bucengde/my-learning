package com.learning.qrcode.utils;

import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.learning.commons.exception.RuntimeServerException;
import com.learning.qrcode.enums.QrCodeTypeEnum;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * @author Wang Xu
 * @date 2021/2/28
 */
public class QrCodeUtil {
    private static final int QR_CODE_ARC_WIGHT = 6;
    private static final int QR_CODE_ARC_HEIGHT = 6;
    private static final int QR_CODE_WIGHT = 300;
    private static final int QR_CODE_HEIGHT = 300;

    public static String generateNormal(String content, QrCodeTypeEnum qrCodeTypeEnum, InputStream bgImgInputStream) throws Exception {
        switch (qrCodeTypeEnum) {
            case normal:
                return generateNormal(content);
            case normalPreColor:
                return generateNormalPreColor(content);
            case normalColor2:
                return generateNormal(content);
            case normalColor3:
                return generateNormal(content);
            case normalBackground:
                return generateNormal(content);
            case normalStyle:
                return generateNormal(content);
            case normalImageFill:
                return generateNormal(content);
            case normalGif:
                return generateNormal(content);
            default:
                throw new RuntimeServerException("QrCodeType not match ...");
        }
    }

    public static String generateLogo(String content, QrCodeTypeEnum qrCodeTypeEnum, InputStream logoImgInputStream, InputStream bgImgInputStream) throws Exception {
        BufferedImage logoImage = ImageIO.read(logoImgInputStream);
        // 压缩logo，限制logo图片宽高最大为 100 * 100
        Image compressLogoImage = doCompressImage(logoImage, Math.min(logoImage.getWidth(), 100), Math.min(logoImage.getHeight(), 100));

        BufferedImage bufferedImage = toBufferedImage(compressLogoImage);
        switch (qrCodeTypeEnum) {
            case logo:
                return generateLogo(content, bufferedImage);
            case logoPreColor:
                return generateLogoPreColor(content, bufferedImage);
            case logoColor2:
                return generateNormal(content);
            case logoColor3:
                return generateNormal(content);
            case logoBackground:
                return generateNormal(content);
            case logoStyle:
                return generateNormal(content);
            case logoImageFill:
                return generateNormal(content);
            case logoGif:
                return generateNormal(content);
            default:
                throw new RuntimeServerException("QrCodeType not match ...");
        }
    }

    private static QrCodeGenWrapper.Builder getBasicQrCodeGenWrapperBuilder(String content) {
        return QrCodeGenWrapper.of(content).setW(QR_CODE_WIGHT).setH(QR_CODE_HEIGHT);
    }

    private static Image doCompressImage(BufferedImage image, int compressWidth, int compressHeight) {
        Image compressImage = image.getScaledInstance(compressWidth, compressHeight, Image.SCALE_SMOOTH);
        Graphics graphics = new BufferedImage(compressWidth, compressHeight, BufferedImage.TYPE_INT_RGB).getGraphics();
        graphics.drawImage(compressImage, 0, 0, null);
        // 释放资源
        graphics.dispose();
        return compressImage;
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        //boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
       /* if (hasAlpha) {
         transparency = Transparency.BITMASK;
         }*/

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            //int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
        /*if (hasAlpha) {
         type = BufferedImage.TYPE_INT_ARGB;
         }*/
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    /**
     * 生成颜色二维码-前景
     */
    private static String generateNormalPreColor(String content) throws Exception {
        return getBasicQrCodeGenWrapperBuilder(content)
                // 二维码背景图
                .setDrawBgColor(0xffffffff)
                // 二维码着色点
                .setDrawPreColor(Color.BLUE)
                // 定位点(探测图形)外边颜色
                .setDetectOutColor(Color.CYAN)
                // 定位点内部颜色
                .setDetectInColor(Color.RED)
                .setDetectSpecial()
                .asString();
    }

    /**
     * 生成颜色logo二维码-前景
     */
    private static String generateLogoPreColor(String content, BufferedImage logoImg) throws Exception {
        return getBasicQrCodeGenWrapperBuilder(content)
                .setDetectOutColor(Color.CYAN)
                .setDetectInColor(Color.RED)
                .setDetectSpecial()
                .setDrawPreColor(Color.BLUE)
                .setDrawBgColor(0xffffffff)
                .setLogo(logoImg)
                .setLogoRate(4)
                .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                .setLogoOpacity(0.8f)
                .asString();
    }

    /**
     * 普通logo二维码生成
     */
    private static String generateLogo(String content, BufferedImage logoImg) throws Exception {
        return getBasicQrCodeGenWrapperBuilder(content)
                .setLogo(logoImg)
                .setLogoRate(7)
                .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                .setLogoBgColor(0xfffefefe)
                .setLogoBorderBgColor(0xffc7c7c7)
                .setLogoBorder(true)
                .setLogoOpacity(0.8f)
                .asString();
    }

    /**
     * 普通二维码生成
     */
    private static String generateNormal(String content) throws Exception {
        return getBasicQrCodeGenWrapperBuilder(content).asString();
    }

    /**
     * 图片填充二维码生成
     */
    /**
     * 带有背景图的二维码生成
     */

}