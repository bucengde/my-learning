package com.learning.qrcode.utils;

import com.github.hui.quick.plugin.base.ColorUtil;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.learning.commons.exception.RuntimeServerException;
import com.learning.qrcode.enums.QrCodeTypeEnum;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author Wang Xu
 * @date 2021/2/28
 */
public class QrCodeUtil {
    private static final int QR_CODE_ARC_WIGHT = 6;
    private static final int QR_CODE_ARC_HEIGHT = 6;
    private static final int QR_CODE_WIGHT = 300;
    private static final int QR_CODE_HEIGHT = 300;

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

    public static String generateNormal(String content, QrCodeTypeEnum qrCodeTypeEnum, InputStream bgImgInputStream) throws Exception {
        switch (qrCodeTypeEnum) {
            case normal:
                return generateNormal(content);
            case normalPreColor:
                return generateNormalPreColor(content);
            case normalCoverBackground:
                return generateNormalCoverBg(content, bgImgInputStream);
            case normalFillBackground:
                return generateNormalFillBg(content, bgImgInputStream);
            case normalRenderingBackground:
                return generateNormalRenderingBg(content, bgImgInputStream);
            case normalStyle:
                return generateNormalStyle(content);
            case normalImageFill:
                return generateNormalImageFill(content, bgImgInputStream);
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
            case logoCoverBackground:
                return generateLogoCoverBg(content, bufferedImage, bgImgInputStream);
            case logoFillBackground:
                return logoFillBackground(content, bufferedImage, bgImgInputStream);
            case logoRenderingBackground:
                return generateLogoRenderingBg(content, bufferedImage, bgImgInputStream);
            case logoStyle:
                return generateLogoStyle(content, bufferedImage);
            case logoImageFill:
                return generateNormal(content);
            case logoGif:
                return generateNormal(content);
            default:
                throw new RuntimeServerException("QrCodeType not match ...");
        }
    }

    /**
     * 普通-图片填充二维码生成
     */
    private static String generateNormalImageFill(String content, InputStream bgImgInputStream) throws Exception {
        return getBasicQrCodeGenWrapperBuilder(content)
                // 前置爱心图
//                .setW(500)
//                .setH(500)
//                // 二维码绘制在前置图上的坐标
//                .setDrawPreColor(Color.RED)
//                .setFtImg("https://cdn.pixabay.com/photo/2017/06/14/12/58/heart-2402086_960_720.png")
//                .setFtStartX(110)
//                .setFtStartY(120)

                // 填充图片
                .setErrorCorrection(ErrorCorrectionLevel.H)
                // 因为素材为png透明图，我们这里设置二维码的背景为透明，输出更加优雅
                .setDrawBgColor(ColorUtil.OPACITY)
                .setDetectImg("jihe/PDP.png")
                .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE)
                .addImg(1, 1, "jihe/a.png")
                .addImg(3, 1, "jihe/b.png")
                .addImg(1, 3, "jihe/c.png")
                .addImg(3, 2, "jihe/e.png")
                .addImg(2, 3, "jihe/f.png")
                .addImg(2, 2, "jihe/g.png")
                .addImg(3, 4, "jihe/h.png")
                .setPicType("png")
                .asString();

    }

    /**
     * logo-几何形状二维码生成(常见的几何形式支持，如矩形 RECT，圆点 CIRCLE，三角 TRIANGLE，钻石 DIAMOND，六边形 SEXANGLE，八边形 OCTAGON)
     *
     */
    private static String generateLogoStyle(String content, BufferedImage logoImg) throws Exception {
        return getBasicQrCodeGenWrapperBuilder(content)
                .setDrawEnableScale(true)
                .setDrawStyle(QrCodeOptions.DrawStyle.DIAMOND)
                .setLogo(logoImg)
                .setLogoRate(4)
                .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                .setLogoOpacity(0.8f)
                .asString();
    }

    /**
     * 普通-几何形状二维码生成(常见的几何形式支持，如矩形 RECT，圆点 CIRCLE，三角 TRIANGLE，钻石 DIAMOND，六边形 SEXANGLE，八边形 OCTAGON)
     *
     */
    private static String generateNormalStyle(String content) throws Exception {
        return getBasicQrCodeGenWrapperBuilder(content)
                // 支持将临近相同的合并成一个大的圆点
                .setDrawEnableScale(true)
                .setDrawStyle(QrCodeOptions.DrawStyle.DIAMOND)
                .asString();
    }

    /**
     * logo-背景渲染二维码生成
     */
    private static String generateLogoRenderingBg(String content, BufferedImage logoImg, InputStream bgImgInputStream) throws Exception {
        Optional.ofNullable(bgImgInputStream).orElseThrow(() -> new RuntimeServerException("background file not is null"));
        return getBasicQrCodeGenWrapperBuilder(content)
                .setBgStyle(QrCodeOptions.BgImgStyle.PENETRATE)
                .setBgW(QR_CODE_WIGHT)
                .setBgH(QR_CODE_HEIGHT)
                .setBgImg(bgImgInputStream)
                .setLogo(logoImg)
                .setLogoRate(4)
                .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                .setLogoOpacity(0.8f)
                .asString();
    }

    /**
     * 普通-背景渲染二维码生成
     */
    private static String generateNormalRenderingBg(String content, InputStream bgImgInputStream) throws Exception {
        Optional.ofNullable(bgImgInputStream).orElseThrow(() -> new RuntimeServerException("background file not is null"));
        return getBasicQrCodeGenWrapperBuilder(content)
                .setBgStyle(QrCodeOptions.BgImgStyle.PENETRATE)
                .setBgW(QR_CODE_WIGHT)
                .setBgH(QR_CODE_HEIGHT)
                .setBgImg(bgImgInputStream)
                .asString();
    }

    /**
     * logo-填充背景二维码生成
     */
    private static String logoFillBackground(String content, BufferedImage logoImg, InputStream bgImgInputStream) throws Exception {
        Optional.ofNullable(bgImgInputStream).orElseThrow(() -> new RuntimeServerException("background file not is null"));
        return getBasicQrCodeGenWrapperBuilder(content)
                // 指定为背景图填充模式
                .setBgStyle(QrCodeOptions.BgImgStyle.FILL)
                .setBgW(500)
                .setBgH(500)
                // 二维码回执开始坐标
                .setBgStartX((500 - QR_CODE_WIGHT) / 2)
                .setBgStartY((500 - QR_CODE_HEIGHT) / 2)
                .setBgImg(bgImgInputStream)
                .setPadding(0)
//                .setDrawBgColor(Color.BLUE)
                .setDrawBgColor(0xfff7f7f7)
                .setLogo(logoImg)
                .setLogoRate(4)
                .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                .setLogoOpacity(0.8f)
                .asString();
    }

    /**
     * 普通-填充背景二维码生成
     */
    private static String generateNormalFillBg(String content, InputStream bgImgInputStream) throws Exception {
        Optional.ofNullable(bgImgInputStream).orElseThrow(() -> new RuntimeServerException("background file not is null"));
        return getBasicQrCodeGenWrapperBuilder(content)
                // 指定为背景图填充模式
                .setBgStyle(QrCodeOptions.BgImgStyle.FILL)
                .setBgW(500)
                .setBgH(500)
                // 二维码回执开始坐标
                .setBgStartX((500 - QR_CODE_WIGHT) / 2)
                .setBgStartY((500 - QR_CODE_HEIGHT) / 2)
                .setBgImg(bgImgInputStream)
                .setPadding(0)
                .setDrawBgColor(0xfff7f7f7)
                .asString();
    }

    /**
     * logo-全覆盖背景二维码生成
     */
    private static String generateLogoCoverBg(String content, BufferedImage logoImg, InputStream bgImgInputStream) throws Exception {
        Optional.ofNullable(bgImgInputStream).orElseThrow(() -> new RuntimeServerException("background file not is null"));
        return getBasicQrCodeGenWrapperBuilder(content)
                .setBgW(QR_CODE_WIGHT)
                .setBgH(QR_CODE_HEIGHT)
                .setBgImg(bgImgInputStream)
                .setBgOpacity(0.5f)
                .setLogo(logoImg)
                .setLogoRate(7)
                .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                .asString();
    }

    /**
     * 普通-全覆盖背景二维码生成
     */
    private static String generateNormalCoverBg(String content, InputStream bgImgInputStream) throws Exception {
        Optional.ofNullable(bgImgInputStream).orElseThrow(() -> new RuntimeServerException("background file not is null"));
        return getBasicQrCodeGenWrapperBuilder(content)
                .setBgW(QR_CODE_WIGHT)
                .setBgH(QR_CODE_HEIGHT)
                .setBgImg(bgImgInputStream)
                .setBgOpacity(0.5f)
                .asString();
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