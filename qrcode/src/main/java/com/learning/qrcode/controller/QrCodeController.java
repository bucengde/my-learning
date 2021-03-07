package com.learning.qrcode.controller;

import com.learning.commons.exception.RuntimeServerException;
import com.learning.qrcode.enums.QrCodeTypeEnum;
import com.learning.qrcode.enums.ZXingTypeEnum;
import com.learning.qrcode.utils.QrCodeUtil;
import com.learning.qrcode.utils.ZXingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Wang Xu
 * @date 2021/2/27
 */
@Slf4j
@RequestMapping("/qrCode/v1")
@RestController
public class QrCodeController {

    @PostMapping("/zxing/generate")
    public String generateByZXing(@RequestParam(value = "file", required = false) MultipartFile file,
                               @RequestParam("toLink") String toLink,
                               @RequestParam("type") Integer type) throws Exception {

        log.info("##################### file.name:{}, file.size:{}, toLike:{}, type:{}", Objects.isNull(file) ? null : file.getName(), Objects.isNull(file) ? null : file.getSize(), toLink, type);

        if (Objects.isNull(file)) {
            return ZXingUtil.generate(toLink, ZXingTypeEnum.getByType(type), null);
        }
        return ZXingUtil.generate(toLink, ZXingTypeEnum.getByType(type), file.getInputStream());
    }

    @PostMapping("/qrCode/generate")
    public String generateByQrCode(@RequestParam(value = "logo", required = false) MultipartFile logo,
                                   @RequestParam(value = "bg", required = false) MultipartFile bg,
                                   @RequestParam("content") String content,
                                   @RequestParam("type") Integer type,
                                   @RequestParam("bgOpacity") Float bgOpacity) throws Exception {

        log.info("##################### logo.name:{}, bg.size:{}, toLike:{}, type:{}, bgOpacity:{}", Objects.isNull(logo) ? null : logo.getName(), Objects.isNull(bg) ? null : bg.getSize(), content, type, bgOpacity);

        if (QrCodeTypeEnum.isLogoType(type)) {
            Optional.ofNullable(logo).orElseThrow(() -> new RuntimeServerException("logo file not is null"));
            return QrCodeUtil.generateLogo(content, QrCodeTypeEnum.getByType(type), logo.getInputStream(), Objects.isNull(bg) ? null : bg.getInputStream());
        }
        return QrCodeUtil.generateNormal(content, QrCodeTypeEnum.getByType(type), Objects.isNull(bg) ? null : bg.getInputStream(), bgOpacity);
    }
}