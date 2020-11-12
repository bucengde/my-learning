package com.learning.vps.utils;

/**
 * @author Wang Xu
 * @date 2020/11/11
 */
public class MediaUtils {
    private final static Integer LENGTH_OF_ONE_MESSAGE = 70;

    public static Integer calculationSmsCount(String smsContent) {
        int len = 0;
        if (smsContent == null || (len = smsContent.length()) == 0) {
            return len;
        }
        int size = smsContent.length() / LENGTH_OF_ONE_MESSAGE;
        int size2 = smsContent.length() % LENGTH_OF_ONE_MESSAGE;
        if (size2 > 0){
            size ++;
        }
        return size;
    }

}