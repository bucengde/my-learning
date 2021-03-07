package com.learning.qrcode.bean;

import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.base.ImageOperateUtil;
import com.github.hui.quick.plugin.qrcode.helper.QrCodeRenderHelper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Wang Xu
 * @date 2021/3/8
 */
public class IQrCodeRenderHelper extends QrCodeRenderHelper {


    public static BufferedImage drawFrontImg(BufferedImage qrImg, QrCodeOptions.FrontImgOptions frontImgOptions) {
        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        int resW = Math.max(frontImgOptions.getFtW(), qrWidth);
        int resH = Math.max(frontImgOptions.getFtH(), qrHeight);

        // 先将二维码绘制在底层背景图上
        int startX = frontImgOptions.getStartX(), startY = frontImgOptions.getStartY();
//        BufferedImage bottomImg = GraphicUtil.createImg(resW, resH, Math.max(startX, 0),
//                Math.max(startY, 0), qrImg, frontImgOptions.getFillColor());


        // 再画一层白布背景
        BufferedImage bottomImg = new BufferedImage(resW, resH, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bottomImg.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, resW, resH);
        graphics.drawImage(qrImg, startX, startY, null);

        BufferedImage ftImg = frontImgOptions.getFtImg();
        // 前置图支持设置圆角 or 圆形设置
        if (frontImgOptions.getImgStyle() == QrCodeOptions.ImgStyle.ROUND) {
            int cornerRadius = (int) (Math.min(resW, resH) * frontImgOptions.getRadius());
            ftImg = ImageOperateUtil.makeRoundedCorner(ftImg, cornerRadius);
        } else if (frontImgOptions.getImgStyle() == QrCodeOptions.ImgStyle.CIRCLE) {
            ftImg = ImageOperateUtil.makeRoundImg(ftImg, false, null);
        }

        Graphics2D g2d = GraphicUtil.getG2d(bottomImg);
        g2d.drawImage(ftImg, -Math.min(startX, 0), -Math.min(startY, 0), null);
        g2d.dispose();
        return bottomImg;
    }
}