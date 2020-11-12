package com.learning.vps.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * @Description <p>MD5加密</p>
 * @author: Wang Xu
 * @date: 2019年4月26日 下午6:51:30
 */
public class MD5Util {
	private static final Logger LOGGER = LoggerFactory.getLogger(MD5Util.class);
	private static final String[] HEX_DIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	private static final String SALT = "palette";

	private static String byteArrayToHexString(byte[] b) {
		StringBuilder resultStringBuffer = new StringBuilder();
        for (byte value : b) {
            resultStringBuffer.append(byteToHexString(value));
        }
		return resultStringBuffer.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n += 256;
        }
		int d1 = n / 16;
		int d2 = n % 16;
		return HEX_DIGITS[d1] + HEX_DIGITS[d2];
	}

	/**
	 * 返回大写MD5
	 */
	static String MD5Encode(String origin, String charsetName) {
		String resultString = null;
		try {
			resultString = origin;
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (StringUtils.isBlank(charsetName)) {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			} else {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
			}
		} catch (Exception e) {
			LOGGER.error("###################### MD5加密失败，异常信息为：{}", e.toString(), e);
			return origin;
		}
		return resultString.toUpperCase();
	}

    /**
     * 返回小写MD5
     */
    public static String getLowerCaseMD5Encode(String origin, String charsetName) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (StringUtils.isBlank(charsetName)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        } catch (Exception e) {
            LOGGER.error("###################### MD5加密失败，异常信息为：{}", e.toString(), e);
            return origin;
        }
        return resultString.toLowerCase();
    }

	/**
	 * @Description: <p>会从类路径下的application.properties文件中来获取一个额外的字符串，
	 * 		以保证碰撞算法的命中率更低，且在第三方MD5平台上也很难被搜索到。</p>
	 * @param origin
	 * @param propertiesName
	 * @return
	 */
	public static String MD5EncodeUtf8(String origin, String propertiesName) {
//		String readProperty = PropertiesUtil.readProperty("application.properties", propertiesName);
//		origin = origin + (StringUtils.isEmpty(readProperty) ? SALT : readProperty);
		return MD5Encode(origin, "UTF-8");
	}

}
